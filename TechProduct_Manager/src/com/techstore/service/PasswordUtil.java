package com.techstore.service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Simple secure password hashing using SHA-256 + salt.
 * Format: "sha256$<salt>$<hash>"
 */
public class PasswordUtil {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String hash(String rawPassword) {
        try {
            byte[] saltBytes = new byte[16];
            RANDOM.nextBytes(saltBytes);
            String salt = Base64.getEncoder().encodeToString(saltBytes);
            String hashed = sha256(salt + rawPassword);
            return "sha256$" + salt + "$" + hashed;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi mã hóa mật khẩu: " + e.getMessage());
        }
    }

    public static boolean verify(String rawPassword, String stored) {
        try {
            if (stored == null || !stored.startsWith("sha256$")) return false;
            String[] parts = stored.split("\\$", 3);
            if (parts.length != 3) return false;
            String salt = parts[1];
            String expected = parts[2];
            String actual = sha256(salt + rawPassword);
            return expected.equals(actual);
        } catch (Exception e) {
            return false;
        }
    }

    private static String sha256(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = md.digest(input.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
