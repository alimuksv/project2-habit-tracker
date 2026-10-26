package dev.alim.habittracker;


import dev.alim.habittracker.dto.HabitRequestDto;
import dev.alim.habittracker.dto.HabitResponseDto;
import dev.alim.habittracker.entity.Habit;
import dev.alim.habittracker.exception.HabitNotFoundException;
import dev.alim.habittracker.repository.HabitRepository;
import dev.alim.habittracker.service.HabitService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
}
