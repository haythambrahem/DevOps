package tn.esprit.se.pispring.DTO.Request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank(message = "first name is mandatory")
    private String firstName;
    @NotBlank(message = "last name is mandatory")
    private String lastName;
    @NotBlank(message = "email is mandatory")
    private String email;
    private String role = "user";

    private String portfolio;

}
