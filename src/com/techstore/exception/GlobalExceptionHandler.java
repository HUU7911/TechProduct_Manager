package com.techstore.exception;

import javax.swing.*;
import java.awt.*;

public class GlobalExceptionHandler {
    
    public static void handle(Component parent, Exception ex) {

        if (ex instanceof AppException appEx) {

            JOptionPane.showMessageDialog(
                    parent,
                    "[" + appEx.getErrorCode().getCode() + "] "
                            + appEx.getMessage(),
                    "Application Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } else {

            JOptionPane.showMessageDialog(
                    parent,
                    ex.getMessage(),
                    "System Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }
}