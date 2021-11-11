package com.example.testgymapp;

import java.util.ArrayList;
import java.util.Date;

public class GymClass {

    private GymClassType gymClassType;
    private String startTime;
    private int duration;
    private int maximumCapacity;
    private String day;
    private String difficulty;
    private User instructor;
    private ArrayList<User> members;
    private int numberOfUsers;

    public GymClass(){}
    public GymClass(String startTime, int maximumCapacity, String day, String difficulty){
        this.startTime = startTime;
        this.maximumCapacity = maximumCapacity;
        this.day = day;
        this.difficulty = difficulty;
        members = new ArrayList<User>();
    }
    public GymClass(String startTime, int maximumCapacity, String day, String difficulty, User instructor){
            this.startTime = startTime;
            this.maximumCapacity = maximumCapacity;
            this.day = day;
            this.difficulty = difficulty;
            this.instructor = instructor;
            members = new ArrayList<User>();
    }

    public String getClassTime() {
        return startTime;
    }

    public void setClassTime(String classTime) {
        this.startTime = classTime;
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

    public GymClassType getGymClassType() {
        return gymClassType;
    }

    public void setGymClassType(GymClassType gymClassType) {
        this.gymClassType = gymClassType;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public boolean addMember(GymMember user) {
        if (numberOfUsers<=maximumCapacity){
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
