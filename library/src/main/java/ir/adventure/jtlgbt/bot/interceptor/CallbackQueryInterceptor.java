package ir.adventure.jtlgbt.bot.interceptor;

import ir.adventure.jtlgbt.Util;
import ir.adventure.jtlgbt.bot.Chain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Saeed
 *         Date: 10/28/2017
 */
public class CallbackQueryInterceptor implements Interceptor {
    Map<String, CallbackQueryHandler> actionHandlers = new HashMap<>();

    public CallbackQueryInterceptor addAction(String actionName, CallbackQueryHandler handler) {
        actionHandlers.put(actionName, handler);
        return this;
    }

    @Override
    public boolean handle(Chain chain) {
        if (!chain.hasCallbackQuery())
            return true;
        chain.noReply();
        String data = chain.incomingCallbackQuery().data();
        CallbackQueryHandler callbackQueryHandler = actionHandlers.get(Util.callbackAction(data));
        if (callbackQueryHandler == null)
            return true;
        return callbackQueryHandler.handle(chain, Util.callbackParams(data));
    }
}
