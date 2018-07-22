package ir.saj.adventure.bot.interceptor.state;

import ir.saj.adventure.bot.Chain;
import ir.saj.adventure.bot.interceptor.Interceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Saeed
 *         Date: 8/22/2017
 */
public class StateInterceptor implements Interceptor {
    Map<Integer, StateHandler> handlers = new HashMap<>();
    private String botName;
    public StateInterceptor(String botName) {
        this.botName = botName;
    }

    @Override
    public boolean handle(Chain chain) {
        Integer botState = chain.getFromEntity().getBotState(botName);
        if (botState == null)
            botState = 0;
        StateHandler stateHandler = handlers.get(botState);
        if (stateHandler == null)
            return true;
        Integer newState = stateHandler.handle(chain);
        Boolean finished = false;

        while (!finished) {
            StateHandler newStateHandler = handlers.get(newState);
            if (newStateHandler == null)
                return false;
            Integer redirectResult = newStateHandler.redirectCheck(chain);
            if (redirectResult == null) {
                newStateHandler.preHandle(chain);
                chain.getFromEntity().setBotState(botName, newState);
                finished = true;
            } else {
                newState = redirectResult;
                chain.getFromEntity().setBotState(botName, newState);
            }
        }
        return false;
    }

    public StateInterceptor state(Integer stateNumber, StateHandler stateHandler) {
        handlers.put(stateNumber, stateHandler);
        return this;
    }


}
