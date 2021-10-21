package com.example.testgymapp;

import java.util.ArrayList;

public class Instructor extends User{
    private ArrayList<GymClass> classes;

    public Instructor(){}
    public Instructor(String name, String email){
        super(name, email);
        classes = new ArrayList<>();
        super.setRole("Instructor");
    }

    public  boolean addClass(GymClass gymClass){
        return classes.add(gymClass);
    }
    public boolean removeClass(GymClass gymClass){
        return classes.remove(gymClass);
    }

    public ArrayList<GymClass> getClasses() {
        return classes;
    }
}
