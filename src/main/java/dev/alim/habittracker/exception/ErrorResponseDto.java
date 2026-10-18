package dev.alim.habittracker.exception;


import java.time.LocalDateTime;

public record ErrorResponseDto(
        //человеко-читаемая ошибка
        String message,
        //подробная инфа(больше для себя)
        String detailedMessage,

        LocalDateTime errorTime
)

{ }
