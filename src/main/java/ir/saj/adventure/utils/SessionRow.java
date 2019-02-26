package ir.saj.adventure.utils;

/**
 * Created by jalil on 12/29/2018.
 */
public class SessionRow {
    Object value;
    Boolean permanent;

    public SessionRow(Object value, Boolean permanent) {
        this.value = value;
        this.permanent = permanent;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Boolean getPermanent() {
        return permanent;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }
}
