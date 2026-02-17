package com.ibarhatov.betsettlement.worker.bet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "bets", indexes = {
        @Index(name = "idx_bets_event_winner", columnList = "eventId,eventWinnerId")
})
public class BetEntity {

    @Id
    private String betId;
    private String userId;
    private String eventId;
    private String eventMarketId;
    private String eventWinnerId;
    private BigDecimal betAmount;
}
