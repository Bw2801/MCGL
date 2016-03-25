package de.bwueller.mcgl.coordinate;

public class Coordinates {

    protected double x, y, z;

    public Coordinates(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }

    public String to2DString() {
        return x + " " + z;
    }

    public Coordinates add(double x, double y, double z) {
        return new Coordinates(this.x + x, this.y + y, this.z + z);
    }

    public Coordinates sub(double x, double y, double z) {
        return add(-x, -y, -z);
    }
}
