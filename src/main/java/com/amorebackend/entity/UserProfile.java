package com.amorebackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Entity
@Table("user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uid;

    @Column(nullable = false)
    private Integer gender;

    @Column(length = 255)
    private String bio;

    @Column(nullable = false)
    private Integer points = 0;

    @Lob
    @Basic(fetch = FetchType.LAZY)  // 懒加载，减少查询压力
    private byte[] avatar;

//    birthday

    public UserProfile() {}

    public UserProfile(String uid) {
        this.uid = uid;
        this.gender = 0;
        this.points = 0;
        this.bio = "";
        this.avatar = null;
    }
}
