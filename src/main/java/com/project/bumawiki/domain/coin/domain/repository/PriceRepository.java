package com.project.bumawiki.domain.coin.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bumawiki.domain.coin.domain.Price;

public interface PriceRepository extends JpaRepository<Price, Long> {

	@Query(value = "select * from price p order by p.started_time desc LIMIT 1", nativeQuery = true)
	Optional<Price> findTopOrderByStartedTime();

	@Query("select p from Price p order by p.startedTime")
	List<Price> findAllByOrderByStartedTime();

	default Price getRecentPrice() {
		return findTopOrderByStartedTime()
			.orElseGet(() ->
				save(new Price(1000000L))
			);
	}

	@Query("select p from Price p where p.startedTime >= :twoWeeksAgo order by p.startedTime asc")
	List<Price> findAllAfterStartedTime(LocalDateTime twoWeeksAgo);
}
