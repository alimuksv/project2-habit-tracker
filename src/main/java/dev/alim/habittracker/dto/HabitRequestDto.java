package dev.alim.habittracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record HabitRequestDto(
    @NotBlank(message = "Name is required")
    String name,

    String description,

    @Positive(message = "Target must be a positive number")
    Integer target
)
{ }
