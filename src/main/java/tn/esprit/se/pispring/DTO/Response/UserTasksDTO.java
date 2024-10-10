package tn.esprit.se.pispring.DTO.Response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserTasksDTO {
    private String firstName;
    private String LastName;
    private List<TaskStatusCountDTO> tasks;
}
