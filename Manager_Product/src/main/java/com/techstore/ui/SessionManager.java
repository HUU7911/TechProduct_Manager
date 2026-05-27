package com.techstore.ui;

import com.techstore.entity.Admin;
import lombok.Getter;
import lombok.Setter;

public class SessionManager {
    @Setter
    @Getter
    private static Admin currentAdmin;

    public static void logout() { currentAdmin = null; }
    public static boolean isLoggedIn() { return currentAdmin != null; }
}
