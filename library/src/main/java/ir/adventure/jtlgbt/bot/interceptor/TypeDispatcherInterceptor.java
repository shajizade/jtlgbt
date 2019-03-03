package ir.adventure.jtlgbt.bot.interceptor;

import ir.adventure.jtlgbt.bot.Chain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Saeed
 *         Date: 10/28/2017
 */
public class TypeDispatcherInterceptor implements Interceptor {
    Map<String, Interceptor> interceptorMap = new HashMap<>();

    public TypeDispatcherInterceptor addType(String type, Interceptor interceptor) {
        interceptorMap.put(type, interceptor);
        return this;
    }

    @Override
    public boolean handle(Chain chain) {
        String type = chain.getFromEntity().getType();
        if (type == null)
            return false;
        Interceptor interceptor = interceptorMap.get(type);
        if (interceptor == null)
            return false;
        return interceptor.handle(chain);
    }
}
