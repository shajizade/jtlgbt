package ir.adventure.jtlgbt.bot;

import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import ir.adventure.jtlgbt.bot.callback.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Saeed
 *         Date: 8/9/2017
 */
public class Sendable {
    SendableFuture future = new SendableFuture(this);
    SendAudio sendAudio = null;
    SendVoice sendVoice = null;
    SendPhoto sendPhoto = null;
    SendMessage sendMessage = null;
    ForwardMessage forwardMessageMessage = null;
    DeleteMessage deleteMessage = null;
    EditMessageText editMessageText = null;
    EditMessageCaption editMessageCaption = null;
    AnswerCallbackQuery answerCallbackQuery = null;
    SendDocument sendDocument = null;
    SendSticker sendSticker = null;
    GetFile getFile = null;
    SendVideo sendVideo = null;
    AnswerInlineQuery answerInlineQuery = null;
    GetChatMember getChatMember = null;
    List<Callback> callbacks = new ArrayList<>();

    Integer retries = 0;


    public void increaseTryNumber() {
        retries++;
    }

    public void decreasTryNumber() {
        retries--;
    }

    public Integer getRetries() {
        return retries;
    }

    public Sendable(SendVideo sendVideo) {
        this.sendVideo = sendVideo;
    }

    public Sendable(AnswerInlineQuery answer) {
        this.answerInlineQuery = answer;
    }

    public Sendable(SendVoice sendVoice) {
        this.sendVoice = sendVoice;
    }

    public SendableFuture getFuture() {
        return future;
    }

    public void setFuture(SendableFuture future) {
        this.future = future;
    }

    public Sendable(SendSticker sendSticker) {
        this.sendSticker = sendSticker;
    }

    public Sendable(GetChatMember getChatMember) {
        this.getChatMember = getChatMember;
    }

    public Sendable(GetFile getFile) {
        this.getFile = getFile;
    }

    public Sendable(EditMessageCaption editMessageCaption) {
        this.editMessageCaption = editMessageCaption;
    }

    public Sendable(SendDocument sendDocument) {
        this.sendDocument = sendDocument;
    }

    public Sendable(SendAudio sendAudio) {
        this.sendAudio = sendAudio;
    }

    public List<Callback> callback() {
        return callbacks;
    }

    public Sendable callback(Callback callback) {
        this.callbacks.add(callback);
        return this;
    }

    public Sendable(DeleteMessage deleteMessage) {
        this.deleteMessage = deleteMessage;
    }

    public Sendable(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
    }

    public Sendable(ForwardMessage forwardMessageMessage) {
        this.forwardMessageMessage = forwardMessageMessage;
    }

    public Sendable(EditMessageText editMessage) {
        this.editMessageText = editMessage;
    }

    public Sendable(AnswerCallbackQuery answerCallbackQuery) {
        this.answerCallbackQuery = answerCallbackQuery;
    }

    public Sendable(SendPhoto sendPhoto) {
        this.sendPhoto = sendPhoto;
    }

    public BaseRequest<? extends BaseRequest<?, ?>, ? extends BaseResponse> getSendable() {
        if (sendMessage != null)
            return sendMessage;
        if (forwardMessageMessage != null)
            return forwardMessageMessage;
        if (deleteMessage != null)
            return deleteMessage;
        if (editMessageText != null)
            return editMessageText;
        if (answerCallbackQuery != null)
            return answerCallbackQuery;
        if (sendPhoto != null)
            return sendPhoto;
        if (sendSticker != null)
            return sendSticker;
        if (editMessageCaption != null)
            return editMessageCaption;
        if (sendDocument != null)
            return sendDocument;
        if (sendAudio != null)
            return sendAudio;
        if (getFile != null)
            return getFile;
        if (sendVideo != null)
            return sendVideo;
        if (answerInlineQuery != null)
            return answerInlineQuery;
        if (sendVoice != null)
            return sendVoice;
        if (getChatMember != null)
            return getChatMember;
        return null;
    }

    public String doneMessage() {
        if (sendMessage != null)
            return "sent";
        if (forwardMessageMessage != null)
            return "forwarded";
        if (deleteMessage != null)
            return "deleted";
        if (editMessageText != null)
            return "edited";
        if (answerCallbackQuery != null)
            return "cbAnswered";
        if (sendPhoto != null)
            return "photoSent";
        if (sendSticker != null)
            return "stickerSent";
        if (editMessageCaption != null)
            return "captionEdited";
        if (sendDocument != null)
            return "docSent";
        if (sendAudio != null)
            return "audioSent";
        if (getFile != null)
            return "fileGot";
        if (sendVideo != null)
            return "videoSent";
        if (answerInlineQuery != null)
            return "inlnAnswer";
        if (sendVoice != null)
            return "voiceSent";
        if (getChatMember != null)
            return "gotChatMember";
        return "done";
    }

    public String getChatId() {
        Map<String, Object> parameters = getSendable().getParameters();
        if (parameters != null && parameters.get("chat_id") != null)
            return parameters.get("chat_id").toString();
        return "--";
    }
}
