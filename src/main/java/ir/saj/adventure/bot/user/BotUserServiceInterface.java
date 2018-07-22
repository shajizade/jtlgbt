package ir.saj.adventure.bot.user;

import com.pengrad.telegrambot.model.User;

/**
 * Created by jalil on 5/1/2018.
 */
public interface BotUserServiceInterface<T extends BotUserEntityInterface> {
    T updateOrCreate(User user);

    T save(T user);
}
