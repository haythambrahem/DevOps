package tn.esprit.se.pispring.DTO.Response;

import lombok.*;
import tn.esprit.se.pispring.entities.TaskStatus;
import tn.esprit.se.pispring.entities.User;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserWithTaskStatus {
    private User user;
    private Map<TaskStatus, Integer> tasksByStatus;

}
