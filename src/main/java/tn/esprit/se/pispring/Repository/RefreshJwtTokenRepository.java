package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.ResponseBody;
import tn.esprit.se.pispring.entities.JwtRefreshToken;

import javax.transaction.Transactional;
import java.util.Optional;

@ResponseBody
public interface RefreshJwtTokenRepository extends JpaRepository<JwtRefreshToken, Long> {
    @Query("select u from JwtRefreshToken u where u.user.email = :username")
    Optional<JwtRefreshToken> findTokenByUser(String username);

    @Transactional
    @Override
    void deleteById(Long id);

    @Query("select u from JwtRefreshToken u where u.token = :refreshToken")
    Optional<JwtRefreshToken> findJwtRefreshTokenByToken(String refreshToken);
}
