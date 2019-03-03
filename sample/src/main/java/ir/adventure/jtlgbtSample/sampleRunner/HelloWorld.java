package ir.adventure.jtlgbtSample.sampleRunner;

import ir.adventure.jtlgbt.bot.BotManager;
import ir.adventure.jtlgbt.bot.Chain;
import ir.adventure.jtlgbt.bot.interceptor.Interceptor;

import java.net.Proxy;

/**
 * Created by jalil on 3/2/2019.
 */
public class HelloWorld {
    public void runSample(String token, Proxy proxy) {
        BotManager bot = new BotManager(token, proxy);

        bot.addInterceptor(new Interceptor() {
            @Override
            public boolean handle(Chain chain) {
                chain.reply("Hello World!");
                return false;
            }
        });
        System.out.println("start");
        bot.start();
    }
}
