package ir.adventure.jtlgbt.bot;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Saeed
 *         Date: 8/12/2017
 */
public class InlineKeyboardBuilder {
    List<List<InlineKeyboardButton>> array = new ArrayList<>();

    public InlineKeyboardBuilder addButtonToLastRow(String text, String data) {
        addButtonToLastRow(text, data, false);
        return this;
    }

    public InlineKeyboardBuilder addButtonToLastRow(String text, String data, Boolean isUrl) {
        InlineKeyboardButton inlineButton = createInlineButton(text, data, isUrl);
        if (array.size() == 0)
            array.add(new ArrayList<InlineKeyboardButton>());
        List<InlineKeyboardButton> lastRow = array.get(array.size() - 1);
        lastRow.add(inlineButton);
        return this;
    }

    public InlineKeyboardBuilder addButtonToNewRow(String text, String data) {
        return addButtonToNewRow(text, data, false);
    }

    public InlineKeyboardBuilder addButtonToNewRow(String text, String data, Boolean isUrl) {
        InlineKeyboardButton inlineButton = createInlineButton(text, data, isUrl);
        ArrayList<InlineKeyboardButton> newRow = new ArrayList<>();
        array.add(newRow);
        newRow.add(inlineButton);
        return this;
    }

    private InlineKeyboardButton createInlineButton(String text, String data, Boolean isUrl) {
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        if (isUrl)
            button.url(data);
        else
            button.callbackData(data);
        return button;
    }

    public InlineKeyboardMarkup toMarkup() {
        return new InlineKeyboardMarkup(toArray());
    }

    public InlineKeyboardButton[][] toArray() {
        InlineKeyboardButton[][] result = new InlineKeyboardButton[array.size()][];
        int index = 0;
        for (InlineKeyboardButton[] resultRow : result) {
            resultRow = new InlineKeyboardButton[array.get(index).size()];
            resultRow = array.get(index).toArray(resultRow);
            result[index] = resultRow;
            index++;
        }
        return result;
    }

}
