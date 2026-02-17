package com.ibarhatov.betsettlement.worker.infra.kafka;

import com.ibarhatov.betsettlement.contracts.messaging.EventOutcomeMessage;
import com.ibarhatov.betsettlement.worker.service.BetSettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventOutcomeListener {

    private final BetSettlementService betSettlementService;

    @KafkaListener(topics = "${app.kafka.topic}")
    public void onMessage(EventOutcomeMessage message) {
        betSettlementService.handleOutcome(message);
    }
}
