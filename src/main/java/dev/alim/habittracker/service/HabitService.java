package dev.alim.habittracker.service;

import dev.alim.habittracker.dto.HabitRequestDto;
import dev.alim.habittracker.dto.HabitResponseDto;
import dev.alim.habittracker.entity.Habit;
import dev.alim.habittracker.exception.HabitNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class HabitService{

    private static final Logger log = LoggerFactory.getLogger(HabitService.class);

    private final Map<Long, Habit> habitMap;
    private final AtomicLong idCounter;

    public HabitService() {
        this.habitMap = new HashMap<>();
        idCounter = new AtomicLong();
    }


    public HabitResponseDto getHabitById(Long id) {
        log.info("Called getHabitById: id {}", id );
        if(!habitMap.containsKey(id)){
            throw new HabitNotFoundException("Habit not found by id = " + id);
        }

        log.info("Successfully getHabitById: id = {}", id);
        return habitToResponseDto(habitMap.get(id));
    }

    public List<HabitResponseDto> findAllHabits(){
        log.info("Called findAllHabits");

    if(habitMap.isEmpty()){
        throw new NoSuchElementException("Still dont have a habits");
    }

    log.info("Successfully findAllHabits");

    List<HabitResponseDto> habits = habitMap.values()
                                    .stream()
                                    .map(habit -> habitToResponseDto(habit))
                                    .toList();
    return habits;
    }

    public HabitResponseDto createHabit(HabitRequestDto habitToCreate){

        log.info("Called createHabit");

        var newHabit = new Habit(
                idCounter.incrementAndGet(),
                habitToCreate.name(),
                habitToCreate.description(),
                habitToCreate.target()
        );

        log.info("Successfully createHabit");
        habitMap.put(newHabit.getId(), newHabit);

        return habitToResponseDto(newHabit);
    }

    public HabitResponseDto updateHabitById(
            Long id,
            HabitRequestDto habitToUpdate
    ){
        log.info("Called updateHabitById: id = {}", id);
        if (!habitMap.containsKey(id)){
            throw new HabitNotFoundException("Habit not found by id = " + id);
        }

        //Сохранил старый habit чтоб обращаться за информацией
        var habit = habitMap.get(id);

        var updatedHabit = new Habit(
                habit.getId(),
                habitToUpdate.name(),
                habitToUpdate.description(),
                habitToUpdate.target()
        );
        habitMap.put(habit.getId(), updatedHabit);

        log.info("Successfully deleteHabitById: id = {}, updated habit = {}", id, habitToUpdate);

        return habitToResponseDto(updatedHabit);
    }

    public void deleteHabitById(Long id){
        log.info("Called deleteHabitById: id = {}", id);
        if (!habitMap.containsKey(id)){
            throw new HabitNotFoundException("Habit not found by id = " + id);
        }
        log.info("Successfully deleteHabitById: id = {}", id);
        habitMap.remove(id);

    }

    //Быстрое преобразование Entity в DTO
    private HabitResponseDto habitToResponseDto(Habit habit){

        return new HabitResponseDto(
                habit.getId(),
                habit.getName(),
                habit.getDescription(),
                habit.getTarget(),
                habit.getCreatedAt()
        );
    }
}
