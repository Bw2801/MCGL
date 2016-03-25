package de.bwueller.mcgl.coordinate;

public class RelativeRotation extends Rotation {

    public RelativeRotation(double x, double y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return "~" + x + " ~" + y;
    }
}
