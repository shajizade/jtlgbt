package ir.adventure.jtlgbt.bot.interceptor.state;

import ir.adventure.jtlgbt.bot.Chain;

/**
 * Created by jalil on 5/19/2018.
 */
public class JumperStateHandler extends StateHandler {
    final Integer stateToJump;

    public JumperStateHandler(Integer stateToJump) {
        this.stateToJump = stateToJump;
    }

    @Override
    public Integer handle(Chain chain) {
        return stateToJump;
    }

    @Override
    public void preHandle(Chain chain) {
    }
}
