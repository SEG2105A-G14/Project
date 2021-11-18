package com.example.testgymapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class JUnitTestGymClass {
    @Test
    public void testGymClass(){
        // Initialize the gym class for badminton
        GymClass badminton = new GymClass("Badminton", "Enjoy playing badminton with your friends.");

        // Verify the information for the gym class of badminton matches.
        assertEquals(badminton.getClassName(), "Badminton");
        String description = "Enjoy playing badminton with your friends.";
        assertEquals(badminton.getDescription(), description);

        // Verify the setter classes are functional
        badminton.setClassName("Cricket");
        assertEquals(badminton.getClassName(), "Cricket");
        badminton.setDescription("Have fun with your friends in a game of cricket.");
        String description2 = "Have fun with your friends in a game of cricket.";
        assertEquals(badminton.getDescription(), description2);

        // Initialize a more complex gym class for running
        User instructor = new User("Bob", "bob@gmail.com");
        GymClass running = new GymClass("Running", "Run and improve your stamina",
                "1:30pm", 3, "Monday", "Medium", instructor);

        // Verify the information for the gym class of running matches.
        assertEquals(running.getClassTime(), "1:30pm");
        assertEquals(running.getMaximumCapacity(), 3);
        assertEquals(running.getDay(), "Monday");
        assertEquals(running.getInstructor().getName(), instructor.getName());

        // Verify the setters classes are functional for the complex gym class.
        running.setClassTime("2:00pm");
        assertEquals(running.getClassTime(), "2:00pm");
        running.setMaximumCapacity(2);
        assertEquals(running.getMaximumCapacity(), 2);
        running.setDay("Friday");
        assertEquals(running.getDay(), "Friday");
        User newInstructor = new User("Johnathan", "johnathan@outlook.com");
        running.setInstructor(newInstructor);
        assertEquals(running.getInstructor().getEmail(), newInstructor.getEmail());

        // Initialize user who will join the gym class.
        User user1 = new User("Joe", "joe@yahoo.com");
        User user2 = new User("John", "john@outlook.com");
        User user3 = new User("Daniel", "daniel@gmail.com");

        // Add the users except for Daniel since he cannot join when the maximum capacity is 2.
        running.addMember(user1);
        running.addMember(user2);
        boolean b1 = running.addMember(user3);
        assertTrue(b1 == false);

        // Remove a user and see if Daniel can join the gym class.
        running.removeMember(user2);
        boolean b2 = running.addMember(user3);
        assertTrue(b2 == true);

        // Use the getMembers() function and check to see if Daniel's name is there.
        assertEquals(running.getMembers().get(1).getName(), "Daniel");


        // Verify that the empty GymClass class is functional.
        GymClass emptyGC = new GymClass();
        assertNotEquals(emptyGC, null);

    }
}
