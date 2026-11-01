package dev.alim.habittracker.controller;

import dev.alim.habittracker.dto.StatsDto.DailyStatsResponseDto;
import dev.alim.habittracker.dto.StatsDto.HabitStatsResponseDto;
import dev.alim.habittracker.dto.StatsDto.WeekStatsResponseDto;
import dev.alim.habittracker.service.StatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StatsController {

    private static final Logger log = LoggerFactory.getLogger(StatsController.class);

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/habits/{id}/stats")
    public ResponseEntity<HabitStatsResponseDto> getHabitStats(
            @PathVariable("id") Long id
){
    log.info("Called getHabitStatus: id = {}", id);
    return ResponseEntity.status(HttpStatus.OK).body(statsService.getHabitStatsById(id));

}

    @GetMapping("/stats/daily")
    public ResponseEntity<DailyStatsResponseDto> getDailyStats(){
        log.info("Called getDailyStats");
        return ResponseEntity.status(HttpStatus.OK).body(statsService.getDailyStats());
    }

    @GetMapping("/stats/week")
    public ResponseEntity<WeekStatsResponseDto> getWeeklyStats(){
        log.info("Called getWeekStats");
        return ResponseEntity.ok().body(statsService.getWeeklyStats());
    }

}
