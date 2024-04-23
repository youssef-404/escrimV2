package application.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Configuration {
    private int id;
    private String name;
    private String catastrophe;
    private int semaines;
    private List<Plane> avions;
    private List<Parcel> colis;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatastrophe() {
        return catastrophe;
    }

    public void setCatastrophe(String catastrophe) {
        this.catastrophe = catastrophe;
    }

    public int getSemaines() {
        return semaines;
    }

    public void setSemaines(int semaines) {
        this.semaines = semaines;
    }

    public List<Plane> getAvions() {
        return avions;
    }

    public void setAvions(List<Plane> avions) {
        this.avions = avions;
    }

    public List<Parcel> getColis() {
        return colis;
    }

    public void setColis(List<Parcel> colis) {
        this.colis = colis;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null || getClass() != obj.getClass())
	        return false;
	    Configuration other = (Configuration) obj;
	    return id == other.id;
	}
    
    
    
}
