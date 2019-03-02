package ir.saj.adventure.bot;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import ir.saj.adventure.bot.callback.Callback;
import ir.saj.adventure.bot.callback.GetFileCallback;
import ir.saj.adventure.bot.handlers.ByteArrayHandler;
import ir.saj.adventure.bot.handlers.StringHandler;
import ir.saj.adventure.bot.interceptor.Interceptor;
import ir.saj.adventure.bot.user.BotUserEntityInterface;
import ir.saj.adventure.bot.user.BotUserServiceInterface;
import ir.saj.adventure.bot.user.DefaultBotUserService;
import ir.saj.adventure.exception.BadRequestException;
import ir.saj.adventure.utils.Session;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Saeed
 *         Date: 8/8/2017
 */
public class BotManager {
    public static final int PHOTO_CAPTION_LIMIT = 1024;
    public static final int NUMBER_OF_CUNCURRENT_MESSAGES = 29;
    public static final Date START_DATE = new Date();
    private static final int DEFAULT_INTERVAL = 300;
    private static final String BOT_NAME_DEFAULT = "BOT_NAME";
    final Integer updateInterval;
    final Integer retryCountLimit = 5;
    BotUserServiceInterface dbUserService;
    TelegramBot bot;
    Integer lastUpdateId = -1;
    ArrayBlockingQueue<Sendable> sendQueue = new ArrayBlockingQueue<>(2000);
    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUMBER_OF_CUNCURRENT_MESSAGES);
    List<Interceptor> interceptors = new ArrayList<>();
    Long sequence = 0L;
    Long updateFails = 0L;
    Long sendMessageFails = 0L;
    Long retries = 0L;
    Set<String> updateErors = new HashSet<>();
    HashMap<String, Counter> sendMessageErrors = new HashMap<>();
    private String botUserName;
    private String botName;
    private String token;
    private String basePath = "D:\\";
    private Long retryQueueSize = 0L;
    private OkHttpClient client = null;
    final Logger logger = LoggerFactory.getLogger(BotManager.class);
    Session session = new Session();
    private FludFilter fludFilter = new FludFilter();

    public Session getSession() {
        return session;
    }

    public BotManager(String token) {
        this(token, null, null, null, null, null);
    }

    public BotManager(String token, Proxy proxy) {
        this(token, null, null, null, null, proxy);
    }
    public BotManager(String token, BotUserServiceInterface dbUserService, Integer updateInterval, String botUserName, String botName) {
        this(token, dbUserService, updateInterval, botUserName, botName, null);
    }

    public BotManager(String token, BotUserServiceInterface dbUserService, Integer updateInterval, String botUserName, String botName, Proxy proxy) {
        this.token = token;
        if (proxy == null)
            this.bot = TelegramBotAdapter.build(token);
        else {
            OkHttpClient.Builder builder = new OkHttpClient.Builder().proxy(proxy);
            client = builder.build();
            this.bot = TelegramBotAdapter.buildCustom(token, client);
        }
        if (dbUserService == null)
            dbUserService = new DefaultBotUserService();
        this.dbUserService = dbUserService;
        if (updateInterval == null || updateInterval <= 0)
            updateInterval = DEFAULT_INTERVAL;
        this.updateInterval = updateInterval;
        if (botUserName == null || botUserName.isEmpty())
            botUserName = "UNDEFINED";
        this.botUserName = botUserName;
        if (botName == null || botName.isEmpty())
            botName = BOT_NAME_DEFAULT;
        this.botName = botName;
    }

    public String getBotName() {
        return botName;
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    public String link() {
        return "https://telegram.me/" + botUserName;
    }

    public String link(String startParam) {
        return "https://telegram.me/" + botUserName + "?start=" + startParam;
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    getUpdates();
                    try {
                        Thread.sleep(updateInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
        for (int i = 0; i < NUMBER_OF_CUNCURRENT_MESSAGES; i++)
            executor.execute(new SendTask(i));
    }

    private void getUpdates() {
        try {
            GetUpdates getUpdatesRequest = new GetUpdates().limit(100).offset(lastUpdateId + 1).timeout(0);
            GetUpdatesResponse response = bot.execute(getUpdatesRequest);
            if (response.isOk()) {
                seqPlusPlus();
                if (response.updates().size() > 0) {
                    logger.info("[" + sequence + "|Updates] " + response.updates().size());
                    //System.out.println(CommonUtil.now() + );
                }
                for (Update update : response.updates()) {
                    try {
                        handleUpdate(update);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    lastUpdateId = update.updateId();
                }
            } else {
                //System.out.println(CommonUtil.now() + "Failure IN UPDATES|Telegram Error Code: " + response.errorCode());
                logger.error("Failure IN UPDATES|Telegram Error Code: " + response.errorCode());
                updateErors.add(response.errorCode() + response.description());
                updateFails++;
            }
        } catch (Exception ex) {
            logger.error("EXCEPTION IN UPDATES| " + ex.getClass().getName() + "|" + ex.getMessage());
            //System.out.println(CommonUtil.now() + "EXCEPTION IN UPDATES| " + ex.getClass().getName() + "|" + ex.getMessage());

            updateErors.add(ex.getClass().getName() + "|" + ex.getMessage());
            updateFails++;
        }
    }


    private void seqPlusPlus() {
        sequence++;
        if (sequence > 1000000)
            sequence = 0L;
    }

    User from(Update update) {
        return (update.message() != null ? update.message().from() :
                update.callbackQuery() != null ? update.callbackQuery().from() :
                        update.inlineQuery() != null ? update.inlineQuery().from() :
                                update.chosenInlineResult() != null ? update.chosenInlineResult().from() : null);
    }

    public void handleUpdate(Update update) {
        User from = from(update);
        if (from != null) {
            BotUserEntityInterface dbUser = dbUserService.updateOrCreate(from);
            Chain chain = new Chain(update, from, this);
            chain.setFromEntity(dbUser);
            boolean doContinue = true;
            for (Interceptor interceptor : interceptors) {
                try {
                    if (doContinue)
                        doContinue = interceptor.handle(chain);
                } catch (Exception e) {
                    if (e instanceof BadRequestException) {
                        chain.reply(e.getMessage());
                        doContinue = false;
                    } else {
                        e.printStackTrace();
                        doContinue = true;
                    }
                }
            }
            chain.removeNull();
            for (Sendable sendable : chain) {
                addSendable(sendable);
            }
            dbUserService.save(dbUser);
        } else if (update.channelPost() != null) {
            try {
                //System.out.println(CommonUtil.now() + "ChannelPost:" + update.channelPost().chat().id() + "|" + update.channelPost().chat().title());
                logger.info("ChannelPost:" + update.channelPost().chat().id() + "|" + update.channelPost().chat().title());
            } catch (Exception ignored) {
            }
        }
    }

    public String getBaseFilePath() {
        return "https://api.telegram.org/file/bot" + token + "/";
    }

    public void getFileAsString(String fileId, final StringHandler handler) {
        //System.out.println("log - fileID: " + fileId);
        Sendable getFile = new Sendable(new GetFile(fileId));
        getFile.callback(new GetFileCallback() {
            @Override
            public void onSuccess(GetFileResponse response) {

                String path = getBaseFilePath() + response.file().filePath();
                //System.out.println("log - filePath: " + path);
                try {
                    if (client != null) {
                        Response okResponse = getFileFromUrl(path);
                        String responseStr = okResponse.body().string().replace("\r\n", "\n").replace("\r", "\n");
                        List<String> lines = Arrays.asList(responseStr.split("\n"));
                        handler.handle(lines);
                    } else {
                        List<String> lines = Resources.readLines(new URL(path), Charsets.UTF_8);
                        if (lines.size() > 0 && lines.get(0).charAt(0) == 65533)
                            lines = Resources.readLines(new URL(path), Charsets.UTF_16);
                        if (lines.size() > 0 && lines.get(0).charAt(0) == 65279)
                            lines.set(0, lines.get(0).substring(1));
                        handler.handle(lines);
                    }
                } catch (Exception exc) {
                    System.out.println("File Got Exception:");
                    exc.printStackTrace();
                    handler.error(exc);
                }
            }
        });
        addSendable(getFile);
    }

    private Response getFileFromUrl(String path) throws IOException {
        Request request = new Request.Builder().url(path).build();
        return client.newCall(request).execute();
    }

    public void addSendable(Sendable sendable) {
        while (!sendQueue.offer(sendable)) ;
        sendable.increaseTryNumber();
    }

    public void saveFile(final String fileId, final String path) {
        Sendable getFile = new Sendable(new GetFile(fileId));
        getFile.callback(new GetFileCallback() {
            @Override
            public void onSuccess(GetFileResponse response) {
                String serverPath = getBaseFilePath() + response.file().filePath();
                try {
                    URL url = new URL(serverPath);
                    ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                    FileOutputStream fos = new FileOutputStream(basePath + path);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    fos.flush();
                    fos.close();
                } catch (Exception exc) {
                    //System.out.println(CommonUtil.now() + "Error On Saving File:" + fileId + " on path:" + path);
                    logger.error("Error On Saving File:" + fileId + " on path:" + path);
                    exc.printStackTrace();
                }
            }
        });
        addSendable(getFile);
    }

    public void downloadFileAsByteArray(final String fileId, final ByteArrayHandler handler) {
        Sendable getFile = new Sendable(new GetFile(fileId));
        getFile.callback(new GetFileCallback() {
            @Override
            public void onSuccess(GetFileResponse response) {
                String serverPath = getBaseFilePath() + response.file().filePath();
                try {
                    if (client != null) {
                        Response okResponse = getFileFromUrl(serverPath);
                        handler.handle(okResponse.body().bytes(), response.file().filePath());
                    } else {
                        URL url = new URL(serverPath);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        InputStream inputStream = null;
                        try {
                            inputStream = url.openStream();
                            byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
                            int n;

                            while ((n = inputStream.read(byteChunk)) > 0) {
                                outputStream.write(byteChunk, 0, n);
                            }
                        } catch (Exception exp) {
                            if (exp instanceof SocketException || exp instanceof SocketTimeoutException || exp instanceof UnknownHostException) {
                                //System.out.println(CommonUtil.now() + " Retry Downlowding File in 1s...");
                                logger.info("Retry Downlowding File in 1s...");
                                Thread.sleep(1000);
                                downloadFileAsByteArray(fileId, handler);
                            } else {
                                handler.error(exp);
                                exp.printStackTrace();
                            }
                        } finally {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                        }
                        handler.handle(outputStream.toByteArray(), response.file().filePath());
                    }
                } catch (Exception exc) {
                    handler.error(exc);
                }
            }

            @Override
            public void onError(BaseResponse response) {
                handler.error(new Exception("Network Failure"));
            }
        });
        addSendable(getFile);
    }

    public String getUsername() {
        return botUserName;
    }

    class SendTask implements Runnable {
        Integer number;

        public SendTask(Integer number) {
            this.number = number;
            //System.out.println(CommonUtil.now() + " T" + number + " started");
            logger.info("T" + number + " started");
        }

        @Override
        public void run() {
            while (true) {
                final Sendable sendable = sendQueue.poll();
                if (sendable != null) {
                    if (!fludFilter.fludCheck(sendable)) {
                        retrySendable(sendable, 3000, false);
                    } else {
                        try {
                            sendable.hashCode();
                            if (sendable.sendMessage != null) {
                                final String text = (String) sendable.sendMessage.getParameters().get("text");
                                if (text.length() > 4000) {
                                    sendable.sendMessage.getParameters().put("text", text.substring(0, 4000));
                                    sendable.callback(new Callback() {
                                        @Override
                                        public void onSuccess(BaseResponse baseResponse) {
                                            addSendable(new Sendable(new SendMessage(sendable.getChatId(), text.substring(4000))));
                                        }
                                    });
                                }
                            }
                            final BaseResponse response = bot.execute(sendable.getSendable());
                            if (response.isOk())
                                sendable.getFuture().success(response);
                            else {
                                sendMessageFails++;
                                setLogSendMessageError(response.errorCode() + response.description());
                                if (response.errorCode() == 429)
                                    retrySendable(sendable, randomTime(30000, 2000));
                                else {
                                    sendable.getFuture().fail();
                                }
                            }
                            logger.info("[T" + number + "|" + sendable.getChatId() + "]" + sendable.doneMessage() + (!response.isOk() ? ": " + response.errorCode() + "|" + response.description() : ""));
                        } catch (Exception ex) {
                            logger.error("Exception In SendMessage: " + ex.getClass().getName() + "|" + ex.getMessage());
                            sendMessageFails++;
                            setLogSendMessageError(ex.getClass().getName() + "|" + ex.getMessage());
                            if (ex.getCause() instanceof SocketTimeoutException
                                    || ex.getCause() instanceof NoRouteToHostException
                                    || ex.getCause() instanceof UnknownHostException)
                                retrySendable(sendable, randomTime(30000, 10000));
                            else
                                sendable.getFuture().fail();
                        }
                    }
                }
                try {
                    Thread.sleep(sendable == null ? 200 : 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private long randomTime(int base, int offset) {
        return base + (int) (Math.random() * 2 * offset) - offset;
    }

    private void setLogSendMessageError(String text) {
        Counter counter = sendMessageErrors.get(text);
        if (counter == null) {
            counter = new Counter();
            sendMessageErrors.put(text, counter);
        }
        counter.plusPlus();
    }

    private void retrySendable(final Sendable sendable, final long milli) {
        retrySendable(sendable, milli, true);
    }

    private void retrySendable(final Sendable sendable, final long milli, final boolean countRetry) {
        //System.out.println(CommonUtil.now() + " Retry after " + milli + " (" + sendable.getChatId() + "|" + sendable.doneMessage() + ")");
        if (sendable.getRetries() < retryCountLimit) {
            logger.warn("Retry after " + milli + " (" + sendable.getChatId() + "|" + sendable.doneMessage() + ") " + (!countRetry ? "[no count]" : ""));
            retries++;
            retryQueueSize++;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(milli);
                    } catch (InterruptedException ignored) {
                    }
                    if (!countRetry) {
                        sendable.decreasTryNumber();
                    }
                    addSendable(sendable);
                    retryQueueSize--;
                }
            }.start();
        } else {
            logger.warn("Retry Rejected after " + sendable.getRetries() + " times");
            sendable.future.fail();
        }
    }

    public String getStat() {
        StringBuilder builder = new StringBuilder();
        long[] allThreadIds = ManagementFactory.getThreadMXBean().getAllThreadIds();
        ThreadInfo[] threadInfos = ManagementFactory.getThreadMXBean().getThreadInfo(allThreadIds);
        builder.append("Queue size: " + sendQueue.size()).append("\n")
                .append("Last UpdateId: " + lastUpdateId).append("\n")
                .append("Threads: " + ManagementFactory.getThreadMXBean().getThreadCount()).append("\n")
                .append("Start Date: " + START_DATE + " (" + TimeUnit.MILLISECONDS.toHours((new Date().getTime()) - START_DATE.getTime()) + " Hours Ago)").append("\n")
                .append("Now: " + new Date()).append("\n")
                .append("Update Fails: " + updateFails).append("\n")
                .append("Send Fails: " + sendMessageFails).append("\n")
                .append("Retries: " + retries).append("\n")
                .append("RetryQ#: " + retryQueueSize).append("\n")
                .append("Last Sequence:" + sequence).append("\n").append("\n")
                .append("Update Errors:").append("\n");
        for (String updateEror : updateErors) {
            builder.append(updateEror).append("\n");
        }
        builder.append("\n").append("Send Errors:").append("\n");
        for (String error : sendMessageErrors.keySet()) {
            builder.append(sendMessageErrors.get(error).getValue()).append(": [").append(error).append("]\n");
        }
        return builder.toString();
    }
}
