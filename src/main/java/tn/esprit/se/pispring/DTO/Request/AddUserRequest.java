package tn.esprit.se.pispring.DTO.Request;

import lombok.Data;

@Data
public class    AddUserRequest {


    private String firstName;
    private String lastName;
    private String email;
    private Integer phoneNumber;


}
