package ir.saj.adventure.bot.interceptor;

import ir.saj.adventure.bot.Chain;

/**
 * @author Saeed
 *         Date: 10/28/2017
 */
public interface CallbackQueryHandler {
    boolean handle(Chain chain, String[] params);
}
