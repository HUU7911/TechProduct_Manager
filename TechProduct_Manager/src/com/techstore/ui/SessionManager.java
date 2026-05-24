package com.techstore.ui;

import com.techstore.entity.Admin;

public class SessionManager {
    private static Admin currentAdmin;
    public static Admin getCurrentAdmin() { return currentAdmin; }
    public static void setCurrentAdmin(Admin a) { currentAdmin = a; }
    public static void logout() { currentAdmin = null; }
    public static boolean isLoggedIn() { return currentAdmin != null; }
}