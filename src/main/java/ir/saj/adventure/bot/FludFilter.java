package ir.saj.adventure.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jalil on 1/22/2019.
 */
public class FludFilter {
    private static final int FLUD_LIMIT = 5;
    private static final long FLUD_INTERVAL = 300;
    Map<Long, Integer> fludMap = new ConcurrentHashMap<>();
    final Logger logger = LoggerFactory.getLogger(FludFilter.class);

    public FludFilter() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    fludDecrease();

                    if (new Random().nextDouble() < 0.5) {
                        String filterState = FludFilter.this.toString().trim();
                        if (filterState != null && !filterState.isEmpty())
                            logger.debug("FilterState\n" + filterState);
                    }

                    try {
                        Thread.sleep(FLUD_INTERVAL);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }.start();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Long, Integer> longIntegerEntry : fludMap.entrySet()) {
            Integer value = longIntegerEntry.getValue();
            if (value > 0)
                builder.append(longIntegerEntry.getKey()).append(":").append(value).append("\n");
        }
        return builder.toString();
    }

    private void fludDecrease() {
        for (Map.Entry<Long, Integer> entry : fludMap.entrySet()) {
            Integer newVal = entry.getValue() - 2;
            if (newVal < 0)
                newVal = 0;
            if (entry.getValue() > 0 && newVal == 0)
                logger.debug("FludFilter, Omitting " + entry.getKey());
            entry.setValue(newVal);
        }
    }

    public synchronized boolean fludCheck(Sendable sendable) {
        String chatId = sendable.getChatId();
        Long chatIdL = null;
        try {
            chatIdL = Long.parseLong(chatId);
        } catch (Exception e) {
            return true;
        }
        if (chatIdL == null)
            return true;
        Integer count = fludMap.get(chatIdL);

        if (count == null) {
            count = 0;
        }
        if (count > FLUD_LIMIT)
            return false;
        fludMap.put(chatIdL, count + 1);
        return true;
    }

}
