package com.ibarhatov.betsettlement.contracts.messaging;

import java.math.BigDecimal;

public record BetSettlementMessage(
        String betId,
        String userId,
        String eventId,
        String eventMarketId,
        String eventWinnerId,
        BigDecimal betAmount
) {
}
