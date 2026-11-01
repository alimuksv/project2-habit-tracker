package dev.alim.habittracker.dto.StatsDto;

import java.time.LocalDate;

public record WeekHabitDto(
        LocalDate date,
        Long completedCount,
        Integer totalCount
) { }
