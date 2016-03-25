package de.bwueller.mcgl.coordinate;

public class MixedRotation extends Rotation {

    protected boolean xRelative, yRelative;

    public MixedRotation(double x, boolean xRelative, double y, boolean yRelative) {
        super(x, y);
    }

    @Override
    public String toString() {
        return (xRelative ? "~" + x : "" + x) + " "
                + (yRelative ? "~" + y : "" + y);
    }
}
