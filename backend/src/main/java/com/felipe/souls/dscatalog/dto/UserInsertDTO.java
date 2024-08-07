package com.felipe.souls.dscatalog.dto;

import com.felipe.souls.dscatalog.services.validations.UserInsertValid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@UserInsertValid
public class UserInsertDTO extends UserDTO {
    private static final long serialVersionUID = 1l;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = " The password must have contain at least one uppercase letter, lowercase latter, numbers and special character")
    @Size(min = 8, max = 16, message = "This space is obrigatoried min = 8 e max = 16")
    private String password;

    public UserInsertDTO() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}