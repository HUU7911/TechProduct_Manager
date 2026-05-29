package com.techstore.service;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private static final int Strength = 10;

    public static String hashPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Mat khau khong duoc de trong");
        }

        return BCrypt.hashpw(password, BCrypt.gensalt(Strength));
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null || hashedPassword.isBlank()) {
            return false;
        }

        try {
            return  BCrypt.checkpw(password, hashedPassword);
        }catch (Exception e) {
            return false;
        }
    }
}
