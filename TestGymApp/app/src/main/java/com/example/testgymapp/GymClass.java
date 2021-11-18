package com.example.testgymapp;

import java.util.ArrayList;

public class GymClass {
    private String className;
    private String description;
    private String classTime;
    private int maximumCapacity;
    private String day;
    private String difficulty;
    private User instructor;
    private ArrayList<User> members;
    private int numberOfUsers;

    public GymClass(){}
    public GymClass(String className, String description){
        this.className = className;
        this.description = description;
    }
    public GymClass(String className, String description, String classTime, int maximumCapacity, String day, String difficulty, User instructor){
            this.className = className;
            this.description = description;
            this.classTime = classTime;
            this.maximumCapacity = maximumCapacity;
            this.day = day;
            this.difficulty = difficulty;
            this.instructor = instructor;
            members = new ArrayList<User>();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public User getInstructor() {
        return instructor;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public boolean addMember(User user) {
        if (numberOfUsers<maximumCapacity){
            members.add(user);
            numberOfUsers+=1;
            return true;
        }
        return false;
    }

    public boolean removeMember (User user){
        if (numberOfUsers>0){
            members.remove(user);
            numberOfUsers-=1;
            return true;
        }
        return false;
    }
}
