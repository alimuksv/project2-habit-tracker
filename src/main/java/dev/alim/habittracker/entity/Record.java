package dev.alim.habittracker.entity;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "records")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "habit_id")
    private Habit habit;
//    @Column(name = "habit_id")
//    private Long habitId;

    @Column(name = "date")
    private LocalDate date;




    public Record() {
    }

    public Record(Long id, Habit habit, LocalDate date) {
        this.id = id;
        this.habit = habit;
        this.date = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public Habit getHabit() {
        return habit;
    }

    public LocalDate getDate() {
        return date;
    }
}
