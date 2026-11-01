package dev.alim.habittracker;

import dev.alim.habittracker.dto.RecordDto.RecordRequestDto;
import dev.alim.habittracker.entity.Habit;
import dev.alim.habittracker.entity.Record;
import dev.alim.habittracker.repository.HabitRepository;
import dev.alim.habittracker.repository.RecordRepository;
import dev.alim.habittracker.service.RecordService;
import dev.alim.habittracker.service.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StatsServiceTest {

    @Autowired
    private StatsService statsService;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RecordService recordService;


    @Test
    void checkCorrectStreakConsecutiveDaysTest(){

        LocalDate today = LocalDate.now();

        Habit habit = new Habit(
                null,
                "learn java",
                "every day",
                2
        );

        habit = habitRepository.saveAndFlush(habit);

        recordRepository.saveAllAndFlush(List.of(
                new Record(null, habit, today),
                new Record(null, habit, today.minusDays(1)),
                new Record(null, habit, today.minusDays(2))
                )
        );

        long actual = statsService.calculateCurrentStreak(habit.getId());

        assertEquals(3, actual);
    }

    @Test
    void checkInterruptStreakTest(){


        LocalDate today = LocalDate.now();

        Habit habit = new Habit(
                null,
                "learn java",
                "every day",
                2
        );

        habit = habitRepository.saveAndFlush(habit);

        recordRepository.saveAllAndFlush(List.of(
                        new Record(null, habit, today),
                        new Record(null, habit, today.minusDays(2)),
                        new Record(null, habit, today.minusDays(3))
                )
        );

        long actual = statsService.calculateCurrentStreak(habit.getId());

        assertEquals(1, actual);
    }

    @Test
    void checkBestStreakTest(){

        LocalDate today = LocalDate.now();

            Habit habit = new Habit(
                    null,
                    "learn boot",
                    "at least 4 hours",
                    4
            );

        habit = habitRepository.saveAndFlush(habit);

        recordRepository.saveAllAndFlush(List.of(
                new Record(null, habit, today),
                new Record(null, habit, today.minusDays(3)),
                new Record(null, habit, today.minusDays(4))
                )
        );

        recordService.createRecord(
                new RecordRequestDto(habit.getId(),today.minusDays(2))
        );

        Habit updatedHabit = habitRepository
                .findById(habit.getId()).orElseThrow();

        assertEquals(3, updatedHabit.getBestStreak());
    }

    @Test
    void checkCompletionRateTest(){

        LocalDate today = LocalDate.now();

        Habit habit = new Habit(
                null,
                "learn boot",
                "at least 4 hours",
                4
        );

        habit.setCreatedAt(today.minusDays(6));

        habit = habitRepository.saveAndFlush(habit);

        recordRepository.saveAllAndFlush(List.of(
                        new Record(null, habit, today),
                        new Record(null, habit, today.minusDays(1)),
                        new Record(null, habit, today.minusDays(2)),
                        new Record(null, habit, today.minusDays(4)),
                        new Record(null, habit, today.minusDays(6))
                )
        );


        double completionRate = statsService.calculateCompletionRate(habit.getId());

        assertEquals(71.4, completionRate, 0.001);

    }



}
