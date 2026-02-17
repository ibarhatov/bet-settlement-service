package com.ibarhatov.betsettlement.trigger.controller;

import com.ibarhatov.betsettlement.contracts.api.EventOutcomeRequest;
import com.ibarhatov.betsettlement.contracts.messaging.EventOutcomeMessage;
import com.ibarhatov.betsettlement.trigger.service.PublishOutcomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/events")
@Slf4j
public class EventOutcomeController {

    private final PublishOutcomeService service;

    @PostMapping("/outcome")
    public ResponseEntity<Void> publish(@Valid @RequestBody EventOutcomeRequest request) {
        var msg = new EventOutcomeMessage(request.eventId(), request.eventName(), request.eventWinnerId());
        service.publish(msg);
        log.info("Published to Kafka: {}", msg);
        return ResponseEntity.accepted().build();
    }
}
