package ir.adventure.jtlgbt.bot.handlers;

/**
 * @author Saeed
 *         Date: 12/9/2017
 */
public interface ByteArrayHandler {
    void handle(byte[] bytes, String filePath);

    void error(Exception exp);
}
