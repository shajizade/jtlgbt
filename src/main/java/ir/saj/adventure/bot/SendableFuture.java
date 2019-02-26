package ir.saj.adventure.bot;


import com.pengrad.telegrambot.response.BaseResponse;
import ir.saj.adventure.bot.callback.Callback;

/**
 * @author Saeed
 *         Date: 1/23/2018
 */
public class SendableFuture {
    Sendable father;

    public SendableFuture(Sendable father) {
        this.father = father;
    }

    Boolean done = false;
    Boolean success = false;
    BaseResponse response;

    public Boolean isDone() {
        return done;
    }

    public SendableFuture success(final BaseResponse response) {
        this.response = response;
        success = response.isOk();
        done = true;
        if (father.callback() != null && father.callback().size() > 0) {
            new Thread() {
                @Override
                public void run() {
                    for (Callback callback : father.callback()) {
                        callback.handle(response);
                    }
                }
            }.start();
        }
        return this;
    }

    public BaseResponse getResponse() {
        return response;
    }

    public Boolean isSuccess() {
        return success;
    }

    public SendableFuture fail() {
        this.success = false;
        this.done = true;
        if (father.callback() != null && father.callback().size() > 0) {
            new Thread() {
                @Override
                public void run() {
                    for (Callback callback : father.callback()) {
                        callback.onError(null);
                    }

                }
            }.start();
        }
        return this;
    }

    public SendableFuture sync() {
        System.out.println("Sync Started");
        while (!isDone()) {
            try {
                Thread.sleep(100);
            } catch (Exception ignored) {
            }
        }
        System.out.println("Sync Finished");
        return this;
    }
}
