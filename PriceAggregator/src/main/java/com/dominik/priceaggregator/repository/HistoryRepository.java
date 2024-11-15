package com.dominik.priceaggregator.repository;

import com.dominik.priceaggregator.dto.CryptoPriceDto;
import com.dominik.priceaggregator.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity,Integer> {


    List<HistoryEntity> findAllByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
