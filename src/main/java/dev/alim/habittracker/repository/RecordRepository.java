package dev.alim.habittracker.repository;

import dev.alim.habittracker.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record,Long> {

    List<Record> findByHabit_Id(Long habitId);

    boolean existsByHabit_IdAndDate(Long habitId, LocalDate date);

    List<Record> findAllByDate(LocalDate date);

    @Query("""
        SELECT r
            FROM Record r
            WHERE r.date BETWEEN :startDate 
            AND :endDate
            ORDER BY r.date ASC
    """)
    List<Record> findAllRecordsByPeriod(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    List<Record> findByHabit_IdOrderByDateDesc(Long habitId);   //В аннотации r.habit , а не r.habit_id т.к.
                                                                // DataJpa работает с сущностями java, а не БД

}
