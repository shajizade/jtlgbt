package ir.saj.adventure.bot.callback;

import com.pengrad.telegrambot.response.BaseResponse;

/**
 * @author Saeed
 *         Date: 10/28/2017
 */
public abstract class Callback {
    Class expectedResponse = null;

    public Callback(Class expectedResponse) {
        this.expectedResponse = expectedResponse;
    }

    public Callback() {
        this.expectedResponse = BaseResponse.class;
    }

    public void handle(BaseResponse baseResponse) {
        if (baseResponse.isOk()) {
            if (expectedResponse == null || expectedResponse.isInstance(baseResponse))
                onSuccess(baseResponse);
        } else
            onError(baseResponse);
    }

    public abstract void onSuccess(BaseResponse baseResponse);

    public void onError(BaseResponse response) {
        if (response != null)
            System.out.println("[ResponseError]" + response.errorCode() + " : " + response.description());
        else
            System.out.println("[ResponseError]sending request to telegram was not successfull");
    }
}
