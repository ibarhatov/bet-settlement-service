package com.ibarhatov.betsettlement.trigger.infra.kafka;

import com.ibarhatov.betsettlement.contracts.messaging.EventOutcomeMessage;
import com.ibarhatov.betsettlement.trigger.application.EventOutcomePublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventOutcomePublisher implements EventOutcomePublisher {

    private final KafkaTemplate<String, EventOutcomeMessage> kafkaTemplate;
    private final String topic;

    public KafkaEventOutcomePublisher(
            KafkaTemplate<String, EventOutcomeMessage> kafkaTemplate,
            @Value("${app.kafka.topic}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(EventOutcomeMessage message) {
        kafkaTemplate.send(topic, message.eventId(), message);
    }
}
