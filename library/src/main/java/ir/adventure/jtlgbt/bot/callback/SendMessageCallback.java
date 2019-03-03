package ir.adventure.jtlgbt.bot.callback;

import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;

/**
 * @author Saeed
 *         Date: 10/28/2017
 */
public abstract class SendMessageCallback extends Callback {
    public SendMessageCallback() {
        super(SendResponse.class);
    }

    @Override
    public void onSuccess(BaseResponse baseResponse) {
        onSuccess((SendResponse) baseResponse);
    }

    public abstract void onSuccess(SendResponse response);
}
