package com.dominik.priceaggregator.repository;

import com.dominik.priceaggregator.entity.HourlyCryptoHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HourlyCryptoHistoryRepository extends JpaRepository<HourlyCryptoHistoryEntity, Integer> {

    Optional<List<HourlyCryptoHistoryEntity>> findAllByDate(LocalDateTime date);
    Optional<List<HourlyCryptoHistoryEntity>> findAllByDateBetween(LocalDateTime from,LocalDateTime to);

}
