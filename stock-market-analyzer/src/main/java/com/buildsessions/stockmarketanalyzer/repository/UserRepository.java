package com.buildsessions.stockmarketanalyzer.repository;

import com.buildsessions.stockmarketanalyzer.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findCustomUserByUsername(String username);

    boolean existsByMonitoredStockIdsContains(String monitoredStockIds);

}
