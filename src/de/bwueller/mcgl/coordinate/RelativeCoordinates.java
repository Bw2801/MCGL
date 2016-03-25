package de.bwueller.mcgl.coordinate;

public class RelativeCoordinates extends Coordinates {

    public static final RelativeCoordinates Zero = new RelativeCoordinates(0, 0, 0);
    public static final RelativeCoordinates Above = new RelativeCoordinates(0, 1, 0);
    public static final RelativeCoordinates Below = new RelativeCoordinates(0, -1, 0);
    public static final RelativeCoordinates North = new RelativeCoordinates(0, 0, -1);
    public static final RelativeCoordinates East = new RelativeCoordinates(1, 0, 0);
    public static final RelativeCoordinates South = new RelativeCoordinates(0, 0, 1);
    public static final RelativeCoordinates West = new RelativeCoordinates(-1, 0, 0);
        
    public RelativeCoordinates(double x, double y, double z) {
        super(x, y, z);
    }

    @Override
    public String toString() {
        return "~" + x + " ~" + y + " ~" + z;
    }

    @Override
    public String to2DString() {
        return "~" + x + " ~" + z;
    }
}
