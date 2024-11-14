package ProjectTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.se.pispring.Repository.ProjectRepository;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.Service.ProjectService;
import tn.esprit.se.pispring.entities.Project;
import tn.esprit.se.pispring.entities.ProjectStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateCompletedFuturePercentage_WhenNoProjects() {

        when(projectService.getAllProject()).thenReturn(Collections.emptyList());


        double result = projectService.calculateCompletedFuturePercentage();


        assertEquals(0.0, result, "Percentage should be 0.0 when there are no projects");
    }

    @Test
    public void testCalculateCompletedFuturePercentage_WithProvidedProjects() {
        // Given
        Project project1 = new Project();
        project1.setProjectStatus(ProjectStatus.COMPLETED);

        Project project2 = new Project();
        project2.setProjectStatus(ProjectStatus.COMPLETED);

        Project project3 = new Project();
        project3.setProjectStatus(ProjectStatus.CURRENT);

        List<Project> projects = Arrays.asList(project1, project2, project3);


        double percentage = projectService.calculateCompletedFuturePercentage(projects);


        assertEquals(66.67, percentage, 0.01);
    }



}
