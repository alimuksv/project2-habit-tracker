package dev.alim.habittracker.dto.HabitDto;

import java.time.LocalDate;


public record HabitResponseDto(

    Long id,

    String name,

    String description,

    Integer target,

    LocalDate createAt
)

{ }
