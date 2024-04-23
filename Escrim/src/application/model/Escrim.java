package application.model;


import java.time.LocalDate;



public class Escrim implements ModelInterface {
    private static Escrim instance;
    private boolean State;
    private Inventory globalInventory;
    private Inventory localInventory;
    private LocalDate date;
    private int duration;
    private String description;
    private int configurationId;
    private String country;


    private Escrim() {
        globalInventory = null;
        localInventory = null;
    }
    
    public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(int configurationId) {
		this.configurationId = configurationId;
	}
	
	public boolean getState() {
		return this.State;
	}

	public void setState(boolean state) {
		this.State = state;
	}

    public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Inventory getGlobalInventory() {
		return globalInventory;
	}

	public void setGlobalInventory(Inventory globalInventory) {
		this.globalInventory = globalInventory;
	}
	
	

	public Inventory getLocalInventory() {
		return localInventory;
	}

	public void setLocalInventory(Inventory localInventory) {
		this.localInventory = localInventory;
	}

	public static Escrim getInstance() {
        // Lazy initialization
        if (instance == null) {
            instance = new Escrim();
        }
        return instance;
    }

    public boolean isState() {
        return State;
    }




}