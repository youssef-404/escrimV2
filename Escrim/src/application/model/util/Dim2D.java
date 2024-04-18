package application.model.util;

public class Dim2D {
    private double length;
    private double width;

    public Dim2D(double length, double width) {
        this.length = length;
        this.width = width;
    }

    @Override
    public String toString() {
        return length + "x" + width;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public double getArea() {
        return length * width;
    }
}

