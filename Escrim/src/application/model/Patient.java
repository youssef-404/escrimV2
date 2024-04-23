package application.model;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private int id;
    private String Name;
    private int age;
    private String gender;
    private List<String> observations;
    private List<String> visitDates;

    public Patient(int id, String Name, int age, String gender) {
        this.id = id;
        this.Name = Name;
        this.age = age;
        this.gender = gender;
        this.observations = new ArrayList<>();
        this.visitDates = new ArrayList<>(); 
    }

 

    //Get & Set Observation :

    public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getName() {
		return Name;
	}



	public void setName(String name) {
		Name = name;
	}



	public int getAge() {
		return age;
	}



	public void setAge(int age) {
		this.age = age;
	}



	public String getGender() {
		return gender;
	}



	public void setGender(String gender) {
		this.gender = gender;
	}



	public List<String> getObservations() {
		return observations;
	}



	public void setObservations(List<String> observations) {
		this.observations = observations;
	}



	public List<String> getVisitDates() {
		return visitDates;
	}



	public void setVisitDates(List<String> visitDates) {
		this.visitDates = visitDates;
	}


	//Add & Get Observation :

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



	@Override
	public String toString() {
		return "Patient [id=" + id + ", Name=" + Name + ", age=" + age + ", gender=" + gender + ", observations="
				+ observations + ", visitDates=" + visitDates + "]";
	}

    
    
}
