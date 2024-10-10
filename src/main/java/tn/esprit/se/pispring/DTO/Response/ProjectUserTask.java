package tn.esprit.se.pispring.DTO.Response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectUserTask {
    private String projectName;
    private UserWithTaskStatus userWithTaskStatus;

}
