package dev.alim.habittracker.service;

import dev.alim.habittracker.dto.HabitDto.HabitRequestDto;
import dev.alim.habittracker.dto.HabitDto.HabitResponseDto;
import dev.alim.habittracker.entity.Habit;
import dev.alim.habittracker.exception.HabitNotFoundException;

import dev.alim.habittracker.repository.HabitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class HabitService{

    private static final Logger log = LoggerFactory.getLogger(HabitService.class);


    private final HabitRepository repository;

    public HabitService(HabitRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public HabitResponseDto getHabitById(Long id) {
        log.info("Called getHabitById: id {}", id );
//        if(!habitMap.containsKey(id)){
//            throw new HabitNotFoundException("Habit not found by id = " + id);
//        }
        Habit habitEntity = repository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("Habit not found by id = " + id));

        log.info("Successfully getHabitById: id = {}", id);
        return habitToResponseDto(habitEntity);
    }

    @Transactional(readOnly = true)
    public List<HabitResponseDto> findAllHabits(){
        log.info("Called findAllHabits");

    List<HabitResponseDto> habits = repository.findAll()
            .stream()
            .map(habit -> habitToResponseDto(habit))
            .toList();

    log.info("Successfully findAllHabits");

    /*List<HabitResponseDto> habits = habitMap.values()
                                    .stream()
                                    .map(habit -> habitToResponseDto(habit))
                                    .toList();*/
    return habits;
    }

    @Transactional
    public HabitResponseDto createHabit(HabitRequestDto habitToCreate){

        log.info("Called createHabit");

        var newHabit = new Habit(
                null,                   //idCounter.incrementAndGet() важно запомнить
                habitToCreate.name(),
                habitToCreate.description(),
                habitToCreate.target()
        );

        var savedEntity = repository.save(newHabit);

        log.info("Successfully createHabit");
                                                ///savedEntity, а не newHabit т.к после сохранения,
        return habitToResponseDto(savedEntity); /// бд сама дает идентификатор(id) и мы корректно возвращаем результат
    }

    @Transactional
    public HabitResponseDto updateHabitById(
            Long id,
            HabitRequestDto habitToUpdate
    ){
        log.info("Called updateHabitById: id = {}", id);

//        if (!repository.existsById(id)){
//            throw new HabitNotFoundException("Habit not found by id = " + id);
//        } теперь не нужен, т.к. .findBiId() возвращает тип Optional<>

        //Сохранил старый habit чтоб обращаться за информацией
        var habit = repository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("Habit not found by id = " + id));

        var habitToSave = new Habit(
                habit.getId(),
                habitToUpdate.name(),
                habitToUpdate.description(),
                habitToUpdate.target()
        );

        var updatedHabit = repository.save(habitToSave); //почему не habitToSave просто соханить, т.к. метод .save()
                                                         //обнавленную сущность возвращает

        log.info("Successfully deleteHabitById: id = {}, updated habit = {}", id, habitToUpdate);

        return habitToResponseDto(updatedHabit);
    }

    @Transactional
    public void deleteHabitById(Long id){
        log.info("Called deleteHabitById: id = {}", id);

        if (!repository.existsById(id)){
            throw new HabitNotFoundException("Habit not found by id = " + id);
        }
        log.info("Successfully deleteHabitById: id = {}", id);
        repository.deleteById(id);

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
