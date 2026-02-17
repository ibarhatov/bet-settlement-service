package com.ibarhatov.betsettlement.worker.repository;

import com.ibarhatov.betsettlement.worker.bet.entity.BetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<BetEntity, String> {
    List<BetEntity> findByEventIdAndEventWinnerId(String eventId, String eventWinnerId);
}
