package utils;

import javax.swing.*;
import java.awt.*;

public class ThemeUtils {

    public static void applyDarkTheme(Component component) {
        if (component instanceof JPanel || component instanceof JFrame) {
            component.setBackground(Color.BLACK);
        } else if (component instanceof JButton) {
            component.setBackground(Color.DARK_GRAY);
            component.setForeground(Color.BLACK);
        } else if (component instanceof JTextField || component instanceof JTextArea) {
            component.setBackground(Color.BLACK);
            component.setForeground(Color.WHITE);
        }
        for (Component child : ((Container) component).getComponents()) {
            applyDarkTheme(child);
        }
    }

    public static void applyLightTheme(Component component) {
        if (component instanceof JPanel || component instanceof JFrame) {
            component.setBackground(Color.WHITE);
        } else if (component instanceof JButton) {
            component.setBackground(Color.LIGHT_GRAY);
            component.setForeground(Color.BLACK);
        } else if (component instanceof JTextField || component instanceof JTextArea) {
            component.setBackground(Color.WHITE);
            component.setForeground(Color.BLACK);
        }
        for (Component child : ((Container) component).getComponents()) {
            applyLightTheme(child);
        }
    }
}
