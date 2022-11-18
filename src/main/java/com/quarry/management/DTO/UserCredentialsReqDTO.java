package com.quarry.management.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialsReqDTO {

//    @Email(message = "Email field should be valid")
    private String email;

//    @NotBlank(message = "Password field should be not empty")
    private String password;

//    @NotBlank(message = "Role field should be not empty")
    private String role;

}
