package tn.esprit.se.pispring.Service;

import org.springframework.security.core.userdetails.UserDetails;

public interface RefreshTokenService {
    String generateNewRefreshTokenForUser(UserDetails user) throws Exception;

    void deleteRefreshToken(String refreshToken);
}
