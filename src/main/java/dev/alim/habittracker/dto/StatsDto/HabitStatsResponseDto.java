package dev.alim.habittracker.dto.StatsDto;

public record HabitStatsResponseDto(

        Integer currentStreak,

        Integer bestStreak,

        Double completionRate,

        Integer totalCompletions
) {
}
