package tn.esprit.se.pispring.DTO.Response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserTaskCountDTO {
    private String firstName;
    private String lastName;
    private int completedTasks;
    private int incompleteTasks;
}
