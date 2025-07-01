package org.example.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

public class UIManagers {
    private static final Font GLOBAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public static void applyGlobalFont() {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof Font) {
                UIManager.put(key, GLOBAL_FONT);
            }
        }
    }

    public static Font getGlobalFont() {
        return GLOBAL_FONT;
    }
}
