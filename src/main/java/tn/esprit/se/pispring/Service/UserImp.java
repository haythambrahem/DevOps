package tn.esprit.se.pispring.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.DTO.Request.*;
import tn.esprit.se.pispring.DTO.Response.*;
import tn.esprit.se.pispring.Repository.RoleRepo;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.entities.*;
import tn.esprit.se.pispring.secConfig.JwtUtils;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class UserImp implements UserService {


    @Value("${asserter.default.secret}")
    private  String DEFAULT_PASSWORD;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final RoleRepo roleRepo;
    private final JwtUtils jwtUtils;
    @Value("${file.upload-dir}")
    private String uploadDir;



    private User getUserFromToken(String token) throws Exception {

        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String signup(UserSignupRequest userReq) throws Exception {
        try {
            User user = new User();
            user.setEmail(userReq.getEmail());
            user.setPassword(passwordEncoder.encode(userReq.getPassword()));
            Role role = roleRepo.findRoleByRoleName(ERole.ROLE_USER);
            List<Role> roles = Collections.singletonList(role);
            user.setRoles(roles);
            User  u = userRepository.save(user);
            return "Done";
        } catch (Exception e) {
            throw new Exception("error adding the new user");
        }

    }


    @Override
    public User findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public User createNewUser(String token, UserRequest userRequest) throws Exception {
        log.info(userRequest.toString());
        try {

            User newUser = new User();
            List<Role> roles = new ArrayList<>();
            roles.add(roleRepo.findRoleByRoleName(ERole.ROLE_USER));
            if(userRequest.getRole().equalsIgnoreCase("hre"))
                roles.add(roleRepo.findRoleByRoleName(ERole.ROLE_HRE));
            newUser.setRoles(roles);
            newUser.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
            newUser.setFirstName(userRequest.getFirstName());
            newUser.setLastName(userRequest.getLastName());

            newUser.setEmail(userRequest.getEmail());

            newUser.setEnabled(true);
            return userRepository.save(newUser);
        } catch (Exception e) {
            throw new Exception(e);
        }

    }


    @Override
    public CurrentUserResponse getCurrentUserInfos(String token) throws Exception {
        try {
            User user = userRepository.findByEmail(jwtUtils.getUsernameFromToken(token.split(" ")[1].trim()));
            return new CurrentUserResponse(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRoles().stream().map(role ->  role.getRoleName().toString()).toList(),
                    user.getProfilePhoto()
            );
        }catch (Exception e) {
            throw new Exception(e);

        }
    }

    @Override
    public CurrentUserResponse editCurrentUserInfos(String token, CurrentUserRequest request) throws Exception {
        try {
            User user = userRepository.findByEmail(jwtUtils.getUsernameFromToken(token.split(" ")[1].trim()));

            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            User savedUser = userRepository.save(user);
            return new CurrentUserResponse(
                    savedUser.getId(),
                    savedUser.getFirstName(),
                    savedUser.getLastName(),
                    savedUser.getEmail(),
                    savedUser.getRoles().stream().map(role ->  role.getRoleName().toString()).toList(),
                    savedUser.getProfilePhoto()
            );
        }catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public String editPassword(String token, EditPasswordRequest request) throws Exception {
        try {

            if (!request.getNewPassword().trim().equals(request.getRetypedNewPassword().trim())) {
                throw new Exception("passwords don't match");
            }
            User user = userRepository.findByEmail(jwtUtils.getUsernameFromToken(token.split(" ")[1].trim()));
            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new Exception("incorrect password");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            return "success";
        }catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public List<UserResponse> getUsers(String token) throws Exception {
        try {

            return userRepository.findUsers(userRepository.
                   findByEmail(jwtUtils.getUsernameFromToken(token.split(" ")[1].trim())).
                   getPortfolio()).stream().filter(User -> !User.getRoles().
                   contains(roleRepo.findRoleByRoleName(ERole.ROLE_USER)))
                   .map(User -> new UserResponse(
                   ))
                    .collect(Collectors.toList());

        }catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public List<UserResponse> searchUsers(String token, SearchRequest searchRequest) throws Exception {
        try {
            List<User> users = userRepository.searchUsers(searchRequest.getKeyword());

            List<User> filteredUsers = users.stream()
                    .filter(user -> user.getRoles().stream().noneMatch(role -> role.getRoleName() == ERole.ROLE_ADMIN))
                    .collect(Collectors.toList());

            List<UserResponse> userResponses = filteredUsers.stream()
                    .map(user -> new UserResponse(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            user.getTelephone(),
                            user.getRoles().stream().map(role1 -> role1.getRoleName().toString()).toList()
                    ))
                    .collect(Collectors.toList());

            return userResponses;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
        public void deleteUsers(DeleteUsersRequest deleteUsersRequest) throws Exception {
            log.info("#########################");
            log.info("this is the userRequestEmailsSize {}", deleteUsersRequest.getEmails().size());
            try {
                if (deleteUsersRequest.getEmails().size() == 1) {
                    String email = deleteUsersRequest.getEmails().get(0);
                    User user = userRepository.findAppUserByEmail(email);
                    if (user != null) {
                        userRepository.delete(user);
                    }
                } else {
                    for (String email : deleteUsersRequest.getEmails()) {
                        User user = userRepository.findAppUserByEmail(email);
                        if (user != null) {
                            userRepository.delete(user);
                        }
                    }
                }
            } catch (Exception e) {
                throw new Exception(e);
            }
        }

    @Override
    public PageResponse<UserResponse> findAll(Long page, Long size, String criteria, String direction, String searchTerm) {

        Sort sort = Sort.by(Objects.equals(direction, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC,criteria);

        PageRequest pageRequest = PageRequest.of(Math.toIntExact(page), Math.toIntExact(size), sort);
        Page<User> result;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            result = userRepository.findAll(pageRequest);
        }else {
            result = userRepository.searchBy(pageRequest, searchTerm);
        }


        PageResponse<UserResponse> response = new PageResponse<>();
        response.setContent(result.getContent().stream().map(user -> new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getTelephone(),user.getRoles().stream().map(role1 -> role1.getRoleName().toString()).toList())).toList());
        response.setPageNumber(result.getNumber());
        response.setTotalElements(result.getTotalElements());
        response.setTotalPages(response.getTotalPages());

        return response;
    }

    @Override
    public UserResponse addUser(AddUserRequest request) {


        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setTelephone(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode("123456789"));

        User saved = userRepository.save(user);

        return new UserResponse(saved.getId(), saved.getFirstName(), saved.getLastName(), saved.getEmail(), saved.getTelephone(),saved.getRoles().stream().map(role1 -> role1.getRoleName().toString()).toList());
    }

    @Override
    public void banUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setEnabled(false);
            userRepository.save(user);
        }
    }

    @Override
    public long getTotalBannedUsers() {
        return userRepository.countByEnabledFalse();

    }



        private UserWithTaskStatus createUserWithTaskStatus(User user) {
            UserWithTaskStatus userWithTaskStatus = new UserWithTaskStatus();
            userWithTaskStatus.setUser(user);
            userWithTaskStatus.setTasksByStatus(getTasksByStatus(user));

            return userWithTaskStatus;
        }
        private Map<TaskStatus, Integer> getTasksByStatus(User user) {
            Map<TaskStatus, Integer> tasksByStatus = new HashMap<>();


            for (Task task : user.getTasks()) {
                TaskStatus status = task.getTaskStatus();
                tasksByStatus.put(status, tasksByStatus.getOrDefault(status, 0) + 1);
            }

            return tasksByStatus;
        }

    @Override
    public Map<String, Object> getUsersTaskStatus() {
        List<User> users = userRepository.findAll();

        List<User> usersWithRoleUser = users.stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getRoleName() == ERole.ROLE_USER))
                .collect(Collectors.toList());

        int totalUsers = usersWithRoleUser.size();

        int usersWithAllTasksCompleted = 0;
        int usersWithIncompleteTasks = 0;

        for (User user : usersWithRoleUser) {
            boolean allTasksCompleted = true;
            for (Task task : user.getTasks()) {
                if (task.getTaskStatus() != TaskStatus.COMPLETED) {
                    allTasksCompleted = false;
                    break;
                }
            }
            if (allTasksCompleted) {
                usersWithAllTasksCompleted++;
            } else {
                usersWithIncompleteTasks++;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalUsers", totalUsers);
        result.put("usersWithAllTasksCompleted", usersWithAllTasksCompleted);
        result.put("usersWithIncompleteTasks", usersWithIncompleteTasks);

        return result;
    }

    @Override
    public List<UserTaskCountDTO> getUsersWithTaskStatus() {
        List<User> users = userRepository.findAll();
        List<UserTaskCountDTO> userTaskCountDTOs = new ArrayList<>();

        for (User user : users) {
            int completedTasks = 0;
            int incompleteTasks = 0;

            for (Task task : user.getTasks()) {
                if (task.getTaskStatus() == TaskStatus.COMPLETED ||task.getTaskStatus() == TaskStatus.CANCELLED) {
                    completedTasks++;
                } else {
                    incompleteTasks++;
                }
            }

            UserTaskCountDTO dto = new UserTaskCountDTO();
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setCompletedTasks(completedTasks);
            dto.setIncompleteTasks(incompleteTasks);

            userTaskCountDTOs.add(dto);
        }

        return userTaskCountDTOs;
    }
    @Override
    public List<UserTasksDTO> getUsersTasksWithCount() {
        List<User> users = userRepository.findAll();
        List<UserTasksDTO> usersTasksDTOList = new ArrayList<>();

        for (User user : users) {
            UserTasksDTO userTasksDTO = new UserTasksDTO();
            userTasksDTO.setFirstName(user.getFirstName());
            userTasksDTO.setLastName(user.getLastName());

            Map<TaskStatus, Integer> tasksByStatus = new HashMap<>();
            for (Task task : user.getTasks()) {
                TaskStatus status = task.getTaskStatus();
                tasksByStatus.put(status, tasksByStatus.getOrDefault(status, 0) + 1);
            }

            List<TaskStatusCountDTO> tasksDTOList = new ArrayList<>();
            for (Map.Entry<TaskStatus, Integer> entry : tasksByStatus.entrySet()) {
                TaskStatusCountDTO taskStatusCountDTO = new TaskStatusCountDTO();
                taskStatusCountDTO.setStatus(entry.getKey());
                taskStatusCountDTO.setCount(entry.getValue());
                tasksDTOList.add(taskStatusCountDTO);
            }

            userTasksDTO.setTasks(tasksDTOList);
            usersTasksDTOList.add(userTasksDTO);
        }

        return usersTasksDTOList;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public User retrieveUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }


}
