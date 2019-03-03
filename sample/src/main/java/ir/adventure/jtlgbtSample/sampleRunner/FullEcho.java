package ir.adventure.jtlgbtSample.sampleRunner;

import com.pengrad.telegrambot.request.*;
import ir.adventure.jtlgbt.bot.BotManager;
import ir.adventure.jtlgbt.bot.Chain;
import ir.adventure.jtlgbt.bot.Sendable;
import ir.adventure.jtlgbt.bot.interceptor.Interceptor;

import java.net.Proxy;

/**
 * Created by jalil on 3/2/2019.
 */
public class FullEcho {
    public void runSample(String token, Proxy proxy) {
        BotManager bot = new BotManager(token, proxy);

        //I know it could be done with just one interceptor, but did write like this for educational purposes :)

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
        }).addInterceptor(new Interceptor() {
            @Override
            public boolean handle(Chain chain) {
                if (chain.hasPhoto()) {
                    SendPhoto sendPhoto = new SendPhoto(chain.incomingChatId(), chain.incomingBiggestPhoto().fileId());
                    sendPhoto.caption(chain.incomingTextOrCaption());
                    chain.addSendable(new Sendable(sendPhoto));
                    //No need to continue if it was a text message
                    return false;
                }
                //Continue, maybe an appropriate interceptor is waiting for this kind of message
                return true;
            }
        }).addInterceptor(new Interceptor() {
            @Override
            public boolean handle(Chain chain) {
                if (chain.hasVideo()) {
                    SendVideo sendVideo = new SendVideo(chain.incomingChatId(), chain.incomingVideo().fileId());
                    chain.addSendable(new Sendable(sendVideo));
                    sendVideo.caption(chain.incomingTextOrCaption());
                    //No need to continue if it was a text message
                    return false;
                }
                //Continue, maybe an appropriate interceptor is waiting for this kind of message
                return true;
            }
        }).addInterceptor(new Interceptor() {
            @Override
            public boolean handle(Chain chain) {
                if (chain.hasFile()) {
                    SendDocument sendDoc = new SendDocument(chain.incomingChatId(), chain.incomingFile().fileId());
                    chain.addSendable(new Sendable(sendDoc));
                    sendDoc.caption(chain.incomingTextOrCaption());
                    //No need to continue if it was a text message
                    return false;
                }
                //Continue, maybe an appropriate interceptor is waiting for this kind of message
                return true;
            }
        }).addInterceptor(new Interceptor() {
            @Override
            public boolean handle(Chain chain) {
                if (chain.hasVoice()) {
                    SendVoice sendVoice = new SendVoice(chain.incomingChatId(), chain.incomingVoice().fileId());
                    chain.addSendable(new Sendable(sendVoice));
                    sendVoice.caption(chain.incomingTextOrCaption());
                    //No need to continue if it was a text message
                    return false;
                }
                //Continue, maybe an appropriate interceptor is waiting for this kind of message
                return true;
            }
        }).addInterceptor(new Interceptor() {
            @Override
            public boolean handle(Chain chain) {
                if (chain.hasAudio()) {
                    SendAudio sendAudio = new SendAudio(chain.incomingChatId(), chain.incomingAudio().fileId());
                    chain.addSendable(new Sendable(sendAudio));
                    sendAudio.caption(chain.incomingTextOrCaption());
                    //No need to continue if it was a text message
                    return false;
                }
                //Continue, maybe an appropriate interceptor is waiting for this kind of message
                return true;
            }
        }).addInterceptor(new Interceptor() {
            @Override
            public boolean handle(Chain chain) {
                if (chain.hasSticker()) {
                    SendSticker sendSticker = new SendSticker(chain.incomingChatId(), chain.incomingSticker().fileId());
                    chain.addSendable(new Sendable(sendSticker));
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
