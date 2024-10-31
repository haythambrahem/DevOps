package tests;

import org.assertj.core.api.junit.jupiter.SoftlyExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.se.pispring.PiSpringApplication;
import tn.esprit.se.pispring.Repository.RecruitmentRepository;
import tn.esprit.se.pispring.Service.RecruitmentService;
import tn.esprit.se.pispring.entities.Recruitment;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes= PiSpringApplication.class)
@ExtendWith(SpringExtension.class)
public class RecruitmentServiceTest {

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Autowired
    RecruitmentService recruitmentService;

    @Test
    public void testCalculateAverageSalaryRange_MultipleRecruitments() {
        // Arrange
        Recruitment recruitment1 = new Recruitment();
        recruitment1.setSalaryRangeMin(3000);
        recruitment1.setSalaryRangeMax(5000);
        recruitmentRepository.save(recruitment1);

        Recruitment recruitment2 = new Recruitment();
        recruitment2.setSalaryRangeMin(4000);
        recruitment2.setSalaryRangeMax(6000);
        recruitmentRepository.save(recruitment2);

        Recruitment recruitment3 = new Recruitment();
        recruitment3.setSalaryRangeMin(2000);
        recruitment3.setSalaryRangeMax(4000);
        recruitmentRepository.save(recruitment3);

        // Act
        double averageSalaryRange = recruitmentService.calculateAverageSalaryRange();

        // Assert
        double expectedAverage = 4000;
        assertEquals(expectedAverage, averageSalaryRange,
                "The calculated average salary range should match the expected value.");
    }

    @Test
    public void testCalculateAverageSalaryRange_EmptyRecruitments() {
        // Act
        double averageSalaryRange = recruitmentService.calculateAverageSalaryRange();

        // Assert
        assertEquals(Double.NaN, averageSalaryRange,
                "The average salary range should be NaN when no recruitments are found.");
    }

    @Test
    public void testCalculateAverageSalaryRange_OneRecruitment() {
        // Arrange
        Recruitment recruitment = new Recruitment();
        recruitment.setSalaryRangeMin(3500);
        recruitment.setSalaryRangeMax(4500);
        recruitmentRepository.save(recruitment);

        // Act
        double averageSalaryRange = recruitmentService.calculateAverageSalaryRange();

        // Assert
        double expectedAverage = 4000;
        assertEquals(expectedAverage, averageSalaryRange,
                "The calculated average salary range should match the expected value for one recruitment.");
    }
}
