package tn.esprit.se.pispring.eventListeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import tn.esprit.se.pispring.entities.User;
import tn.esprit.se.pispring.events.NewUserAddedEvent;
import tn.esprit.se.pispring.mailerConfig.MailUtility;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewUserAddedEventListener implements ApplicationListener<NewUserAddedEvent> {


    private final MailUtility mailUtility;

    @Override
    public void onApplicationEvent(NewUserAddedEvent event) {
        try {
            User newUser = event.getUser();
            String password = event.getDefaultPassword();
            String applicationUrl = event.getApplicationUrl();
            log.info("welcome {} {} please click the link below to set up your account : ", newUser.getFirstName(), newUser.getLastName());
            log.info("###################################################################");

            mailUtility.sendMail(newUser.getEmail(), "authentication information", "<h1 style=\"font-size: '1.5rem'; font-weight: 'bolder'; \">Welcome to Asserter</h1>" +
                    "<h2>click <a style=\"width: '60px'; height: '40px'; border-radius: '3px'; background-color='rgba(9, 40, 188, 0.62)'; color: 'white'\" href=\"http://localhost:4200/signin\" target=\"_blank\">here</a> to login</h2>" +
                    "your default password is: <span style=\"color: 'blue'\">" + password + ", use your email as a username</span> ");

            log.info("default password is: ==> {}", password);
            log.info(applicationUrl + "/auth/authenticate");
        }catch (Exception e){
            try {
                throw new Exception(e);
            } catch (Exception ex) {
                throw new RuntimeException("check the mail exceptions");
            }
        }

    }

}
