package dev.alim.habittracker.controller;


import dev.alim.habittracker.dto.RecordDto.RecordRequestDto;
import dev.alim.habittracker.dto.RecordDto.RecordResponseDto;
import dev.alim.habittracker.service.RecordService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;
    private static final Logger log = LoggerFactory.getLogger(RecordService.class);

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping
    public ResponseEntity<List<RecordResponseDto>> getAllRecords(){
        log.info("Called getAllRecords");

        return ResponseEntity.status(HttpStatus.OK).body(recordService.findAllRecords());
    }

    @GetMapping("/{habitId}")
    public ResponseEntity<List<RecordResponseDto>> getRecordsById(
            @PathVariable Long habitId
    ){
        log.info("Called getRecordById: id = {}", habitId);
        return ResponseEntity.status(HttpStatus.OK).body(recordService.findRecordsByHabitId(habitId)); // в сервисе сделать методы
    }

    @PostMapping()
    public ResponseEntity<RecordResponseDto> postRecord(
            @Valid @RequestBody RecordRequestDto record
    ){
        log.info("Called postRecord");
        return ResponseEntity.status(HttpStatus.CREATED).body(recordService.createRecord(record));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecordResponseDto> updateRecord(
            @PathVariable("id") Long id,
            @Valid @RequestBody RecordRequestDto recordToUpdate
    ){
        log.info("Called updateRecord: id = {} ", id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(recordService.updateRecord(id, recordToUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(
            @PathVariable("id") Long id
    ){
        log.info("Called deleteRecord: id = {}", id);
        recordService.deleteRecord(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
