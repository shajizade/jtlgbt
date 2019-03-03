package ir.adventure.jtlgbt.bot;

/**
 * Created by jalil on 4/12/2018.
 */
public class Counter {
    long counter = 0;

    public void plusPlus() {
        counter++;
    }

    public long getValue() {
        return counter;
    }

    public void minusMinus() {
        counter--;
    }
}
