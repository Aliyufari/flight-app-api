package com.afgicafe.flight.event.listener;

import com.afgicafe.flight.domain.event.EmailVerificationEvent;
import com.afgicafe.flight.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailVerificationListener {
    private final EmailService emailService;

    @Async("emailExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle (EmailVerificationEvent event) {
        try {
            emailService.sendEmailVerificationMessage(
                    event.name(),
                    event.email(),
                    event.token()
            );
        } catch (Exception e) {
            log.error("Email sending failed", e);
        }
    }
}
