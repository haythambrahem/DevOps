package tn.esprit.se.pispring.DTO.Response;

import lombok.*;
import tn.esprit.se.pispring.entities.TaskStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskStatusCountDTO {
    private TaskStatus status;
    private int count;
}
