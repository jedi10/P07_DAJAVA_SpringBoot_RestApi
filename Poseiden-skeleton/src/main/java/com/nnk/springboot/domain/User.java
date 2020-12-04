package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter
    @Setter
    private Integer id;

    @NotBlank(message = "Username is mandatory")
    @Column(nullable = false, length = 35)
    @Getter
    @Setter
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Column(nullable = false)
    @Getter
    @Setter
    private String password;

    @NotBlank(message = "FullName is mandatory")
    @Column(nullable = false, length = 35)
    @Getter
    @Setter
    private String fullname;

    @NotBlank(message = "Role is mandatory")
    @Column(nullable = false, length = 35)
    @Getter
    @Setter
    private String role;

}


//https://www.baeldung.com/hibernate-notnull-vs-nullable