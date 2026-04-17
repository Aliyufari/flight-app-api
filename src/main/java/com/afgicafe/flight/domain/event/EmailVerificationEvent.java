package com.afgicafe.flight.domain.event;

public record EmailVerificationEvent (
        String name,
        String email,
        String token
) {}
