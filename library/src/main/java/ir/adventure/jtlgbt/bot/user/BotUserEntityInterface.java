package ir.adventure.jtlgbt.bot.user;

/**
 * Created by jalil on 5/1/2018.
 */
public interface BotUserEntityInterface {
    Integer getBotState(String botName);

    String getType();

    void setBotState(String botName, Integer newState);

    Long getChatId();
}
