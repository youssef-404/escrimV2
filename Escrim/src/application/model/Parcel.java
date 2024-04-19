package application.model;

import application.model.util.Dim3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Parcel {
    private int id;
    private String affectaire;
    private String model;
    private String nominal;
    private String type;
    private String secteur;
    private Dim3D dim;
    private float weight;
    private float volume;
    
    
    private List<Item> items;
    private List<Medicaments> medicaments;
    private List<Utility> utilities;
    
    public Parcel() {
    	this.items = new ArrayList<>();
    	this.medicaments = new ArrayList<>();
    	this.utilities = new ArrayList<>();
    }
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAffectaire() {
		return affectaire;
	}
	public void setAffectaire(String affectaire) {
		this.affectaire = affectaire;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getNominal() {
		return nominal;
	}
	public void setNominal(String nominal) {
		this.nominal = nominal;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSecteur() {
		return secteur;
	}
	public void setSecteur(String secteur) {
		this.secteur = secteur;
	}
	public Dim3D getDim() {
		return dim;
	}
	public void setDim(Dim3D dim) {
		this.dim = dim;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public float getVolume() {
		return volume;
	}
	public void setVolume(float volume) {
		this.volume = volume;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Parcel other = (Parcel) obj;
		return id == other.id;
	}
	
	public List<Medicaments> getMedicaments() {
		return medicaments;
	}

	public void setMedicaments(List<Medicaments> medicaments) {
		this.medicaments = medicaments;
	}

	public void addItem(Item item) {
		this.items.add(item);
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public void addMedicament(Medicaments meds) {
		this.medicaments.add(meds);
	}

	public void addutility(Utility utility) {
		this.utilities.add(utility);
		
	}

	public List<Utility> getUtilities() {
		return utilities;
	}

	public void setUtilities(List<Utility> utilities) {
		this.utilities = utilities;
	}
	
	
	
	
	

}
