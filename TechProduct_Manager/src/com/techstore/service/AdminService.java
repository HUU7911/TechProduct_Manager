package com.techstore.service;

import com.techstore.entity.Admin;
import com.techstore.mapper.AdminMapper;

import java.io.*;
import java.util.*;

public class AdminService implements AdminMapper {

    private final List<Admin> admins = new ArrayList<>();
    private static final String FILE_PATH = "data/admins.bin";

    public AdminService() {
        new File("data").mkdirs();
        loadFromFile();
        if (admins.isEmpty()) {
            String hash = PasswordUtil.hash("admin123");
            Admin admin = Admin.builder()
                    .username("admin")
                    .passwordHash(hash)
                    .email("admin@techstore.com")
                    .fullName("Quản trị viên")
                    .phone("0123456789")
                    .build();
            admins.add(admin);
            saveToFile();
        }
    }

    @Override
    public void register(Admin admin) {
        if (admin == null) throw new IllegalArgumentException("Tài khoản không được null");
        if (admin.getUsername() == null || admin.getUsername().isBlank())
            throw new IllegalArgumentException("Tên đăng nhập không được trống");
        if (admins.stream().anyMatch(a -> a.getUsername().equalsIgnoreCase(admin.getUsername())))
            throw new IllegalStateException("Tên đăng nhập đã tồn tại");
        admins.add(admin);
        saveToFile();
    }

    @Override
    public Admin login(String username, String rawPassword) {
        Admin admin = admins.stream()
                .filter(a -> a.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Tài khoản không tồn tại"));
        if (!PasswordUtil.verify(rawPassword, admin.getPasswordHash()))
            throw new SecurityException("Mật khẩu không chính xác");
        return admin;
    }

    @Override
    public void updateAdmin(Admin admin) {
        if (admin == null) throw new IllegalArgumentException("Tài khoản không được null");
        for (int i = 0; i < admins.size(); i++) {
            if (admins.get(i).getId().equals(admin.getId())) {
                admins.set(i, admin); saveToFile(); return;
            }
        }
        throw new NoSuchElementException("Không tìm thấy tài khoản");
    }

    @Override
    public void deleteAdmin(String id) {
        if (admins.size() <= 1) throw new IllegalStateException("Phải có ít nhất một tài khoản admin");
        if (!admins.removeIf(a -> a.getId().equals(id)))
            throw new NoSuchElementException("Không tìm thấy tài khoản");
        saveToFile();
    }

    @Override
    public List<Admin> getAllAdmins() { return new ArrayList<>(admins); }

    @Override
    public Admin getAdminById(String id) {
        return admins.stream().filter(a -> a.getId().equals(id)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy tài khoản"));
    }

    @Override
    public Admin getAdminByUsername(String username) {
        return admins.stream().filter(a -> a.getUsername().equalsIgnoreCase(username)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy tài khoản"));
    }

    @Override
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            List<String[]> data = new ArrayList<>();
            for (Admin a : admins)
                data.add(new String[]{a.getId(), a.getUsername(), a.getPasswordHash(), a.getEmail(), a.getFullName(), a.getPhone()});
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi lưu file admin: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        File f = new File(FILE_PATH);
        if (!f.exists()) return;
        admins.clear();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            List<String[]> data = (List<String[]>) ois.readObject();
            for (String[] row : data) {
                admins.add(new Admin(row[0], row[1], row[2], row[3], row[4], row[5]));
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi đọc file admin: " + e.getMessage());
        }
    }

    public String hashPassword(String raw) { return PasswordUtil.hash(raw); }
}
