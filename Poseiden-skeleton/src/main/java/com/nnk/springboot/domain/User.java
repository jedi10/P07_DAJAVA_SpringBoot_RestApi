package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @NotNull(message = "UserName is mandatory")
    @Size(min = 2, max = 35, message = "Name must be between 2 and 35 characters long")
    //@Column(nullable = false, length = 35)
    @Getter
    @Setter
    private String username;

    @NotBlank(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    //@Column(nullable = false)
    @Getter
    @Setter
    private String password;

    @NotBlank(message = "FullName is mandatory")
    @NotNull(message = "FullName is mandatory")
    @Size(min = 2, max = 35, message = "FullName must be between 2 and 35 characters long")
    //@Column(nullable = false, length = 35)
    @Getter
    @Setter
    private String fullname;

    @NotBlank(message = "Role is mandatory")
    @NotNull(message = "Role is mandatory")
    @Size(min = 2, max = 35, message = "Role must be between 2 and 35 characters long")
    //@Column(nullable = false, length = 35)
    @Getter
    @Setter
    private String role;

}

//https://www.baeldung.com/java-bean-validation-not-null-empty-blank
//https://www.baeldung.com/hibernate-notnull-vs-nullable