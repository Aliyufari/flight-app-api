package com.afgicafe.flight.event.publisher;

import com.afgicafe.flight.domain.event.EmailVerificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailEventPublisher {
    private final ApplicationEventPublisher publisher;

    public void  publish (EmailVerificationEvent event) {
        publisher.publishEvent(event);
    }
}
