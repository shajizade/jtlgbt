package ir.adventure.jtlgbt.bot.interceptor;

import ir.adventure.jtlgbt.bot.Chain;

/**
 * Created by jalil on 8/21/2018.
 */
public abstract class InlineQueryInterceptor implements Interceptor {

    @Override
    public boolean handle(Chain chain) {
        if (chain.hasInlineQuery())
            return handleQuery(chain);
        if (chain.hasChosenQuery())
            return handleChosen(chain);
        return true;
    }

    protected boolean handleChosen(Chain chain) {
        return false;
    }

    protected abstract boolean handleQuery(Chain chain);

}
