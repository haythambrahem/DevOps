package tn.esprit.se.pispring.Config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);
    @Before("execution(* tn.esprit.se.pispring.Service.NotificationService.sendScheduledNotifications(..))")
    public void logBeforeSendScheduledNotifications(JoinPoint joinPoint) {
        logger.info("Executing scheduled method: {}", joinPoint.getSignature().toShortString());
    }

}
