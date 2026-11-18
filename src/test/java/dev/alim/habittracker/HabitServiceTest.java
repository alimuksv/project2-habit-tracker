package dev.alim.habittracker;


import dev.alim.habittracker.dto.HabitDto.HabitRequestDto;
import dev.alim.habittracker.dto.HabitDto.HabitResponseDto;
import dev.alim.habittracker.entity.Habit;
import dev.alim.habittracker.exception.HabitNotFoundException;
import dev.alim.habittracker.repository.HabitRepository;
import dev.alim.habittracker.service.HabitService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class HabitServiceTest {

    @Autowired
    private HabitService habitService;

    @Autowired
    private HabitRepository habitRepository;

    @Test
    void createHabitTest(){
    //Arrange - Подготовить
        HabitRequestDto request =
                new HabitRequestDto("Learn java",
                                "every day",
                                    3);
    //Act - Выполнить
        HabitResponseDto createdHabit = habitService.createHabit(request);

    //Assert - Проверка результата
        assertNotNull(createdHabit.id());
        assertEquals("Learn java", createdHabit.name());
    }


    @Test
    void getHabitByIdTest(){

        Habit habit = new Habit(
                null,
                "Learn java",
                "every day",
                3
        );

        Habit savedHabit = habitRepository.save(habit);

        HabitResponseDto foundedHabit = habitService.getHabitById(savedHabit.getId());

        assertNotNull(foundedHabit);
        assertEquals(savedHabit.getId(), foundedHabit.id());
        assertEquals("Learn java", foundedHabit.name());

    }

    @Test
    void habitNotFoundByIdTest(){
        assertThrows(HabitNotFoundException.class,
                () -> habitService.getHabitById(999L));
    }

    @Test
    void updateHabitPreservesCreatedAtAndBestStreakTest(){
        LocalDate originalCreatedAt = LocalDate.now().minusDays(10);
        Habit habit = new Habit(null, "Learn java", "every day", 2);
        habit.setCreatedAt(originalCreatedAt);
        habit.setBestStreak(7);
        Habit savedHabit = habitRepository.saveAndFlush(habit);

        HabitRequestDto updateRequest = new HabitRequestDto("Learn Spring", "twice a day", 4);
        HabitResponseDto updated = habitService.updateHabitById(savedHabit.getId(), updateRequest);

        assertEquals("Learn Spring", updated.name());
        assertEquals("twice a day", updated.description());
        assertEquals(4, updated.target());
        assertEquals(originalCreatedAt, updated.createAt());

        Habit reloaded = habitRepository.findById(savedHabit.getId()).orElseThrow();
        assertEquals(originalCreatedAt, reloaded.getCreatedAt());
        assertEquals(7, reloaded.getBestStreak());
    }
}
