package dev.alim.habittracker.controller;

import dev.alim.habittracker.dto.HabitDto.HabitRequestDto;
import dev.alim.habittracker.dto.HabitDto.HabitResponseDto;
import dev.alim.habittracker.service.HabitService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService habitService;

    private static final Logger log = LoggerFactory.getLogger(HabitController.class);

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitResponseDto> getHabitById(
            @PathVariable("id") Long id
    ){
        log.info("Called getHabitById: id = {} ", id);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(habitService.getHabitById(id));

    }

    @GetMapping
    public ResponseEntity<List<HabitResponseDto>> getAllHabits(){
        log.info("Called getAllHabits");
        return ResponseEntity.status(HttpStatus.OK)
                .body(habitService.findAllHabits());
    }

    @PostMapping
    public ResponseEntity<HabitResponseDto> createHabit(
            @Valid @RequestBody HabitRequestDto habitToCreate  //концепцию понял, валидацию не забудь ;)
    ){
        log.info("Called createHabit");

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("created-header", "123")
                .body(habitService.createHabit(habitToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitResponseDto> updateHabit(
            @PathVariable("id") Long id,
            @Valid @RequestBody HabitRequestDto habitToUpdate
    ){
        log.info("Called updateHabit: id = {} , habitToUpdate = {}", id, habitToUpdate);

            var updatedHabit = habitService.updateHabitById(id, habitToUpdate);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(updatedHabit);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(
        @PathVariable("id") Long id
    ){
        log.info("Called deleteHabit: id = {}", id);

            habitService.deleteHabitById(id);
            return ResponseEntity.ok().build();

    }


}
