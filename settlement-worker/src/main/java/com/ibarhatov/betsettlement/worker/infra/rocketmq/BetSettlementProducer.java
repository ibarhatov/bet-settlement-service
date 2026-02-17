package com.ibarhatov.betsettlement.worker.infra.rocketmq;

import com.ibarhatov.betsettlement.contracts.messaging.BetSettlementMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BetSettlementProducer {

    private static final String DESTINATION = "bet-settlements";

    private final RocketMQTemplate rocketMQTemplate;

    public void send(BetSettlementMessage message) {
        rocketMQTemplate.convertAndSend(DESTINATION, message);
        log.info("RocketMQ[bet-settlements] -> sent betId={}", message.betId());
    }
}
