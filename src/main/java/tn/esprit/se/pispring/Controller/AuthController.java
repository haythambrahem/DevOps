package tn.esprit.se.pispring.Controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.DTO.Request.AuthenticationRequest;
import tn.esprit.se.pispring.DTO.Request.UserSignupRequest;
import tn.esprit.se.pispring.DTO.Response.AuthenticationResponse;
import tn.esprit.se.pispring.DTO.Response.ProjectResponse;
import tn.esprit.se.pispring.DTO.Response.UserResponse;
import tn.esprit.se.pispring.Service.UserService;
import tn.esprit.se.pispring.Service.UserStatisticsService;
import tn.esprit.se.pispring.entities.Project;
import tn.esprit.se.pispring.entities.User;
import tn.esprit.se.pispring.secConfig.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
@CrossOrigin(origins ="http://localhost:8089")
@RestController
@RequestMapping("/auth")
@Slf4j
//@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private UserStatisticsService userStatisticsService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupRequest userReq) throws Exception {

        try {
            String result = userService.signup(userReq);

            return ResponseEntity.ok(result);
        }catch (Exception e) {
            throw new Exception(e);
        }

    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest, final HttpServletResponse response) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad Credentials");
        }

        final UserDetails user = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        User user1 = userService.findByEmail(user.getUsername());

//        String refreshToken = refreshTokenService.generateNewRefreshTokenForUser(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
//        Cookie cookie = createRefreshTokenCookie(refreshToken);
//        response.addCookie(cookie);
        AuthenticationResponse auth = new AuthenticationResponse(user1.getId(), jwtUtils.generateToken(user, claims), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        log.info("###########################");
        log.info("###########################");
        log.info("###########################");
        log.info("###########################");
        log.info("this is the authentication response ");
        log.info("this is the token");
        log.info("=================");
        log.info(auth.getToken());
        log.info("this is the id");
        log.info("=================");
        log.info(auth.getId().toString());
        log.info("###########################");
        log.info("###########################");
        log.info("###########################");
        log.info("###########################");
        log.info("###########################");
        log.info("###########################");
        return ResponseEntity.ok(auth);
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) throws Exception {
        return null; //userService.validateVerificationTokenForUser(token);
    }

    @GetMapping("/resendVerification")
    public String resendVerificationToken(@RequestParam("token") String oldToken, final HttpServletRequest httpServletRequest) throws Exception {
//        try {
//            VerificationToken token = userService.generateNewTokenForUser(oldToken);
//            User user = token.getUser();
//            resendVerificationEmail(applicationUrl(httpServletRequest), token, user);
//
//            return "verification link sent";
//        }catch (Exception e) {
//            throw new Exception("error resending the verification token please try again in 30 seconds");
//        }
        return oldToken;
    }

//    @GetMapping("/{userId}/task-count-per-project")
//    public ResponseEntity<Map<ProjectResponse, Integer>> getTaskCountPerProject(@PathVariable Long userId) {
//        Map<ProjectResponse, Integer> taskCountPerProject = userStatisticsService.getTaskCountPerProject(userId);
//        if (taskCountPerProject == null) {
//            // Gérer le cas où l'utilisateur n'existe pas
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(taskCountPerProject, HttpStatus.OK);
//    }

    @GetMapping("/task-count-per-project")
    public ResponseEntity<Map<UserResponse, Map<ProjectResponse, Integer>>> getUserTaskCountPerProject() {
        Map<UserResponse, Map<ProjectResponse, Integer>> userTaskCountPerProject = userStatisticsService.getUserTaskCountPerProject();
        return new ResponseEntity<>(userTaskCountPerProject, HttpStatus.OK);
    }

    @GetMapping("/user-count")
    public ResponseEntity<Map<String, Integer>> getUserCountPerProject() {
        Map<String, Integer> userCountPerProject = userStatisticsService.getUserCountPerProject();
        return new ResponseEntity<>(userCountPerProject, HttpStatus.OK);
    }

}
