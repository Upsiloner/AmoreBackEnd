package com.amorebackend.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;

@Data
@Entity
@Table("user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uid;   // 全局唯一用户标识

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;  // 加密存储

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    public User() {
        this.uid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        this.createTime = LocalDateTime.now();
    }
}
