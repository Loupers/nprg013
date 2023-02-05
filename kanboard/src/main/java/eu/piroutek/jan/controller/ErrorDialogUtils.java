package eu.piroutek.jan.controller;

import javax.swing.*;

/**
 * Utils for displaying error dialogs
 */
public class ErrorDialogUtils {
    /**
     * show dialog, that is not fatal
     * @param text to be displayed
     * @param parent frame in which to show dialog
     */
    public static void showRecoverableErrorDialog(String text, JPanel parent) {
        JOptionPane.showMessageDialog(parent, text, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * show dialog with fatal error
     *
     * @param parent frame in which to show dialog
     */
    public static void showUnRecoverableErrorDialog(JPanel parent) {
        ErrorDialogUtils.showRecoverableErrorDialog("Error with connecting to database, only option is to quit", parent);
        System.exit(1);
    }
}
