package application.model;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private List<String> observations;
    private List<String> visitDates;

    public Patient(int id, String firstName, String lastName, int age, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getAllObservations() {
        return observations;
    }

    public List<String> getAllVisitDates() {
        return visitDates;
    }

    //Get & Set Observation :

    public void addObservation(String observation, String visitDate) {
        observations.add(observation);
        visitDates.add(visitDate);
    }

    public String getObservation(String visitDate) {
        int index = visitDates.indexOf(visitDate);
        if (index != -1) {
            return observations.get(index);
        } else {
            return null; // Visit date not found
        }
    }

}
