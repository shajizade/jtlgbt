package ir.adventure.jtlgbt.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jalil on 8/4/2018.
 */
public class TextFile {

    public static final String NEW_LINE = "\r\n";
    public static final String SEPARATOR = "\t";
    private final File file;
    private final OutputStreamWriter outputStreamWriter;
    private final FileOutputStream fop;
    private String nullSign = "-";
    private boolean lineStarted = true;

    public TextFile(String name) throws IOException {
        this(name, false);
    }

    public File getFile() {
        return file;
    }

    public TextFile(String name, boolean dateInName) throws IOException {
        if (dateInName) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            name = name + "-" + formatter.format(new Date()) + ".txt";
        }
        if (!name.endsWith(".txt")) {
            name = name + ".txt";
        }
        file = new File(name);
        fop = new FileOutputStream(file);
        outputStreamWriter = new OutputStreamWriter(fop, StandardCharsets.UTF_8);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public void write(Object toWrite) throws IOException {

        String str;
        if (toWrite instanceof byte[])
            str = new String((byte[]) toWrite, Charset.forName("utf-8"));
        else
            str = (toWrite == null ? "-" : toWrite.toString());

        outputStreamWriter.write(str);
        lineStarted = false;
    }

    public void writeCol(Object toWrite) throws IOException {
        if (!lineStarted)
            write(SEPARATOR);
        write(toWrite);
    }

    public void writeLine(Object toWrite) throws IOException {
        outputStreamWriter.write((toWrite == null ? nullSign : toWrite.toString()) + NEW_LINE);
        lineStarted = true;
    }

    public void newLine() throws IOException {
        outputStreamWriter.write(NEW_LINE);
        lineStarted = true;
    }

    public void save() throws IOException {
        outputStreamWriter.flush();
        outputStreamWriter.close();
        fop.flush();
        fop.close();
    }
}
