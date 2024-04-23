package application.model;

import application.model.util.Dim3D;
import application.model.util.Dim2D;

public class Plane {
    private int id;
    private String name;
    private float maxLoad;
    private Dim2D doorSize;
    private Dim3D cargoHold;
    private float loadRange;
    
    
	public int getId() {
		return id;
	}
	public void setId(int i) {
		this.id = i;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getMaxLoad() {
		return maxLoad;
	}
	public void setMaxLoad(float maxLoad) {
		this.maxLoad = maxLoad;
	}
	public Dim2D getDoorSize() {
		return doorSize;
	}
	public void setDoorSize(Dim2D doorSize) {
		this.doorSize = doorSize;
	}
	public Dim3D getCargoHold() {
		return cargoHold;
	}
	public void setCargoHold(Dim3D cargoHold) {
		this.cargoHold = cargoHold;
	}
	public float getLoadRange() {
		return loadRange;
	}
	public void setLoadRange(float loadRange) {
		this.loadRange = loadRange;
	}
    
    

}
