package dev.alim.habittracker.repository;

import dev.alim.habittracker.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitRepository extends JpaRepository<Habit,Long> {
}
