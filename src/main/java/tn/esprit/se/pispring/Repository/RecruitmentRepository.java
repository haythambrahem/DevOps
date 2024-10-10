package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.se.pispring.entities.Recruitment;
import tn.esprit.se.pispring.entities.RecruitmentStatus;

import java.util.List;
import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment,Long> {
    Optional<Recruitment> findByPostTitle(String postTitle);


    @Query("SELECT r.recruitmentStatus, COUNT(r) FROM Recruitment r WHERE YEAR(r.recruitmentDate) = :year AND r.recruitmentStatus IN (:statuses) GROUP BY r.recruitmentStatus")
    List<Object[]> countRecruitmentStatusByYear(int year, List<RecruitmentStatus> statuses);

    @Query("SELECT r.postTitle, COUNT(r) " +
            "FROM Recruitment r " +
            "WHERE YEAR(r.recruitmentDate) = :year " +
            "AND r.postTitle = :postTitle " +
            "GROUP BY r.postTitle")
    List<Object[]> countRecruitmentStatusByYearAndPostTitle(@Param("year") int year, @Param("postTitle") String postTitle);
}

