/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.command.friends;

import com.google.common.eventbus.Subscribe;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private static final List<Object> listeners = new ArrayList<Object>();

    public static void register(Object listener) {
        listeners.add(listener);
    }

    public static void unregister(Object listener) {
        listeners.remove(listener);
    }

    public static void post(Object event) {
        for (Object listener : listeners) {
            for (Method method : listener.getClass().getDeclaredMethods()) {
                if (!method.isAnnotationPresent(Subscribe.class) || method.getParameterTypes().length != 1 || !method.getParameterTypes()[0].isAssignableFrom(event.getClass())) continue;
                try {
                    method.invoke(listener, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

