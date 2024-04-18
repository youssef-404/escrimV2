package application.model.util;

public class Dim3D {
    private double length;
    private double width;
    private double height;

    public Dim3D(double length, double width,double height) {
        this.length = length;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return length + "X" + width + "X" + height;
    }

    public double getLength() {
        return length;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getArea() {
        return length * width * height;
    }
}

