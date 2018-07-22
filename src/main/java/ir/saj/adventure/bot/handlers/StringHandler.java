package ir.saj.adventure.bot.handlers;

import java.util.List;

/**
 * @author Saeed
 *         Date: 12/9/2017
 */
public interface StringHandler {
    void handle(List<String> lines);

    void error(Exception exp);
}
