package dev.alim.habittracker.repository;

import dev.alim.habittracker.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record,Long> {

    List<Record> findByHabit_Id(Long habitId);

    boolean existsByHabit_IdAndDate(Long habitId, LocalDate date);

}
