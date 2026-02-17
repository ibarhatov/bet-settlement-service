package com.ibarhatov.betsettlement.worker.service;

import com.ibarhatov.betsettlement.contracts.messaging.BetSettlementMessage;
import com.ibarhatov.betsettlement.contracts.messaging.EventOutcomeMessage;
import com.ibarhatov.betsettlement.worker.bet.entity.BetEntity;
import com.ibarhatov.betsettlement.worker.infra.rocketmq.BetSettlementProducer;
import com.ibarhatov.betsettlement.worker.repository.BetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BetSettlementService {

    private final BetRepository betRepository;
    private final BetSettlementProducer betSettlementProducer;

    @Transactional(readOnly = true)
    public void handleOutcome(EventOutcomeMessage outcome) {
        String eventId = outcome.eventId();
        String eventWinnerId = outcome.eventWinnerId();

        List<BetEntity> bets = betRepository.findByEventIdAndEventWinnerId(eventId, eventWinnerId);

        if (bets.isEmpty()) {
            log.info("No bets found for settlement. eventId={}, eventWinnerId={}", eventId, eventWinnerId);
            return;
        }

        for (BetEntity bet : bets) {
            BetSettlementMessage msg = new BetSettlementMessage(
                    bet.getBetId(),
                    bet.getUserId(),
                    bet.getEventId(),
                    bet.getEventMarketId(),
                    bet.getEventWinnerId(),
                    bet.getBetAmount()
            );

            betSettlementProducer.send(msg);
        }
    }
}
