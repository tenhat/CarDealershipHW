package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeSet;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Car dealership that loads vehicles from a file and prints an inventory report.
 */
public class Dealership {
    private static final Logger LOGGER = Logger.getLogger(Dealership.class.getName());

    private final TreeSet<Vehicle> vehicles = new TreeSet<>();

    /**
     * Starts the program: sets up file logging, loads the vehicles and runs the report.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            FileHandler fileHandler = new FileHandler("cardealerApp.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            Logger.getLogger("").addHandler(fileHandler);
        } catch (IOException error) {
            LOGGER.log(Level.SEVERE, "Could not open the log file cardealerApp.log", error);
        }
        LOGGER.info("Dealership.main() called with " + args.length + " arguments");
        Dealership dealership = new Dealership();
        dealership.loadVehicles();
        dealership.runReport();
    }

    /**
     * Loads the vehicles from vehicles.txt. If the file cannot be read or is invalid,
     * sample vehicles are used instead.
     */
    private void loadVehicles() {
        LOGGER.info("loadVehicles() called");
        try (BufferedReader reader = Files.newBufferedReader(Path.of("vehicles.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) vehicles.add(parseVehicle(line));
            }
            if (vehicles.isEmpty()) throw new IOException("The input file is empty");
        } catch (IOException | IllegalArgumentException error) {
            LOGGER.log(Level.SEVERE, "Could not load vehicles.txt, using sample vehicles", error);
            System.err.println("Could not load vehicles.txt; using sample vehicles: " + error.getMessage());
            vehicles.clear();
            addFallbackVehicles();
        }
    }

    /**
     * Creates a vehicle from one line of the input file.
     *
     * @param line a line in the format TYPE#Brand Model#price#age#km#fuel[#capacity#heatPump]
     * @return the created vehicle
     * @throws IllegalArgumentException if the line is invalid
     */
    private Vehicle parseVehicle(String line) {
        LOGGER.info("parseVehicle() called with line=" + line);
        String[] parts = line.split("#");
        if (parts.length != 6 && parts.length != 8) {
            throw new IllegalArgumentException("Invalid row: " + line);
        }
        String[] name = parts[1].split(" ", 2);
        String brand = name[0];
        String model = name.length == 2 ? name[1] : "";
        double basePrice = Double.parseDouble(parts[2]);
        int age = Integer.parseInt(parts[3]);
        double km = Double.parseDouble(parts[4]);
        Vehicle.FuelType fuel = Vehicle.FuelType.valueOf(parts[5]);

        if (parts[0].equals("VEHICLE") && parts.length == 6) {
            return new Vehicle(brand, model, basePrice, age, km, fuel);
        }
        if (parts[0].equals("ELECTRIC") && parts.length == 8
                && fuel == Vehicle.FuelType.HYBRID) {
            int capacity = Integer.parseInt(parts[6]);
            if (!parts[7].equalsIgnoreCase("yes") && !parts[7].equalsIgnoreCase("no")) {
                throw new IllegalArgumentException("Heat pump must be yes or no: " + line);
            }
            boolean heatPump = parts[7].equalsIgnoreCase("yes");
            return new ElectricVehicle(brand, model, basePrice, age, km,
                    capacity, heatPump);
        }
        throw new IllegalArgumentException("Unknown vehicle type: " + line);
    }

    /**
     * Adds two sample vehicles to the inventory.
     */
    private void addFallbackVehicles() {
        LOGGER.info("addFallbackVehicles() called");
        vehicles.add(new Vehicle("Toyota", "Corolla", 5_500_000, 4,
                75_000, Vehicle.FuelType.HYBRID));
        vehicles.add(new ElectricVehicle("Nissan", "Leaf", 4_800_000, 5,
                65_000, 40, false));
    }

    /**
     * Builds the report, prints it and writes it to outputDealership.txt.
     */
    private void runReport() {
        LOGGER.info("runReport() called");
        StringBuilder report = new StringBuilder();
        appendFleetAverage(report);
        appendCheapest(report);
        appendTopElectric(report);
        appendTotalValue(report);
        appendFilteredHybrids(report);
        appendYoungCount(report);
        appendDiscountSimulation(report);

        String text = report.toString();
        System.out.print(text);
        try {
            Files.writeString(Path.of("outputDealership.txt"), text);
        } catch (IOException error) {
            LOGGER.log(Level.SEVERE, "Could not write the report to outputDealership.txt", error);
            System.err.println("Could not write outputDealership.txt: " + error.getMessage());
        }
    }

    /**
     * Appends the average mileage of the fleet to the report.
     *
     * @param report the report being built
     */
    private void appendFleetAverage(StringBuilder report) {
        LOGGER.info("appendFleetAverage() called");
        double totalKm = 0;
        for (Vehicle vehicle : vehicles) totalKm += vehicle.getKm();
        report.append("1. Fleet Average Mileage: ")
                .append(totalKm / vehicles.size()).append(" km\n");
    }

    /**
     * Appends the cheapest vehicle to the report.
     *
     * @param report the report being built
     */
    private void appendCheapest(StringBuilder report) {
        LOGGER.info("appendCheapest() called");
        report.append("2. Cheapest Vehicle: ")
                .append(vehicles.first()).append('\n');
    }

    /**
     * Appends the most expensive electric vehicle to the report.
     *
     * @param report the report being built
     */
    private void appendTopElectric(StringBuilder report) {
        LOGGER.info("appendTopElectric() called");
        ElectricVehicle top = null;
        for (Vehicle vehicle : vehicles) {
            if (vehicle instanceof ElectricVehicle) {
                ElectricVehicle electric = (ElectricVehicle) vehicle;
                if (top == null || electric.getFinalPrice() > top.getFinalPrice()) {
                    top = electric;
                }
            }
        }
        report.append("3. Top Electric Vehicle: ");
        if (top == null) {
            report.append("none\n");
        } else {
            report.append(top.getBrand()).append(' ').append(top.getModel())
                    .append(" | batteryCapacity=").append(top.getBatteryCapacity())
                    .append(" | averageKmPerYear=").append(top.averageKmPerYear())
                    .append('\n');
        }
    }

    /**
     * Appends the total value of the inventory to the report.
     *
     * @param report the report being built
     */
    private void appendTotalValue(StringBuilder report) {
        LOGGER.info("appendTotalValue() called");
        long total = 0;
        for (Vehicle vehicle : vehicles) total += vehicle.getFinalPrice();
        report.append("4. Total Fleet Inventory Value: ").append(total).append('\n');
    }

    /**
     * Appends the hybrid vehicles whose final price is not above the average to the report.
     *
     * @param report the report being built
     */
    private void appendFilteredHybrids(StringBuilder report) {
        LOGGER.info("appendFilteredHybrids() called");
        long total = 0;
        for (Vehicle vehicle : vehicles) total += vehicle.getFinalPrice();
        double averagePrice = (double) total / vehicles.size();
        report.append("5. Filtered Hybrid Inventory:\n");
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getFuelType() == Vehicle.FuelType.HYBRID
                    && vehicle.getFinalPrice() <= averagePrice) {
                report.append("   ").append(vehicle).append('\n');
            }
        }
    }

    /**
     * Appends the number of vehicles younger than 5 years to the report.
     *
     * @param report the report being built
     */
    private void appendYoungCount(StringBuilder report) {
        LOGGER.info("appendYoungCount() called");
        int count = 0;
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getAge() < 5) count++;
        }
        report.append("6. Young Inventory Count: ").append(count).append('\n');
    }

    /**
     * Applies a 10% discount to the cheapest vehicle and appends the new price to the report.
     *
     * @param report the report being built
     */
    private void appendDiscountSimulation(StringBuilder report) {
        LOGGER.info("appendDiscountSimulation() called");
        Vehicle selected = vehicles.first();
        vehicles.remove(selected);
        selected.applyDiscount(10);
        vehicles.add(selected);
        report.append("7. Discount Simulation: ")
                .append(selected.getBrand()).append(' ').append(selected.getModel())
                .append(" | updatedFinalPrice=").append(selected.getFinalPrice())
                .append('\n');
    }
}

