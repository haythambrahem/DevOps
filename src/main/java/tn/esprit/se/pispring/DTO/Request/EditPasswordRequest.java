package tn.esprit.se.pispring.DTO.Request;

import lombok.Data;

@Data
public class EditPasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String retypedNewPassword;

}
