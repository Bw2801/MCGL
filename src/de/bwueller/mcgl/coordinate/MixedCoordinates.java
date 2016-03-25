package de.bwueller.mcgl.coordinate;

public class MixedCoordinates extends Coordinates {

    protected boolean xRelative, yRelative, zRelative;
    
    public MixedCoordinates(double x, boolean xRelative, double y, boolean yRelative, double z, boolean zRelative) {
        super(x, y, z);
        
        this.xRelative = xRelative;
        this.yRelative = yRelative;
        this.zRelative = zRelative;
    }

    @Override
    public String toString() {
        return
            (xRelative ? "~" + x : "" + x) + " "
            + (yRelative ? "~" + y : "" + y) + " "
            + (zRelative ? "~" + z : "" + z);
    }

    @Override
    public String to2DString() {
        return
            (xRelative ? "~" + x : "" + x) + " "
            + (zRelative ? "~" + z : "" + z);
    }
}
