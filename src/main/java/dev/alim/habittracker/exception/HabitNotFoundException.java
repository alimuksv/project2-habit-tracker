package dev.alim.habittracker.exception;

public class HabitNotFoundException extends RuntimeException{

    public HabitNotFoundException(String info){
        super(info);
    }
}
