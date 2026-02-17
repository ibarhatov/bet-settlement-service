package com.ibarhatov.betsettlement.trigger.application;

import com.ibarhatov.betsettlement.contracts.messaging.EventOutcomeMessage;

public interface EventOutcomePublisher {
    void publish(EventOutcomeMessage message);
}
