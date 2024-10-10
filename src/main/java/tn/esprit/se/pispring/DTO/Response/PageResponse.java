package tn.esprit.se.pispring.DTO.Response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PageResponse<T> {



    private int totalPages;
    private Long totalElements;
    private int pageNumber;

    private List<T> content;




}
