package tn.esprit.se.pispring.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.se.pispring.DTO.Request.*;
import tn.esprit.se.pispring.DTO.Response.*;
import tn.esprit.se.pispring.entities.TaskStatus;
import tn.esprit.se.pispring.entities.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    String uploadProfilePhoto(String token, MultipartFile file) throws Exception;

    String signup(UserSignupRequest userReq) throws Exception;

    List<User> getAlluser();

    User findByEmail(String username);
    User createNewUser(String token, UserRequest userRequest) throws Exception;


    CurrentUserResponse getCurrentUserInfos(String token) throws Exception;

    CurrentUserResponse editCurrentUserInfos(String token, CurrentUserRequest request)throws Exception;

    String editPassword(String token, EditPasswordRequest request)throws Exception;

    List<UserResponse> getUsers(String token)throws Exception;

    List<UserResponse> searchUsers(String token, SearchRequest searchRequest)throws Exception;

    void deleteUsers(DeleteUsersRequest deleteUsersRequest) throws Exception;

    PageResponse<UserResponse> findAll(Long page, Long size, String criteria, String direction, String searchTerm);
    UserResponse addUser(AddUserRequest request);


    void banUserByEmail(String email);
    long getTotalBannedUsers();

    Map<TaskStatus, Integer> getTasksByStatus(Long userId);

    List<ProjectUserTask> getUsersPerProjectAndTasks();

    Map<String, Object> getUsersTaskStatus();

    List<UserTaskCountDTO> getUsersWithTaskStatus();

    List<UserTasksDTO> getUsersTasksWithCount();
    List<User> getAllUsers();

    User getUserById(Long id);

    User retrieveUser(Long id);
}
