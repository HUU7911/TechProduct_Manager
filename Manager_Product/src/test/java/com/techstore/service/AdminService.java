package com.techstore.service;

import com.techstore.entity.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest {

    private AdminService adminService;

    @BeforeEach
    void setUp() {

        // xóa file cũ để test độc lập
        File file = new File("data/admin.bin");

        if (file.exists()) {
            file.delete();
        }

        adminService = new AdminService();
    }

    @Test
    void register_success() {

        Admin admin = Admin.builder()
                .id("A01")
                .username("huu")
                .passwordHash("123456")
                .email("huu@gmail.com")
                .fullName("Huu Tran")
                .phone("0123456789")
                .build();

        adminService.register(admin);

        Admin result = adminService.getAdminByUsername("huu");

        assertNotNull(result);
        assertEquals("huu", result.getUsername());
    }

    @Test
    void register_duplicateUsername() {

        Admin admin1 = Admin.builder()
                .id("A02")
                .username("duplicate")
                .passwordHash("123")
                .email("a@gmail.com")
                .fullName("Admin A")
                .phone("0111")
                .build();

        Admin admin2 = Admin.builder()
                .id("A03")
                .username("duplicate")
                .passwordHash("456")
                .email("b@gmail.com")
                .fullName("Admin B")
                .phone("0222")
                .build();

        adminService.register(admin1);

        assertThrows(IllegalStateException.class, () -> {
            adminService.register(admin2);
        });
    }

    @Test
    void register_nullAdmin() {

        assertThrows(IllegalArgumentException.class, () -> {
            adminService.register(null);
        });
    }

    @Test
    void login_success() {

        Admin admin = Admin.builder()
                .id("A04")
                .username("login_test")
                .passwordHash("123456")
                .email("login@gmail.com")
                .fullName("Login Test")
                .phone("0999")
                .build();

        adminService.register(admin);

        Admin result = adminService.login("login_test", "123456");

        assertNotNull(result);
        assertEquals("login_test", result.getUsername());
    }

    @Test
    void login_wrongPassword() {

        Admin admin = Admin.builder()
                .id("A05")
                .username("wrong_pass")
                .passwordHash("123456")
                .email("wrong@gmail.com")
                .fullName("Wrong Password")
                .phone("0888")
                .build();

        adminService.register(admin);

        assertThrows(SecurityException.class, () -> {
            adminService.login("wrong_pass", "abcdef");
        });
    }

    @Test
    void login_userNotFound() {

        assertThrows(NoSuchElementException.class, () -> {
            adminService.login("unknown", "123456");
        });
    }

    @Test
    void updateAdmin_success() {

        Admin admin = Admin.builder()
                .id("A06")
                .username("update_test")
                .passwordHash("123456")
                .email("old@gmail.com")
                .fullName("Old Name")
                .phone("0777")
                .build();

        adminService.register(admin);

        admin.setFullName("New Name");

        adminService.updateAdmin(admin);

        Admin updated = adminService.getAdminById("A06");

        assertEquals("New Name", updated.getFullName());
    }

    @Test
    void deleteAdmin_success() {

        Admin admin = Admin.builder()
                .id("A07")
                .username("delete_test")
                .passwordHash("123456")
                .email("delete@gmail.com")
                .fullName("Delete Test")
                .phone("0666")
                .build();

        adminService.register(admin);

        adminService.deleteAdmin("A07");

        assertThrows(NoSuchElementException.class, () -> {
            adminService.getAdminById("A07");
        });
    }

    @Test
    void getAllAdmins_success() {

        List<Admin> admins = adminService.getAllAdmins();

        assertNotNull(admins);

        // luôn có ít nhất admin mặc định
        assertFalse(admins.isEmpty());
    }
}