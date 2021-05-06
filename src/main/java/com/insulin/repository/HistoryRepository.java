package com.insulin.repository;

import com.insulin.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HistoryRepository extends JpaRepository<History, Long> {
    void deleteByCreationDateBetween(LocalDate from, LocalDate to);
}
