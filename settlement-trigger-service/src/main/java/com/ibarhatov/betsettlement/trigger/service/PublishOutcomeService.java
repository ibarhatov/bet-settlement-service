package com.ibarhatov.betsettlement.trigger.service;

import com.ibarhatov.betsettlement.contracts.messaging.EventOutcomeMessage;
import com.ibarhatov.betsettlement.trigger.application.EventOutcomePublisher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PublishOutcomeService {

    private final EventOutcomePublisher publisher;

    public void publish(EventOutcomeMessage message) {
        publisher.publish(message);
    }
}