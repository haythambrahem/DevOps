package tn.esprit.se.pispring.Repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.se.pispring.entities.Portfolio;
import tn.esprit.se.pispring.entities.User;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findAppUserByEmail(String email);

    Optional<User> findOneByEmailAndPassword(String email, String password);
    User findByEmail(String email);
    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Long getUserIdByEmail(String email);
    User findByFirstName(String username);

    @Transactional
    Long deleteByFirstName(String username);

    @Query("select u from User u where u.portfolio = :portfolios and u.deleted = false")
    List<User> findUsers(Portfolio portfolios);

    @Query("select u from User u where (u.firstName like concat(:keyword, '%')" +
            " or u.lastName like concat(:keyword, '%') or u.email like concat(:keyword, '%'))" +
            " and u.deleted = false")
    List<User> searchUsers(String keyword);

    @Query("select u from User u where ( u.firstName like concat(:searchTerm, '%') or u.lastName like concat(:searchTerm, '%') or u.email like concat(:searchTerm, '%') )")
    Page<User> searchBy(PageRequest pageRequest, String searchTerm);

    long countByEnabledFalse();

}
