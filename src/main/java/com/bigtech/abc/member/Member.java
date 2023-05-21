package com.bigtech.abc.member;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String name;

    private String password;

    @Column(unique = true)
    private String email;

    private LocalDateTime birth;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    public Member(Long id, String name, String password, String email, LocalDateTime birth, LocalDateTime createdDate, LocalDateTime updateDate) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.birth = birth;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
    }
}
