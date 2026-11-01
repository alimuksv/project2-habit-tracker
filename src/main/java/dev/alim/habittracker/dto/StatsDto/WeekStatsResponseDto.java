package dev.alim.habittracker.dto.StatsDto;

import java.time.LocalDate;
import java.util.List;

public record WeekStatsResponseDto(
        LocalDate startDate,

        LocalDate endDate,

        List<WeekHabitDto> week
) { }
