package ir.adventure.jtlgbt.bot.user;

import com.pengrad.telegrambot.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jalil on 3/2/2019.
 */
public class DefaultBotUserService implements BotUserServiceInterface {
    private static final String DEFAULT_TYPE = "USER";
    Map<Long, DefaultBotUserImple> userMap = new ConcurrentHashMap<>();

    @Override
    public BotUserEntityInterface updateOrCreate(User user) {
        DefaultBotUserImple userImple = userMap.get(user.id().longValue());
        if (userImple == null) {
            userImple = new DefaultBotUserImple(user);
            userImple.setType(DEFAULT_TYPE);
            userMap.put(user.id().longValue(), userImple);
        }
        userImple.setUser(user);
        return userImple;
    }

    @Override
    public BotUserEntityInterface save(BotUserEntityInterface userImpl) {
        DefaultBotUserImple inputUser = (DefaultBotUserImple) userImpl;
        userMap.put(userImpl.getChatId(), inputUser);
        return inputUser;
    }
}
