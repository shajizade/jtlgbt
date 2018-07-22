package ir.saj.adventure.bot.user;

import com.pengrad.telegrambot.model.User;

/**
 * Created by jalil on 5/1/2018.
 */
public interface BotUserEntityInterface {
    Integer getBotState(String botName);

    String getType();

    void setBotState(String botName, Integer newState);

    Long getChatId();
}
