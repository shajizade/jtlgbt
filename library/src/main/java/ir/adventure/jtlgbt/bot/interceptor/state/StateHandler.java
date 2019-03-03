package ir.adventure.jtlgbt.bot.interceptor.state;

import ir.adventure.jtlgbt.bot.Chain;

/**
 * @author Saeed
 *         Date: 8/8/2017
 */
public abstract class StateHandler {

    public abstract Integer handle(Chain chain);

    public abstract void preHandle(Chain chain);

    public Integer redirectCheck(Chain chain) {
        return null;
    }
}
