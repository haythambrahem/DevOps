package tn.esprit.se.pispring.Service;

import org.springframework.stereotype.Repository;
import tn.esprit.se.pispring.entities.Meeting;
import tn.esprit.se.pispring.entities.Product;

import java.util.List;

@Repository

public interface MeetInterface {
    List<Meeting> retrieveAllMeetings();

    Meeting addMeeting(Meeting meeting);

    Meeting updateMeeting(Meeting meeting);



    Meeting getMeetingById(Long meetId);

    void deleteMeeting(Long meetId);

    void validerMeeting( Long meetId) ;
    Meeting planifierMeeting(Meeting m, Long consultantId, Long userId) ;

  //  void refuserMeet ( Long meetId) ;
    void annulerMeet ( Long meetId) ;


}
