package com.techstore.entity;

import java.util.UUID;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Admin {
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String username;
    private String passwordHash;
    private String email;
    private String fullName;
    private String phone;
}