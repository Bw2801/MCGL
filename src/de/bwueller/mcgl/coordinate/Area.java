package de.bwueller.mcgl.coordinate;

public class Area {

    public Coordinates min;
    public Coordinates max;

    public Area(Coordinates min, Coordinates max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public String toString() {
        return min.toString() + " " + max.toString();
    }
}
