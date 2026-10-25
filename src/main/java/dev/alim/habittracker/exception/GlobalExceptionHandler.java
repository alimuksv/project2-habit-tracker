package dev.alim.habittracker.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneralServerException(
            Exception e
    ){
        log.error("Handle GeneralServerException ", e);

        var errorDto = new ErrorResponseDto(
                "Server error",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }


    @ExceptionHandler(HabitNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleHabitNotFoundException(
            HabitNotFoundException e
    ){
        log.error("Handle HabitNotFoundException: ",  e);

        var errorDto = new ErrorResponseDto(
                "Habit not found",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(
            IllegalArgumentException e
    ){
        log.error("Handle IllegalArgumentException ", e);

        var errorDto = new ErrorResponseDto(
                "Not correct information",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(
            IllegalStateException e
    ){
        log.error("Handle IllegalArgumentException ", e);

        var errorDto = new ErrorResponseDto(
                "Record already exist",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }

    @ExceptionHandler(exception = {
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorResponseDto> handleHttpMessageException(
            HttpMessageNotReadableException e
    ){
        log.error("Handle httpMessageException ", e);
        var errorDto = new ErrorResponseDto(
                "Not correct input",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        log.error("Handle methodArgumentNotValidException ", e);
        var errorDto = new ErrorResponseDto(
                "Not correct input",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

}
