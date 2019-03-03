package ir.adventure.jtlgbt.bot.user;

import com.pengrad.telegrambot.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jalil on 3/2/2019.
 */
public class DefaultBotUserImple implements BotUserEntityInterface {

    String type;
    Map<String, Integer> botState = new ConcurrentHashMap<>();
    User user;

    public DefaultBotUserImple(User user) {
        this.user = user;
    }

    @Override
    public Integer getBotState(String botName) {
        return botState.get(botName);
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setBotState(String botName, Integer newState) {
        botState.put(botName, newState);
    }

    @Override
    public Long getChatId() {
        return user.id().longValue();
    }

}
