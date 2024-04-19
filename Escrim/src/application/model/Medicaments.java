package application.model;

import java.time.LocalDate;

public class Medicaments {
	private int id;
    private String nom;
    private String dci;
    private String dosage;
    private LocalDate dlu;
    private String medClass;
    private int quantity;
    private int lot;
    
    
	public Medicaments(int id,String nom, String dci, String dosage, LocalDate dlu,int quantity, String medClass, int lot) {
		super();
		this.id = id;
		this.nom = nom;
		this.dci = dci;
		this.dosage = dosage;
		this.dlu = dlu;
		this.medClass = medClass;
		this.lot = lot;
		this.quantity= quantity;
	}
	



	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public String getNom() {
		return nom;
	}




	public void setNom(String nom) {
		this.nom = nom;
	}




	public String getDci() {
		return dci;
	}




	public void setDci(String dci) {
		this.dci = dci;
	}




	public String getDosage() {
		return dosage;
	}




	public void setDosage(String dosage) {
		this.dosage = dosage;
	}




	public String getDlu() {
		return dlu.toString();
	}




	public void setDlu(LocalDate dlu) {
		this.dlu = dlu;
	}




	public String getMedClass() {
		return medClass;
	}




	public void setMedClass(String medClass) {
		this.medClass = medClass;
	}




	public int getQuantity() {
		return quantity;
	}




	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}




	public int getLot() {
		return lot;
	}




	public void setLot(int lot) {
		this.lot = lot;
	}




	@Override
	public String toString() {
		return "Medicaments [id=" + id + ", nom=" + nom + ", dci=" + dci + ", dosage=" + dosage + ", dlu=" + dlu
				+ ", medClass=" + medClass + ", quantity=" + quantity + ", lot=" + lot + "]";
	}
    
	
    
    
}
