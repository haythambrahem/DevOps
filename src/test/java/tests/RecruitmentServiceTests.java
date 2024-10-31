package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.se.pispring.Repository.RecruitmentRepository;
import tn.esprit.se.pispring.Service.RecruitmentService;
import tn.esprit.se.pispring.entities.Recruitment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class RecruitmentServiceTests {

    @Mock
    private RecruitmentRepository recruitmentRepository;

    @InjectMocks
    private RecruitmentService recruitmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateAverageSalaryRange_MultipleRecruitments() {
        // Arrange
        Recruitment recruitment1 = Recruitment.builder()
                .salaryRangeMin(3000)
                .salaryRangeMax(5000)
                .build();

        Recruitment recruitment2 = Recruitment.builder()
                .salaryRangeMin(4000)
                .salaryRangeMax(6000)
                .build();

        Recruitment recruitment3 = Recruitment.builder()
                .salaryRangeMin(2000)
                .salaryRangeMax(4000)
                .build();

        List<Recruitment> recruitments = Arrays.asList(recruitment1, recruitment2, recruitment3);

        when(recruitmentRepository.findAll()).thenReturn(recruitments);

        // Act
        double averageSalaryRange = recruitmentService.calculateAverageSalaryRange();

        // Assert
        double expectedAverage = 4000;
        assertEquals(expectedAverage, averageSalaryRange,
                "The calculated average salary range should match the expected value.");


        System.out.println("Test Multiple Recruitments: Expected average = " + expectedAverage +
                ", Actual average = " + averageSalaryRange);
    }

    @Test
    public void testCalculateAverageSalaryRange_EmptyRecruitments() {
        // Arrange
        when(recruitmentRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        double averageSalaryRange = recruitmentService.calculateAverageSalaryRange();

        // Assert
        assertTrue(Double.isNaN(averageSalaryRange),
                "The average salary range should be NaN when no recruitments are found.");


        System.out.println("Test Empty Recruitments: Average salary range is NaN = " + Double.isNaN(averageSalaryRange));
    }

    @Test
    public void testCalculateAverageSalaryRange_OneRecruitment() {
        // Arrange
        Recruitment recruitment = Recruitment.builder()
                .salaryRangeMin(3500)
                .salaryRangeMax(4500)
                .build();

        when(recruitmentRepository.findAll()).thenReturn(Collections.singletonList(recruitment));

        // Act
        double averageSalaryRange = recruitmentService.calculateAverageSalaryRange();

        // Assert
        double expectedAverage = 4000;
        assertEquals(expectedAverage, averageSalaryRange,
                "The calculated average salary range should match the expected value for one recruitment.");


        System.out.println("Test One Recruitment: Expected average = " + expectedAverage +
                ", Actual average = " + averageSalaryRange);
    }
}
