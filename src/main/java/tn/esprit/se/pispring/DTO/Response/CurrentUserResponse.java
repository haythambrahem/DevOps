package tn.esprit.se.pispring.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurrentUserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> role;
    private String profilePhoto;

}
