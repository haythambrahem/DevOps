package tn.esprit.se.pispring.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.entities.Role;
import tn.esprit.se.pispring.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;




    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        List<Role> roles = user.getRoles();

        List<GrantedAuthority> authorities = roles.size() == 1 ? List.of(new SimpleGrantedAuthority(roles.get(0)
                .getRoleName().toString())) : roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName()
                .toString())).collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                authorities
        );

    }
}
