package dev.alim.habittracker.dto.StatsDto;

import java.time.LocalDate;
import java.util.List;

public record DailyStatsResponseDto(
        LocalDate date,

        List<DailyHabitDto> habits
)
{ }
