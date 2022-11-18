package com.quarry.management.DTO;

import lombok.Data;

@Data
public class ForgotPasswordReqDTO {

    //@Email(message = "Email field should be valid")
    private String email;

    private String verificationCode;

    private String password;

}
