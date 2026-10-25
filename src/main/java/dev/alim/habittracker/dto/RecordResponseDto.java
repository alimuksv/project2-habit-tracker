package dev.alim.habittracker.dto;

import java.time.LocalDate;

public record RecordResponseDto(

    Long id,
    Long habit_id,
    LocalDate date

)
{ }
