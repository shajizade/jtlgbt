package ir.adventure.jtlgbt.bot.interceptor;

import ir.adventure.jtlgbt.bot.Chain;

/**
 * @author Saeed
 *         Date: 10/28/2017
 */
public interface CallbackQueryHandler {
    boolean handle(Chain chain, String[] params);
}
