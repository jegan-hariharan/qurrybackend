package com.quarry.management.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReqDTO {

    private String role;
    private String name;
    private String email;
    private String phoneNumber;
    private String gender;
    private Boolean isActive;
    private Long quarryId;
}
