package tn.esprit.se.pispring.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.RefreshJwtTokenRepository;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.entities.JwtRefreshToken;
import tn.esprit.se.pispring.entities.User;
import tn.esprit.se.pispring.secConfig.JwtUtils;

import javax.transaction.Transactional;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{
    private final JwtUtils jwtUtils;
    private final RefreshJwtTokenRepository refreshJwtTokenRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public String generateNewRefreshTokenForUser(UserDetails user) throws Exception, UsernameNotFoundException {
        try {

            Optional<JwtRefreshToken> existingToken = refreshJwtTokenRepository.findTokenByUser(user.getUsername());
            existingToken.ifPresent(token -> refreshJwtTokenRepository.deleteById(token.getId()));
            String token = jwtUtils.generateRefreshToken(user);
            JwtRefreshToken refreshToken = new JwtRefreshToken();
            refreshToken.setToken(token);
            User user1 = userRepository.findAppUserByEmail(user.getUsername());
            refreshToken.setUser(user1);
            refreshJwtTokenRepository.save(refreshToken);
            return token;
        }
        catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
        catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        Optional<JwtRefreshToken> existingToken = refreshJwtTokenRepository.findJwtRefreshTokenByToken(refreshToken);
        if (existingToken.isPresent() && !existingToken.isEmpty()) {
            refreshJwtTokenRepository.deleteById(existingToken.get().getId());
        }
    }
}
