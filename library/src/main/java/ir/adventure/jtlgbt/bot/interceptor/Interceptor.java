package ir.adventure.jtlgbt.bot.interceptor;

import ir.adventure.jtlgbt.bot.Chain;

/**
 * @author Saeed
 *         Date: 8/22/2017
 */
public interface Interceptor {
    public boolean handle(Chain chain);
}
