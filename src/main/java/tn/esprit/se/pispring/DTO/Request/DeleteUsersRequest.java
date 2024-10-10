package tn.esprit.se.pispring.DTO.Request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
@Data
public class DeleteUsersRequest {
    @NotNull(message = "should at least contain one element")
    private List<String> emails = new ArrayList<>();
}
