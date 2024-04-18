package application.model;

import java.text.BreakIterator;



public class Escrim implements ModelInterface {
    private static Escrim instance;
    private boolean State;
    private Inventory globalInventory;
    private Inventory localInventory;


    private Escrim() {
        State = false; // Initial state
        globalInventory = null;
        localInventory = null;
    }

    public Inventory getGlobalInventory() {
		return globalInventory;
	}

	public void setGlobalInventory(Inventory globalInventory) {
		this.globalInventory = globalInventory;
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