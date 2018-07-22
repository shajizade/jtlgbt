package ir.saj.adventure.bot.callback;

import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;

/**
 * @author Saeed
 *         Date: 10/28/2017
 */
public abstract class GetFileCallback extends Callback {
    public GetFileCallback() {
        super(GetFileResponse.class);
    }

    @Override
    public void onSuccess(BaseResponse baseResponse) {
        onSuccess((GetFileResponse) baseResponse);
    }

    public abstract void onSuccess(GetFileResponse response);
}
