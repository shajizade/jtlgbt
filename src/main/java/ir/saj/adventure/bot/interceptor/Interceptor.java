package ir.saj.adventure.bot.interceptor;

import ir.saj.adventure.bot.Chain;

/**
 * @author Saeed
 *         Date: 8/22/2017
 */
public interface Interceptor {
    public boolean handle(Chain chain);
}
