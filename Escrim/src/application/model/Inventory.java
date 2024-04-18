package application.model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Parcel> packages;

    public Inventory() {
        packages = new ArrayList<>();
    }

    public void addItem(Parcel c) {
        packages.add(c);
    }

    public void RemoveItem(Parcel c) {
        packages.remove(c);
    }

    public List<Parcel> getPackages() {
        return packages;
    }

    public void printItems() {
        for (Parcel item : packages) {
            System.out.println(item);
        }
    }
}

