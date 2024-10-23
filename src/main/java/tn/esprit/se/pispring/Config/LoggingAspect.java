package tn.esprit.se.pispring.Config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j  // This annotation provides the 'logger' variable
public class LoggingAspect {

    @Before("execution(* tn.esprit.se.pispring.Service.NotificationService.sendScheduledNotifications(..))")
    public void logBeforeSendScheduledNotifications(JoinPoint joinPoint) {
        // The logger is provided by Lombok, and using parameterized logging
        log.info("Executing scheduled method: {}", joinPoint.getSignature().toShortString());
    }
}
