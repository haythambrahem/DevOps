package tn.esprit.se.pispring.Service;

import tn.esprit.se.pispring.Service.IProjectService;
import tn.esprit.se.pispring.entities.Project;
import tn.esprit.se.pispring.entities.User;

import java.util.List;
import java.util.Map;

public interface INoteService {

    void classifyProjects();

    Map<Long, List<User>> getCompletedProjectsAndUsers();

    Map<Long, List<User>> getretardProjectsAndUsers();

    void assignUsersToNote1();

    void assignUsersToNote3();

    Map<User, Long> countUserOccurrencesForNote1();

    Map<User, Long> countUserOccurrencesForNote3();

    Map<User, Map<String, Long>> countNoteOccurrencesForEachUser();
}
