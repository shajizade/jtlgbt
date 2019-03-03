package ir.adventure.jtlgbtSample.sampleRunner;

import ir.adventure.jtlgbt.bot.BotManager;
import ir.adventure.jtlgbt.bot.Chain;
import ir.adventure.jtlgbt.bot.interceptor.Interceptor;

import java.net.Proxy;

/**
 * Created by jalil on 3/2/2019.
 */
public class Echo {
    public void runSample(String token, Proxy proxy) {
        BotManager bot = new BotManager(token, proxy);

        bot.addInterceptor(new Interceptor() {
            @Override
            public boolean handle(Chain chain) {
                if (chain.hasIncomeText()) {
                    chain.reply(chain.incomingText());
                    //No need to continue if it was a text message
                    return false;
                }
                //Continue, maybe an appropriate interceptor is waiting for this kind of message
                return true;
            }
        });
        System.out.println("start");
        bot.start();
    }
}
