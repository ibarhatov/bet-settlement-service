package com.ibarhatov.betsettlement.contracts.messaging;

public record EventOutcomeMessage(String eventId, String eventName, String eventWinnerId) {
}
