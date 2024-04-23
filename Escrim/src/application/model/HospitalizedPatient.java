package application.model;

import java.time.LocalDate;

public class HospitalizedPatient {
    private int id;
    private int patientId;
    private String name;
    private int bedId;
    private LocalDate entryDate;
    private int expectedStay;

    public HospitalizedPatient(int id, int patientId, String name, int bedId, LocalDate entryDate, int expectedStay) {
        this.id = id;
        this.patientId = patientId;
        this.name = name;
        this.bedId = bedId;
        this.entryDate = entryDate;
        this.expectedStay = expectedStay;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBedId() {
        return bedId;
    }

    public void setBedId(int bedId) {
        this.bedId = bedId;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public int getExpectedStay() {
        return expectedStay;
    }

    public void setExpectedStay(int expectedStay) {
        this.expectedStay = expectedStay;
    }

    @Override
    public String toString() {
        return "HospitalizedPatient{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", name='" + name + '\'' +
                ", bedId=" + bedId +
                ", entryDate=" + entryDate +
                ", expectedStay=" + expectedStay +
                '}';
    }
}

