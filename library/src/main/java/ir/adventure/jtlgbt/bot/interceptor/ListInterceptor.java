package ir.adventure.jtlgbt.bot.interceptor;

import ir.adventure.jtlgbt.bot.Chain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Saeed
 *         Date: 10/29/2017
 */
public class ListInterceptor implements Interceptor {
    List<Interceptor> interceptors = new ArrayList<>();

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    @Override
    public boolean handle(Chain chain) {
        boolean doContinue = true;
        for (Interceptor interceptor : interceptors) {
            try {
                if (doContinue)
                    doContinue = interceptor.handle(chain);
            } catch (Exception e) {
                e.printStackTrace();
                doContinue = true;
            }
        }
        return doContinue;
    }
}
