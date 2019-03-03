package ir.adventure.jtlgbt.bot;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Saeed
 *         Date: 8/12/2017
 */
public class ReplyKeyboardBuilder {
    public static final String PHONE_TAG = "|PHONE|";
    List<List<String>> array = new ArrayList<>();

    public ReplyKeyboardBuilder addButtonToLastRow(String text) {
        addButtonToLastRow(text, false);
        return this;
    }

    public ReplyKeyboardBuilder addButtonToLastRow(String text, Boolean phone) {
        if (array.size() == 0)
            array.add(new ArrayList<String>());
        List<String> lastRow = array.get(array.size() - 1);
        if (phone)
            text = PHONE_TAG + text;
        lastRow.add(text);
        return this;
    }

    public ReplyKeyboardBuilder addButtonToNewRow(String text) {
        ArrayList<String> newRow = new ArrayList<>();
        array.add(newRow);
        newRow.add(text);
        return this;
    }

    public ReplyKeyboardMarkup toMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(toButtonArray());
        replyKeyboardMarkup.oneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public KeyboardButton[][] toButtonArray() {
        KeyboardButton[][] result = new KeyboardButton[array.size()][];
        int index = 0;
        for (KeyboardButton[] resultRow : result) {
            resultRow = new KeyboardButton[array.get(index).size()];
            List<String> rowArray = array.get(index);
            int innerIndex = 0;
            for (String s : rowArray) {
                if (!s.startsWith(PHONE_TAG))
                    resultRow[innerIndex] = new KeyboardButton(s);
                else {
                    resultRow[innerIndex] = new KeyboardButton(s.substring(PHONE_TAG.length())).requestContact(true);
                }
                innerIndex++;
            }
            //resultRow=array.get(index).toArray(resultRow);
            result[index] = resultRow;
            index++;
        }
        return result;
    }

    public String[][] toArray() {
        String[][] result = new String[array.size()][];
        int index = 0;
        for (String[] resultRow : result) {
            resultRow = new String[array.get(index).size()];
            resultRow = array.get(index).toArray(resultRow);
            result[index] = resultRow;
            index++;
        }
        return result;
    }
}
