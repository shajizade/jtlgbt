package ir.adventure.jtlgbtSample.sampleRunner;

import ir.adventure.jtlgbt.bot.BotManager;
import ir.adventure.jtlgbt.bot.Chain;
import ir.adventure.jtlgbt.bot.interceptor.Interceptor;

import java.net.Proxy;

/**
 * Created by jalil on 3/2/2019.
 */
public class IAmWatching {
    public void runSample(String token, Proxy proxy) {
        BotManager bot = new BotManager(token, proxy);

        System.out.println("start");
        bot.addInterceptor(new Interceptor() {

            //This function will be called on each update
            @Override
            public boolean handle(Chain chain) {
                System.out.println("I got an update");

                //False means do not continue to other interceptors
                return false;
            }
        });
        bot.start();
    }
}
