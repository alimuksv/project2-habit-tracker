package dev.alim.habittracker.entity;

import java.time.LocalDateTime;


public class Habit {

    private final Long id;
    private final String name;
    private final String description;
    private final Integer target;
    private final LocalDateTime createdAt;


    public Habit(Long id, String name, String description, Integer target ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.target = target;
        this.createdAt = LocalDateTime.now();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


}
