package dev.alim.habittracker.service;


import dev.alim.habittracker.dto.RecordRequestDto;
import dev.alim.habittracker.dto.RecordResponseDto;
import dev.alim.habittracker.entity.Habit;
import dev.alim.habittracker.entity.Record;
import dev.alim.habittracker.exception.HabitNotFoundException;
import dev.alim.habittracker.repository.HabitRepository;
import dev.alim.habittracker.repository.RecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {

    public static final Logger log = LoggerFactory.getLogger(RecordService.class);

    private final RecordRepository recordRepository;
    private final HabitRepository habitRepository;


    public RecordService(RecordRepository recordRepository, HabitRepository habitRepository){
        this.recordRepository = recordRepository;
        this.habitRepository = habitRepository;

    }

    public List<RecordResponseDto> findRecordsByHabitId(Long id){
        log.info("Called findRecordById: id = {}", id);

//        без проверки или неправильно работает метод репозитория
//        if(!recordRepository.existsById(id)){
//            throw new HabitNotFoundException("Record not found by id = " + id);
//        }

        List<RecordResponseDto> recordEntity = recordRepository.findByHabit_Id(id)
                        .stream().map(record -> recordToRecordResponseDto(record)).toList();

        log.info("Successfully called findRecordById: id = {} ", id);
        return recordEntity;
    }

    public List<RecordResponseDto> findAllRecords(){
        log.info("Called findAllRecords");

        List<RecordResponseDto> records = recordRepository.findAll().
                stream().map(record -> recordToRecordResponseDto(record) ).toList();

        return records;
    }

    public RecordResponseDto createRecord(RecordRequestDto record){
        log.info("Called createRecord");


        Habit habit = habitRepository.findById(record.habitId())
                .orElseThrow(() -> new HabitNotFoundException("Habit not found" ));

        boolean alreadyRecorded = recordRepository.existsByHabit_IdAndDate(
                record.habitId(),
                record.date()
        );

        if (alreadyRecorded){
            throw new IllegalStateException("Record already exist");
        }


        var recordToCreate = new Record(
                null,
                habit,
                record.date()
        );

        var savedRecord = recordRepository.save(recordToCreate);
        log.info("Successfully createRecord");

        return recordToRecordResponseDto(savedRecord);

    }

    public RecordResponseDto updateRecord(Long id, RecordRequestDto recordToUpdate){
        log.info("Called updateRecord: id = {} ", id);

        var oldRecord = recordRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("Habit not found by id = " + id));

        Habit habit = habitRepository.findById(recordToUpdate.habitId())
                .orElseThrow(() -> new HabitNotFoundException("Habit not found" ));

        var updatedRecord = new Record(
                oldRecord.getId(),
                habit,
                recordToUpdate.date()
        );

        var savedRecord = recordRepository.save(updatedRecord);
        log.info("Successfully update record: id = {}", id);

        return recordToRecordResponseDto(savedRecord);
    }

    public void deleteRecord(Long id){
        log.info("Called deleteRecord: id = {}", id);

        if (!recordRepository.existsById(id)){
            throw new HabitNotFoundException("Cant delete record by id = " + id);
        }
        log.info("Successfully delete record by id = " + id);

        recordRepository.deleteById(id);
    }






    private RecordResponseDto recordToRecordResponseDto(dev.alim.habittracker.entity.Record record){

        RecordResponseDto recordToResponse = new RecordResponseDto(
                record.getId(),
                record.getHabit().getId(),
                record.getDate()
                );
        return recordToResponse;
    }


}
