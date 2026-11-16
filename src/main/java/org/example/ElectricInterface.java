package org.example;

/**
 * Operations specific to electric vehicles.
 */
public interface ElectricInterface {
    /**
     * Checks whether another vehicle is in the same battery range category.
     *
     * @param other the vehicle to compare with
     * @return true if the other vehicle is electric and its battery capacity differs by at most 5
     */
    boolean hasSameRangeCategory(Vehicle other);

    /**
     * Calculates the share of the price that comes from the battery.
     *
     * @return the battery cost share
     */
    int batteryCostShare();
}
