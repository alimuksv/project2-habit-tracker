package dev.alim.habittracker.dto.RecordDto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RecordRequestDto(
        @NotNull(message = "habit id is required")
        Long habitId,

        @FutureOrPresent
        @NotNull(message = "date is required")
        LocalDate date
) { }
