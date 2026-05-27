package com.techstore.mapper;

import com.techstore.entity.Admin;
import java.util.List;

public interface AdminMapper {
    void register(Admin admin);
    Admin login(String username, String rawPassword);
    void updateAdmin(Admin admin);
    void deleteAdmin(String id);
    List<Admin> getAllAdmins();
    Admin getAdminById(String id);
    Admin getAdminByUsername(String username);
    void saveToFile();
    void loadFromFile();
}
