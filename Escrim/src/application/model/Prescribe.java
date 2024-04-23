package application.model;

import java.time.LocalDate;

public class Prescribe {
    private int id;
    private int patientId;
    private int medId;
    private LocalDate prescDate;
    private String comment;
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
	public int getMedId() {
		return medId;
	}
	public void setMedId(int medId) {
		this.medId = medId;
	}
	public LocalDate getPrescDate() {
		return prescDate;
	}
	public void setPrescDate(LocalDate prescDate) {
		this.prescDate = prescDate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Prescribe(int id, int patientId, int medId, LocalDate prescDate, String comment) {
		super();
		this.id = id;
		this.patientId = patientId;
		this.medId = medId;
		this.prescDate = prescDate;
		this.comment = comment;
	}
	public Prescribe() {
		super();
	}

    // Constructor, getters, and setters
    
    
}