package ir.adventure.jtlgbt.bot.interceptor;

import ir.adventure.jtlgbt.Util;
import ir.adventure.jtlgbt.bot.Chain;

/**
 * @author Saeed
 *         Date: 11/1/2017
 */
public abstract class StartInterceptor implements Interceptor {
    @Override
    public boolean handle(Chain chain) {
        if (chain.hasIncomeText() && chain.incomingText().startsWith("/start")) {
            String data = chain.incomingText().substring(6);
            return startHandle(chain, Util.callbackAction(data), Util.callbackParams(data));
        }
        return true;
    }

    public abstract boolean startHandle(Chain chain, String action, String[] params);
}
