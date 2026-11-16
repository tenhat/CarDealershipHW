package org.example;

/**
 * Basic operations of a vehicle in the dealership.
 */
public interface VehicleInterface {
    /**
     * Reduces the base price by the given percentage.
     *
     * @param percentage discount in percent, between 0 and 100
     * @throws IllegalArgumentException if the percentage is outside 0..100
     */
    void applyDiscount(int percentage);

    /**
     * Calculates the final selling price of the vehicle.
     *
     * @return the final price
     */
    int getFinalPrice();

    /**
     * Calculates the average distance driven per year.
     *
     * @return the average kilometres per year
     */
    double averageKmPerYear();

    /**
     * Returns a text description of the vehicle.
     *
     * @return the description
     */
    String toString();
}
