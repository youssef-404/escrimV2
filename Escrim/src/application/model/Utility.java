package application.model;
import java.time.LocalDate;


public class Utility {
	private int id;
	private LocalDate dlu;
    private String designation;
    private int quantity;
    private int parcel;
    
    
    
    public Utility(int id, String designation, LocalDate dlu, int quantity,int parcel) {
		super();
		this.id = id;
		this.dlu = dlu;
		this.designation = designation;
		this.quantity = quantity;
		this.parcel = parcel;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParcel() {
		return parcel;
	}
	public void setParcel(int parcel) {
		this.parcel= parcel;
	}
	
	public String getDlu() {
		return dlu.toString();
	}
	public void setDlu(LocalDate dlu) {
		this.dlu = dlu;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
