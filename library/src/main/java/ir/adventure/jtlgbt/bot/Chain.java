package ir.adventure.jtlgbt.bot;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import ir.adventure.jtlgbt.bot.user.BotUserEntityInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Saeed
 *         Date: 8/9/2017
 */
public class Chain implements Iterable<Sendable> {
    List<Sendable> sendables = new ArrayList<>();
    SendMessage replyMessage;
    EditMessageText editMessage;
    Update update;
    User from;
    BotUserEntityInterface fromEntity;
    BotManager bot;

    public User getFrom() {
        return from;
    }

    public BotUserEntityInterface getFromEntity() {
        return fromEntity;
    }

    public <Any> Any getCastedFrom() {
        return (Any) fromEntity;
    }

    public void setFromEntity(BotUserEntityInterface fromEntity) {
        this.fromEntity = fromEntity;
    }

    public Chain(Update update, User from, BotManager bot) {
        this.update = update;
        this.from = from;
        this.bot = bot;
        replyMessage = new SendMessage(incomingChatId(), null);
        editMessage = null;
    }

    public Chain edit(String text, InlineKeyboardMarkup markup) {
        editMessage = new EditMessageText(incomingChatId(), incomingCallbackQuery().message().messageId(), text);
        if (markup != null)
            editMessage.replyMarkup(markup);
        return this;
    }

    public Chain noReply() {
        replyMessage = null;
        return this;
    }

    public Chain reply(String text) {
        reply(text, null);
        return this;
    }

    public void replyForceReply() {
        replyMessage().replyMarkup(new ForceReply());
    }

    public void replyRemoveKeyboard() {
        replyMessage().replyMarkup(new ReplyKeyboardRemove());
    }

    public Chain replyAppendLine(String text) {
        replyAppend("\n" + text);
        return this;
    }

    public void replyAppend(String text) {
        if (replyText() == null)
            reply(text);
        else
            reply(replyText() + text);
    }

    public void replyInjectLine(String text) {
        replyInject(text + "\n");
    }

    public void replyInject(String text) {
        reply(text + replyText());
    }

    public void reply(ReplyKeyboardMarkup replyKeyboardMarkup) {
        reply(null, replyKeyboardMarkup);
    }

    public void reply(InlineKeyboardMarkup inlineKeyboardMarkup) {
        replyMessage.replyMarkup(inlineKeyboardMarkup);
    }

    public void reply(ParseMode parseMode) {
        if (replyMessage == null) {
            replyMessage = new SendMessage(incomingChatId(), null);
        }
        replyMessage.parseMode(parseMode);
    }

    public void reply(String text, ReplyKeyboardMarkup replyKeyboardMarkup) {
        if (replyMessage == null) {
            replyMessage = new SendMessage(incomingChatId(), null);
        }
        if (text != null)
            replyMessage.getParameters().put("text", text);
        if (replyKeyboardMarkup != null)
            replyMessage.replyMarkup(replyKeyboardMarkup);
    }

    public void addSendable(Sendable sendable) {
        sendables.add(sendable);
    }

    public Update incomingUpdate() {
        return update;
    }

    public CallbackQuery incomingCallbackQuery() {
        return update.callbackQuery();
    }

    public Long incomingChatId() {
        return update.message() != null ? update.message().chat().id() : from.id();
    }

    public String incomingText() {
        if (update.message() == null)
            return null;
        return update.message().text();
    }

    public String incomingTextOrCaption() {
        String text = update.message().text();
        String caption = update.message().caption();
        return (text == null ? "" : text) + (caption == null ? "" : caption);
    }

    public Message incomingMessage() {
        return update.message();
    }

    public PhotoSize incomingBiggestPhoto() {
        return (hasPhoto()) ? incomingMessage().photo()[incomingMessage().photo().length - 1] : null;
    }

    public SendMessage newReplyMessage(String text) {
        return new SendMessage(incomingChatId(), text);
    }

    public String replyText() {
        if (replyMessage() == null)
            return null;
        return (String) replyMessage.getParameters().get("text");
    }

    public SendMessage replyMessage() {
        return replyMessage;
    }

    @Override
    public Iterator<Sendable> iterator() {
        return new Iterator<Sendable>() {
            Integer state = 0;
            Integer index = 0;

            @Override
            public boolean hasNext() {
                if (state == 0 && sendables.size() > index)
                    return true;
                if (state == 0 && sendables.size() <= index) {
                    state = 1;
                    index = 0;
                }
                if (state == 1 && replyMessage != null)
                    return true;
                if (state == 1 && replyMessage == null)
                    state = 2;
                if (state == 2 && editMessage != null)
                    return true;
                return false;
            }

            @Override
            public Sendable next() {
                if (state == 0) {
                    index++;
                    return sendables.get(index - 1);
                }
                if (state == 1) {
                    state = 2;
                    return new Sendable(replyMessage);
                }
                if (state == 2) {
                    state = 3;
                    return new Sendable(editMessage);
                }
                return null;
            }

            @Override
            public void remove() {

            }
        };
    }

    public Boolean hasIncomeText() {
        return (incomingMessage() != null && incomingMessage().text() != null && !incomingMessage().text().isEmpty());
    }

    public Boolean hasContact() {
        return (incomingMessage() != null && incomingMessage().contact() != null);
    }

    public Contact incomingContact() {
        return hasContact() ? incomingMessage().contact() : null;
    }

    public boolean hasCallbackQuery() {
        return (update.callbackQuery() != null);
    }

    public boolean hasSticker() {
        return (update.message() != null && update.message().sticker() != null);
    }


    public boolean hasIncomeMessage() {
        return update.message() != null;
    }

    public boolean hasFile() {
        return (update.message() != null && update.message().document() != null);
    }

    public boolean hasAudio() {
        return (update.message() != null && update.message().audio() != null);
    }

    public boolean hasVoice() {
        return (update.message() != null && update.message().voice() != null);
    }

    public boolean hasPhoto() {
        return (update.message() != null && update.message().photo() != null && update.message().photo().length != 0);
    }

    public void removeNull() {
        if (replyMessage != null && replyText() == null)
            noReply();
    }

    public void answerCallback(String message) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery(incomingCallbackQuery().id());
        answerCallbackQuery.text(message);
        this.addSendable(new Sendable(answerCallbackQuery));
    }

    public AnswerCallbackQuery getAnswerCallback(String message) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery(incomingCallbackQuery().id());
        answerCallbackQuery.text(message);
        return answerCallbackQuery;
    }

    public BotManager getBot() {
        return bot;
    }

    public Document incomingFile() {
        if (!hasFile())
            return null;
        return update.message().document();
    }

    public boolean hasVideo() {
        return (update.message() != null && update.message().video() != null);
    }

    public boolean hasGif() {
        return (update.message() != null && update.message().document() != null && update.message().document().mimeType().equals("video/mp4"));
    }

    public Video incomingVideo() {
        return (hasVideo()) ? incomingMessage().video() : null;
    }

    public boolean hasDocument() {
        return (update.message() != null && update.message().document() != null);
    }

    public Document incomingDocument() {
        return (hasDocument()) ? incomingMessage().document() : null;
    }

    public InlineQuery incomingInlineQuery() {
        return update.inlineQuery();
    }

    public boolean hasInlineQuery() {
        return incomingInlineQuery() != null;
    }

    public boolean hasChosenQuery() {
        return incomingChosenQuery() != null;
    }

    private ChosenInlineResult incomingChosenQuery() {
        return update.chosenInlineResult();
    }

    public void setProperty(String name, Object value) {
        bot.getSession().set(incomingChatId(), name, value, false);
    }

    public void setPermanentProperty(String name, Object value) {
        bot.getSession().set(incomingChatId(), name, value, true);
    }

    public Object getProperty(String name) {
        return bot.getSession().get(incomingChatId(), name);
    }

    public void deleteProperty(String name) {
        bot.getSession().delete(incomingChatId(), name);
    }

    public void cleanProperties() {
        bot.getSession().clean(incomingChatId());
    }

    public Voice incomingVoice() {
        return (hasVoice()) ? incomingMessage().voice() : null;
    }

    public Audio incomingAudio() {
        return (hasAudio()) ? incomingMessage().audio() : null;
    }

    public Sticker incomingSticker() {
        return (hasSticker()) ? incomingMessage().sticker() : null;
    }
}
