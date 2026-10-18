package dev.alim.habittracker.dto;

import java.time.LocalDateTime;

public record HabitResponseDto(

    Long id,

    String name,

    String description,

    Integer target,

    LocalDateTime createAt
)

{ }
