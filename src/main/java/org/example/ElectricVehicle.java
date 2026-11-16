package org.example;

import java.util.logging.Logger;

/**
 * An electric (hybrid) vehicle with a battery.
 */
public class ElectricVehicle extends Vehicle implements ElectricInterface {
    private static final Logger LOGGER = Logger.getLogger(ElectricVehicle.class.getName());

    private int batteryCapacity;
    private boolean hasHeatPump;

    /**
     * Creates an electric vehicle. The fuel type is always HYBRID.
     *
     * @param brand the brand name
     * @param model the model name
     * @param basePrice the base price
     * @param age the age in years
     * @param km the distance driven in kilometres
     * @param batteryCapacity the battery capacity
     * @param hasHeatPump whether the vehicle has a heat pump
     */
    public ElectricVehicle(String brand, String model, double basePrice,
                           int age, double km, int batteryCapacity,
                           boolean hasHeatPump) {
        super(brand, model, basePrice, age, km, FuelType.HYBRID);
        LOGGER.info("ElectricVehicle constructor called: brand=" + brand + ", model=" + model
                + ", batteryCapacity=" + batteryCapacity + ", hasHeatPump=" + hasHeatPump);
        this.batteryCapacity = batteryCapacity;
        this.hasHeatPump = hasHeatPump;
    }

    /**
     * Returns the battery capacity.
     *
     * @return the battery capacity
     */
    public int getBatteryCapacity() {
        LOGGER.info("getBatteryCapacity() called");
        return batteryCapacity;
    }

    /**
     * Sets the battery capacity.
     *
     * @param batteryCapacity the new battery capacity
     */
    public void setBatteryCapacity(int batteryCapacity) {
        LOGGER.info("setBatteryCapacity() called with batteryCapacity=" + batteryCapacity);
        this.batteryCapacity = batteryCapacity;
    }

    /**
     * Tells whether the vehicle has a heat pump.
     *
     * @return true if it has a heat pump
     */
    public boolean hasHeatPump() {
        LOGGER.info("hasHeatPump() called");
        return hasHeatPump;
    }

    /**
     * Sets whether the vehicle has a heat pump.
     *
     * @param hasHeatPump true if it has a heat pump
     */
    public void setHasHeatPump(boolean hasHeatPump) {
        LOGGER.info("setHasHeatPump() called with hasHeatPump=" + hasHeatPump);
        this.hasHeatPump = hasHeatPump;
    }

    /**
     * Calculates the final price: +10% for a battery of at least 60 and +5% for a heat pump.
     *
     * @return the final price
     */
    @Override
    public int getFinalPrice() {
        LOGGER.info("ElectricVehicle.getFinalPrice() called");
        double multiplier = 1.0;
        if (batteryCapacity >= 60) multiplier += 0.10;
        if (hasHeatPump) multiplier += 0.05;
        return (int) Math.round(super.getFinalPrice() * multiplier);
    }

    /**
     * Returns a text description including the battery data.
     *
     * @return the description
     */
    @Override
    public String toString() {
        LOGGER.info("ElectricVehicle.toString() called");
        return super.toString() + " | batteryCapacity=" + batteryCapacity
                + " | hasHeatPump=" + hasHeatPump;
    }

    /**
     * Checks whether another vehicle is in the same battery range category.
     *
     * @param other the vehicle to compare with
     * @return true if the other vehicle is electric and its battery capacity differs by at most 5
     */
    @Override
    public boolean hasSameRangeCategory(Vehicle other) {
        LOGGER.info("hasSameRangeCategory() called with other=" + other.getBrand() + " " + other.getModel());
        if (!(other instanceof ElectricVehicle)) return false;
        ElectricVehicle electric = (ElectricVehicle) other;
        return Math.abs(batteryCapacity - electric.batteryCapacity) <= 5;
    }

    /**
     * Calculates the battery cost share (25 000 per capacity unit).
     *
     * @return the battery cost share
     */
    @Override
    public int batteryCostShare() {
        LOGGER.info("batteryCostShare() called");
        return batteryCapacity * 25_000;
    }
}
