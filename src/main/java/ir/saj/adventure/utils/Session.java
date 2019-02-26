package ir.saj.adventure.utils;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Session {
    Map<Long, Map<String, SessionRow>> memory = new HashMap<>();

    public void setPermanent(Long chatId, String name, Object value) {
        set(chatId, name, value, false);
    }

    public void set(Long chatId, String name, Object value, Boolean permanent) {
        if (permanent == null)
            permanent = false;
        if (chatId == null)
            chatId = -1L;
        Map<String, SessionRow> userMemory = memory.get(chatId);
        if (userMemory == null) {
            userMemory = new HashMap<>();
            memory.put(chatId, userMemory);
        }
        userMemory.put(name, new SessionRow(value, permanent));
    }

    public Object get(Long chatId, String name) {
        if (chatId == null)
            chatId = -1L;
        Map<String, SessionRow> userMemory = memory.get(chatId);
        if (userMemory == null) {
            return null;
        }
        SessionRow sessionRow = userMemory.get(name);

        return sessionRow == null ? null : sessionRow.getValue();
    }

    public void delete(Long chatId, String name) {
        if (chatId == null)
            chatId = -1L;
        Map<String, SessionRow> userMemory = memory.get(chatId);
        if (userMemory == null) {
            return;
        }
        userMemory.remove(name);
    }

    public void clean(Long chatId) {
        if (chatId == null)
            chatId = -1L;
        Map<String, SessionRow> userMemory = memory.get(chatId);
        if (userMemory == null) {
            return;
        }
        Iterator<String> iterator = userMemory.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (!userMemory.get(key).getPermanent())
                iterator.remove();
        }
    }
}
