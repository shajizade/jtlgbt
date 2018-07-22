package ir.saj.adventure;

import java.net.URL;

/**
 * Created by saeed on 11/2/17.
 */
public class Util {
    public static final String ACTION_SEPERATOR = "::";
    public static final String PARAM_SEPERATOR = ";;";

    public static boolean evaluateTelegramLink(String bannerLink) {
        String regex = "(https?:\\/\\/)?t\\.me\\/[a-zA-Z0-9_]+\\/[0-9]+";
        if (!bannerLink.matches(regex))
            return false;
        return true;
    }

    public static boolean isUsername(String text) {
        String regex = "@[0-9a-zA-Z_\\.]+";
        return text.matches(regex);
    }

    public static String callbackData(String actionName, Object... params) {
        StringBuilder builder = new StringBuilder(actionName);
        builder.append(ACTION_SEPERATOR);
        if (params != null)
            for (Object param : params) {
                builder.append(param.toString());
                builder.append(PARAM_SEPERATOR);
            }
        return builder.toString();
    }

    public static String callbackData(String actionName) {
        StringBuilder builder = new StringBuilder(actionName);
        builder.append(ACTION_SEPERATOR);
        return builder.toString();
    }

    public static final String callbackAction(String data) {
        String[] parts = data.split(ACTION_SEPERATOR);
        return parts[0];
    }

    public static final String[] callbackParams(String data) {
        String[] parts = data.split(ACTION_SEPERATOR);
        if (parts.length > 1)
            return parts[1].split(PARAM_SEPERATOR);
        return new String[0];
    }

    public static boolean isUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String safeTrim(String text, Integer len) {
        if (text.length() <= len)
            return text;
        return text.substring(0, len - 1);
    }

    public static boolean isJoinLink(String joinink) {
        String regex = "(https?:\\/\\/)?t\\.me\\/joinchat\\/[a-zA-Z0-9_\\-]+";
        if (!joinink.matches(regex))
            return false;
        return true;
    }
}
