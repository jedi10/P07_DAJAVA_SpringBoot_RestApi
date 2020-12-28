package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

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

    /**
     * <b>Constructor User</b>
     * @param username username
     * @param password password
     * @param fullname fullName
     * @param role role
     */
    public User(String username, String password, String fullname, String role) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(fullname, user.fullname) &&
                Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, fullname, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", fullname='" + fullname + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

//https://www.baeldung.com/java-bean-validation-not-null-empty-blank
//https://www.baeldung.com/hibernate-notnull-vs-nullable
//https://nullbeans.com/how-to-use-java-bean-validation-in-spring-boot/