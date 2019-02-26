package ir.saj.adventure.bot;

import ir.saj.adventure.Util;

/**
 * Created by jalil on 8/14/2018.
 */
public class PageableInlineKeyboardBuilder {
    private String GOTOPAGE_TOKEN = "GOTOPAGE";
    InlineKeyboardBuilder builder = new InlineKeyboardBuilder();
    Integer columnNumber;
    Integer currentPageNumber;
    Boolean hasNexPage;
    Boolean hasPreviousPage;
    Integer index = 0;

    public PageableInlineKeyboardBuilder(Integer columnNumber, Integer currentPageNumber, Boolean hasNexPage, Boolean hasPreviousPage) {
        this.columnNumber = columnNumber;
        this.currentPageNumber = currentPageNumber;
        this.hasNexPage = hasNexPage;
        this.hasPreviousPage = hasPreviousPage;
    }

    public PageableInlineKeyboardBuilder addPagingRow() {
        if (hasPreviousPage) {
            builder.addButtonToNewRow(Integer.toString(currentPageNumber - 1) + " <<", Util.callbackData(GOTOPAGE_TOKEN, currentPageNumber - 1));
            builder.addButtonToLastRow(Integer.toString(currentPageNumber), Util.callbackData(GOTOPAGE_TOKEN, currentPageNumber));
        } else {
            builder.addButtonToNewRow(Integer.toString(currentPageNumber), Util.callbackData(GOTOPAGE_TOKEN, currentPageNumber));
        }

        if (hasNexPage)
            builder.addButtonToLastRow(">> " + Integer.toString(currentPageNumber + 1), Util.callbackData(GOTOPAGE_TOKEN, currentPageNumber + 1));
        return this;
    }

    public PageableInlineKeyboardBuilder addButton(String text, String data) {
        addButton(text, data, false);
        return this;
    }

    public PageableInlineKeyboardBuilder addButton(String text, String data, Boolean isUrl) {
        if (index % columnNumber == 0)
            builder.addButtonToNewRow(text, data, isUrl);
        else
            builder.addButtonToLastRow(text, data, isUrl);
        index++;
        return this;
    }

    public InlineKeyboardBuilder getBuilder() {
        return builder;
    }

    public PageableInlineKeyboardBuilder setBuilder(InlineKeyboardBuilder builder) {
        this.builder = builder;
        return this;
    }

    public PageableInlineKeyboardBuilder setGOTOPAGE_TOKEN(String GOTOPAGE_TOKEN) {
        this.GOTOPAGE_TOKEN = GOTOPAGE_TOKEN;
        return this;
    }
}
