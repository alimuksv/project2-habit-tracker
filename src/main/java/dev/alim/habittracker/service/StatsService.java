package dev.alim.habittracker.service;


import dev.alim.habittracker.dto.StatsDto.*;
import dev.alim.habittracker.entity.Habit;
import dev.alim.habittracker.entity.Record;
import dev.alim.habittracker.repository.HabitRepository;
import dev.alim.habittracker.repository.RecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatsService {

    private static final Logger log = LoggerFactory.getLogger(StatsService.class);

    private final RecordRepository recordRepository;
    private final HabitRepository habitRepository;

    public StatsService(RecordRepository recordRepository, HabitRepository habitRepository) {
        this.recordRepository = recordRepository;
        this.habitRepository = habitRepository;
    }

    @Transactional(readOnly = true)
    public HabitStatsResponseDto getHabitStatsById(Long habitId){
        log.info("Called getHabitStatusById: id = {}", habitId);

        var currentStreak = calculateCurrentStreak(habitId);

        var habitStatsToResponse = new HabitStatsResponseDto(
                currentStreak,
                habitRepository.findById(habitId).get().getBestStreak(),
                calculateCompletionRate(habitId),
                recordRepository.findByHabit_Id(habitId).size()
        );
        log.info("Successfully got habits stats by id = {}", habitId);
        return habitStatsToResponse;
    }

    @Transactional(readOnly = true)
    public DailyStatsResponseDto getDailyStats(){
        log.info("Called getDailyStats in StatsService");

        LocalDate today = LocalDate.now();

        List<Habit> habits = habitRepository.findAll();
        List<Record> todayHabits = recordRepository.findAllByDate(today);

        //Set чтобы быстро и без повторов сложить все записи по id
        Set<Long> completedHabitsId = todayHabits.stream()
                .map(record -> record.getHabit().getId())
                .collect(Collectors.toSet());

        //Сложить все в DailyHabitDto чтобы потом просто положить в Response с today
        List<DailyHabitDto> dailyHabits = habits.stream()
                .map(habit -> new DailyHabitDto(

                        habit.getId(),
                        habit.getName(),
                        completedHabitsId.contains(habit.getId())

                        )).toList();

        // 3 Stream Api, я сигма -_-
        List<DailyHabitDto> sortedDailyHabits = dailyHabits.stream()
                .sorted(Comparator.comparing(DailyHabitDto::id)).toList();

        return new DailyStatsResponseDto(today,sortedDailyHabits);

    }

    @Transactional(readOnly = true)
    public WeekStatsResponseDto getWeeklyStats(){
        log.info("Called getWeeklyStats in StatsService");

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        var weekRecords = recordRepository.findAllRecordsByPeriod(startDate, endDate);

        Map<LocalDate, Long> completedByDate = weekRecords.stream()
                .collect(Collectors.groupingBy(
                        Record::getDate,
                        Collectors.counting()
                ));

        List<Habit> allHabits = habitRepository.findAll();

        List<WeekHabitDto> weeklyList = new ArrayList<>();

        for(LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)){

            int totalCount = 0;
            long completedCount = completedByDate.getOrDefault(date, 0L);

            for(Habit habit : allHabits){
                if(!habit.getCreatedAt().isAfter(date)){
                    totalCount++;
                }
            }

            weeklyList.add(new WeekHabitDto(
                    date,
                    completedCount,
                    totalCount
                    )
            );

        }

    return new WeekStatsResponseDto(startDate, endDate, weeklyList);

    }


    public double calculateCompletionRate(Long habitId){

        double amountOfCompletions = recordRepository.findByHabit_Id(habitId).size();
        LocalDate dayOfMakeHabit = habitRepository.findById(habitId).get().getCreatedAt();

        if (recordRepository.findByHabit_Id(habitId).isEmpty()){
            return 0;
        }

        double amountOfHabitDays = ChronoUnit.DAYS.between(
                dayOfMakeHabit
                ,
                LocalDate.now()) + 1 ; // +1 т.к. с датой создания включительно

        double rate = (amountOfCompletions / amountOfHabitDays) * 100;

        return Math.round(rate * 10.0) /10.0;

    }

    public int calculateCurrentStreak(Long habitId){

        List<dev.alim.habittracker.entity.Record> records = new ArrayList<>(
                recordRepository.findByHabit_IdOrderByDateDesc(habitId));

//        records.sort(
//                Comparator.comparing(dev.alim.habittracker.entity.Record::getDate).reversed()
//        );

        LocalDate currentDate = LocalDate.now();
        LocalDate dayBeforeCurrent = currentDate.minusDays(1);
        int streak = 0;

        if (records.isEmpty()){
            return streak;
        }
        if (!records.getFirst().getDate().equals(dayBeforeCurrent) &&
                !records.getFirst().getDate().equals(currentDate)){
            return streak;
        }
        else {

            log.info("Record dates: {}",
                    records.stream()
                            .map(Record::getDate)
                            .toList());


            LocalDate expectedDate = records.getFirst().getDate();
            for (Record record : records) {
                LocalDate actualDate = record.getDate();
                if (expectedDate.equals(actualDate)) {
                    streak++;
                    expectedDate = expectedDate.minusDays(1);
                } else {
                    break;
                }

            }
        }

        return streak;
    }

    public int calculateBestStreak(Long habitId) {

        List<Record> records = new ArrayList<>(
                recordRepository.findByHabit_IdOrderByDateDesc(habitId)
        );

        int currentRun = 1;
        int bestStreak = 1;

        if (records.isEmpty()) {
            return 0;
        } else {

            LocalDate previousDate = records.getFirst().getDate();

            for (int i = 1; i < records.size(); i++) {

                LocalDate actualDate = records.get(i).getDate();

                if (previousDate.minusDays(1).equals(actualDate)) {
                    currentRun++;
                }
                else{
                    currentRun = 1;
                }

                bestStreak = Math.max(bestStreak, currentRun);
                previousDate = actualDate;
            }

        }

        return bestStreak;
    }
}
