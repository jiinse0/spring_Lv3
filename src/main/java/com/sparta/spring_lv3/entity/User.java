package com.sparta.spring_lv3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING) /*문자열로 매핑되어 데이터베이스에 저장*/
    private UserRoleEnum role;

    public User(String username, String password, UserRoleEnum role) {
        this.username = getUsername();
        this.password = getPassword();
        this.role = getRole();
    }
}
