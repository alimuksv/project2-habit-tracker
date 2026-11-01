package dev.alim.habittracker.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Table(name = "habits")
@Entity
public class Habit {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "target")
    private Integer target;

    @Column(name = "createdAt")
    private LocalDate createdAt;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Record> records;

    @Column(name = "best_streak", nullable = false)
    private int bestStreak = 0;

    public Habit() {

    }

    public Habit(Long id, String name, String description, Integer target ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.target = target;
        this.createdAt = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getTarget() {
        return target;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Integer getBestStreak(){
        return bestStreak;
    }

    public void setBestStreak(Integer newBestStreak){
        bestStreak = newBestStreak;
    }

    public void setCreatedAt(LocalDate newCreatedAt){
        createdAt = newCreatedAt;
    }


}
