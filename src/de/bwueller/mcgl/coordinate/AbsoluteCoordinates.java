package de.bwueller.mcgl.coordinate;

/**
 * To be used in places where only absolute coordinates are allowed
 */
public class AbsoluteCoordinates extends Coordinates {

    public AbsoluteCoordinates(double x, double y, double z) {
        super(x, y, z);
    }
}
