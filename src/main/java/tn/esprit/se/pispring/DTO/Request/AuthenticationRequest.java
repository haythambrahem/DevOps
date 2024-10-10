package tn.esprit.se.pispring.DTO.Request;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String email;

    private String password;

}
