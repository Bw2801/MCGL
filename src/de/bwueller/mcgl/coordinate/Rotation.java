package de.bwueller.mcgl.coordinate;

public class Rotation {

    protected double x, y;

    public Rotation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return x + " " + y;
    }

    public Rotation Add(double x, double y) {
        return new Rotation(this.x + x, this.y + y);
    }

    public Rotation Sub(double x, double y) {
        return Add(-x, -y);
    }
}
