package org.example;

import java.util.logging.Logger;

/**
 * A vehicle sold by the dealership.
 */
public class Vehicle implements VehicleInterface, Comparable<Vehicle> {
    private static final Logger LOGGER = Logger.getLogger(Vehicle.class.getName());

    /**
     * Fuel types a vehicle can use.
     */
    public enum FuelType { PETROL, DIESEL, HYBRID }

    private static long nextId = 0;
    private final long id = ++nextId;

    private String brand;
    private String model;
    private double basePrice;
    private int age;
    private double km;
    private FuelType fuelType;

    /**
     * Creates a vehicle.
     *
     * @param brand the brand name
     * @param model the model name
     * @param basePrice the base price
     * @param age the age in years
     * @param km the distance driven in kilometres
     * @param fuelType the fuel type
     */
    public Vehicle(String brand, String model, double basePrice,
                   int age, double km, FuelType fuelType) {
        LOGGER.info("Vehicle constructor called: brand=" + brand + ", model=" + model
                + ", basePrice=" + basePrice + ", age=" + age + ", km=" + km
                + ", fuelType=" + fuelType);
        this.brand = brand;
        this.model = model;
        this.basePrice = basePrice;
        this.age = age;
        this.km = km;
        this.fuelType = fuelType;
    }

    /**
     * Returns the brand.
     *
     * @return the brand name
     */
    public String getBrand() {
        LOGGER.info("getBrand() called for vehicle " + id);
        return brand;
    }

    /**
     * Sets the brand.
     *
     * @param brand the new brand name
     */
    public void setBrand(String brand) {
        LOGGER.info("setBrand() called for vehicle " + id + " with brand=" + brand);
        this.brand = brand;
    }

    /**
     * Returns the model.
     *
     * @return the model name
     */
    public String getModel() {
        LOGGER.info("getModel() called for vehicle " + id);
        return model;
    }

    /**
     * Sets the model.
     *
     * @param model the new model name
     */
    public void setModel(String model) {
        LOGGER.info("setModel() called for vehicle " + id + " with model=" + model);
        this.model = model;
    }

    /**
     * Returns the base price.
     *
     * @return the base price
     */
    public double getBasePrice() {
        LOGGER.info("getBasePrice() called for vehicle " + id);
        return basePrice;
    }

    /**
     * Sets the base price.
     *
     * @param basePrice the new base price
     */
    public void setBasePrice(double basePrice) {
        LOGGER.info("setBasePrice() called for vehicle " + id + " with basePrice=" + basePrice);
        this.basePrice = basePrice;
    }

    /**
     * Returns the age.
     *
     * @return the age in years
     */
    public int getAge() {
        LOGGER.info("getAge() called for vehicle " + id);
        return age;
    }

    /**
     * Sets the age.
     *
     * @param age the new age in years
     */
    public void setAge(int age) {
        LOGGER.info("setAge() called for vehicle " + id + " with age=" + age);
        this.age = age;
    }

    /**
     * Returns the distance driven.
     *
     * @return the distance in kilometres
     */
    public double getKm() {
        LOGGER.info("getKm() called for vehicle " + id);
        return km;
    }

    /**
     * Sets the distance driven.
     *
     * @param km the new distance in kilometres
     */
    public void setKm(double km) {
        LOGGER.info("setKm() called for vehicle " + id + " with km=" + km);
        this.km = km;
    }

    /**
     * Returns the fuel type.
     *
     * @return the fuel type
     */
    public FuelType getFuelType() {
        LOGGER.info("getFuelType() called for vehicle " + id);
        return fuelType;
    }

    /**
     * Sets the fuel type.
     *
     * @param fuelType the new fuel type
     */
    public void setFuelType(FuelType fuelType) {
        LOGGER.info("setFuelType() called for vehicle " + id + " with fuelType=" + fuelType);
        this.fuelType = fuelType;
    }

    /**
     * Reduces the base price by the given percentage.
     *
     * @param percentage discount in percent, between 0 and 100
     * @throws IllegalArgumentException if the percentage is outside 0..100
     */
    @Override
    public void applyDiscount(int percentage) {
        LOGGER.info("applyDiscount() called for vehicle " + id + " with percentage=" + percentage);
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Discount must be 0..100");
        }
        basePrice *= (100 - percentage) / 100.0;
    }

    /**
     * Calculates the final price from the base price, the age and the fuel type.
     *
     * @return the final price
     */
    @Override
    public int getFinalPrice() {
        LOGGER.info("Vehicle.getFinalPrice() called for vehicle " + id);
        double price = Math.max(200000, basePrice - age * 150000);
        if (fuelType == FuelType.HYBRID) {
            price *= 1.15;
        } else if (fuelType == FuelType.DIESEL) {
            price *= 1.05;
        }
        return (int) Math.round(price);
    }

    /**
     * Calculates the average distance driven per year.
     *
     * @return the average kilometres per year, or the total distance if the age is 0
     */
    @Override
    public double averageKmPerYear() {
        LOGGER.info("averageKmPerYear() called for vehicle " + id);
        return age == 0 ? km : km / age;
    }

    /**
     * Compares vehicles by final price, then by creation order.
     *
     * @param other the vehicle to compare with
     * @return a negative number, zero or a positive number if this vehicle is
     *         less than, equal to or greater than the other
     */
    @Override
    public int compareTo(Vehicle other) {
        LOGGER.info("compareTo() called for vehicle " + id + " with vehicle " + other.id);
        int byPrice = Integer.compare(getFinalPrice(), other.getFinalPrice());
        return byPrice != 0 ? byPrice : Long.compare(id, other.id);
    }

    /**
     * Returns a text description of the vehicle.
     *
     * @return the description with all fields, the final price and the yearly average
     */
    @Override
    public String toString() {
        LOGGER.info("Vehicle.toString() called for vehicle " + id);
        StringBuilder text = new StringBuilder();
        text.append(brand).append(' ').append(model);
        text.append(" | basePrice=").append(basePrice);
        text.append(" | age=").append(age);
        text.append(" | km=").append(km);
        text.append(" | fuelType=").append(fuelType);
        text.append(" | finalPrice=").append(getFinalPrice());
        text.append(" | averageKmPerYear=").append(averageKmPerYear());
        return text.toString();
    }
}
