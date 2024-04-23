package application.model;

public class Bed extends Item {

    private Boolean inUse;

    public Bed(int id, int quantity) {
        super(id, quantity);
        this.inUse = false;
    }

	public Boolean getInUse() {
		return inUse;
	}

	public void setInUse(Boolean inUse) {
		this.inUse = inUse;
	}
    
    
}
