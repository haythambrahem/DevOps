package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.se.pispring.entities.Consultant;
import tn.esprit.se.pispring.entities.MeetStatus;
import tn.esprit.se.pispring.entities.Meeting;

import java.util.Date;
import java.util.List;

public interface MeetRepository extends JpaRepository<Meeting,Long> {
    List<Meeting> findByMeetStatus(MeetStatus meetStatus);
    List<Meeting> findByConsultantAndMeettdateBetween(Consultant consultant, Date dateBegin, Date dateEnd);


    @Query("SELECT m FROM Meeting m WHERE m.meettdate< CURRENT_DATE()")
    List<Meeting> findMeetingsBeforeCurrentDate();



    @Query("SELECT COUNT(m) FROM Meeting m WHERE m.consultant.consultant_id = :consultantId AND m.meettdate BETWEEN :startDate AND :endDate")
    Long countTotalMeetings(@Param("consultantId") Long consultantId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT COUNT(m) FROM Meeting m WHERE m.consultant.consultant_id = :consultantId AND m.meetStatus = 'CANCELED' AND m.meettdate BETWEEN :startDate AND :endDate")
    Long countCanceledMeetings(@Param("consultantId") Long consultantId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT COUNT(m) FROM Meeting m WHERE m.consultant.consultant_id = :consultantId AND m.meetStatus = 'SUCCEDED' AND m.meettdate BETWEEN :startDate AND :endDate")
    Long countSucceededMeetings(@Param("consultantId") Long consultantId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    @Query("SELECT COUNT(m) FROM Meeting m WHERE m.consultant.consultant_id = :consultantId AND m.meetStatus = 'PASSED'AND m.first='oui' AND m.meettdate BETWEEN :startDate AND :endDate" )
    Long countPASSEDMeetingFailed(@Param("consultantId") Long consultantId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT COUNT(m) FROM Meeting m WHERE m.consultant.consultant_id = :consultantId AND m.meetStatus = 'PASSED'AND m.first='non' AND m.meettdate BETWEEN :startDate AND :endDate" )
    Long countMeetingAncienClient(@Param("consultantId") Long consultantId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT COUNT(m) FROM Meeting m WHERE m.consultant.consultant_id = :consultantId AND m.meetStatus = 'PASSED' AND m.meettdate BETWEEN :startDate AND :endDate")
    Long passsedMeetingstot(@Param("consultantId") Long consultantId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
