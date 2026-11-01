package dev.alim.habittracker.dto.RecordDto;

import java.time.LocalDate;

public record RecordResponseDto(

    Long id,
    Long habit_id,
    LocalDate date

)
{ }
