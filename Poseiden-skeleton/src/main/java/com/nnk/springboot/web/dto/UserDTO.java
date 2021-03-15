package com.nnk.springboot.web.dto;

import com.nnk.springboot.web.dto.validation.ValidPassword;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDTO {

    private Integer id;

    @NotBlank(message = "Username is mandatory")
    @Size(min = 2, max = 35, message = "UserName must be between 2 and 35 characters long")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @ValidPassword
    private String password;

    @NotBlank(message = "FullName is mandatory")
    @Size(min = 2, max = 35, message = "FullName must be between 2 and 35 characters long")
    private String fullname;

    @NotBlank(message = "Role is mandatory")
    @Size(min = 2, max = 35, message = "Role must be between 2 and 35 characters long")
    private String role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO that = (UserDTO) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(fullname, that.fullname) &&
                Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, fullname, role);
    }

    @Override
    public String toString() {
        return "UserRegistrationDto{" +
                "username='" + username + '\'' +
                ", fullname='" + fullname + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
