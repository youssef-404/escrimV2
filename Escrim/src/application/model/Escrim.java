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

	public void setState(boolean state) {
		State = state;
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

    public void toogleState() {
        this.State = !this.State;
        if (State) {
            localInventory = new Inventory();
        } else {
            localInventory = null;
        }
    }

    public void deploy(Configuration config) {
        boolean flag = true;
        for (Parcel p : config.getColis()) {
            //CHeck if Package exists
            if (!globalInventory.getPackages().contains(p)) {
                System.out.println("Error: Package p.getId()  not found in global inventory.");
                flag = false;
            }
        }

        //Move Colis from Inv to Inv
        if (flag) {
            for (Parcel p : config.getColis()) {
                globalInventory.RemoveItem(p);
                localInventory.addItem(p);
            }
        }

    }


}