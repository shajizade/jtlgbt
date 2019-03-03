package ir.adventure.jtlgbtSample.sampleRunner;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.EditMessageText;
import ir.adventure.jtlgbt.Emoji;
import ir.adventure.jtlgbt.bot.*;
import ir.adventure.jtlgbt.bot.interceptor.state.StateHandler;
import ir.adventure.jtlgbt.bot.interceptor.state.StateInterceptor;

import java.net.Proxy;
import java.util.Objects;

/**
 * Created by jalil on 3/2/2019.
 */
public class WorkflowState {
    public void runSample(String token, Proxy proxy) {
        BotManager bot = new BotManager(token, proxy);
        StateInterceptor stateInterceptor = new StateInterceptor(bot.getBotName());
        stateInterceptor.state(0, new StateHandler() {
            @Override
            public Integer handle(Chain chain) {
                chain.setProperty("Name", chain.incomingText());
                //Go To WorkflowState 1
                return 1;
            }

            @Override
            public void preHandle(Chain chain) {
                chain.replyAppendLine("What is your name?");
            }
        }).state(1, new StateHandler() {
            @Override
            public Integer handle(Chain chain) {
                if (chain.hasIncomeText() && Objects.equals(chain.incomingText(), "Go Back"))
                    return 0;
                else if (chain.hasIncomeText() && Objects.equals(chain.incomingText(), "Go Ahead")) {
                    return 2;
                } else {
                    chain.reply("I didn't understand you");
                    return 1;
                }
            }

            @Override
            public void preHandle(Chain chain) {
                chain.replyAppendLine("Hello " + chain.getProperty("Name"));
                ReplyKeyboardBuilder replyKeyboardBuilder = new ReplyKeyboardBuilder();
                replyKeyboardBuilder.addButtonToLastRow("Go Back");
                replyKeyboardBuilder.addButtonToLastRow("Go Ahead");
                chain.reply(replyKeyboardBuilder.toMarkup());
            }
        }).state(2, new StateHandler() {
            @Override
            public Integer handle(Chain chain) {
                if (chain.hasCallbackQuery()) {
                    Message baseMessage = chain.incomingCallbackQuery().message();
                    EditMessageText editMessage = new EditMessageText(baseMessage.chat().id(), baseMessage.messageId(), "You " + chain.incomingCallbackQuery().data() + " it!" + "\n" + "Lets restart the game" + Emoji.clock);
                    chain.addSendable(new Sendable(editMessage));
                    AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery(chain.incomingCallbackQuery().id());
                    answerCallbackQuery.text("catch you!");
                    chain.addSendable(new Sendable(answerCallbackQuery));
                    return 0;
                } else {
                    chain.reply("I didn't understand you");
                    return 2;
                }
            }

            @Override
            public void preHandle(Chain chain) {
                chain.replyAppendLine("Hey " + chain.getProperty("Name") + "\n try our inline keyboard");
                InlineKeyboardBuilder inlineKeyboardBuilder = new InlineKeyboardBuilder();
                inlineKeyboardBuilder.addButtonToLastRow("I like it", "liked");
                inlineKeyboardBuilder.addButtonToLastRow("I hate it", "hated");
                inlineKeyboardBuilder.addButtonToNewRow("Go To Project Git", "https://github.com/shajizade/jtlgbt", true);
                chain.reply(inlineKeyboardBuilder.toMarkup());
            }
        });
        bot.addInterceptor(stateInterceptor);

        System.out.println("start");
        bot.start();
    }
}
