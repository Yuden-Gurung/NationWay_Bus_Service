/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 * Represents a bus in the NationWay Bus Service system. This class encapsulates
 * all information related to a bus including. identification, route details,
 * pricing, and seat availability.
 *
 * @author lalit
 */
public class Bus {

    private int busId;
    private String busNumber;
    private String route;
    private double fare;
    private String departureTime;
    private int totalSeats;
    private int availableSeats;

    /**
     * Constructs a new Bus object with the specified parameters. Initializes
     * the bus with all required information and sets available seats equal to
     * total seats.
     *
     * @param busId Unique identifier for the bus (must be positive)
     * @param busNumber Registration/identification number of the bus
     * @param route Route description (origin - destination)
     * @param fare Ticket fare for this bus (cannot be negative)
     * @param departureTime Scheduled departure time
     * @param totalSeats Total number of seats in the bus (must be positive)
     * @throws IllegalArgumentException if busId <= 0, totalSeats <= 0, or fare
     * < 0
     */
    public Bus(int busId, String busNumber, String route,
            double fare, String departureTime, int totalSeats) {

        try {
            if (busId <= 0 || totalSeats <= 0 || fare < 0) {
                throw new IllegalArgumentException("Invalid bus data");
            }

            this.busId = busId;
            this.busNumber = busNumber;
            this.route = route;
            this.fare = fare;
            this.departureTime = departureTime;
            this.totalSeats = totalSeats;
            this.availableSeats = totalSeats;

        } catch (IllegalArgumentException e) {
            System.out.println("Bus creation failed: " + e.getMessage());
        }
    }

    public int getBusId() {
        return busId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public String getRoute() {
        return route;
    }

    public double getFare() {
        return fare;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setBusId(int busId) {
        if (busId <= 0) {
            throw new IllegalArgumentException("Invalid bus data");
        }
        this.busId = busId;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void setFare(double fare) {
        try {
            if (fare < 0) {
                throw new IllegalArgumentException("Fare cannot be negative");
            }
            this.fare = fare;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setTotalSeats(int totalSeats) {
        try {
            if (totalSeats <= 0) {
                throw new IllegalArgumentException("Total seats must be greater than 0");
            }
            this.totalSeats = totalSeats;
            this.availableSeats = totalSeats;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setAvailableSeats(int availableSeats) {
        try {
            if (availableSeats < 0 || availableSeats > totalSeats) {
                throw new IllegalArgumentException("Invalid available seats value");
            }
            this.availableSeats = availableSeats;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Books a seat on this bus by decrementing the available seats count.
     *
     * @return true if seat was successfully booked, false otherwise
     * @throws IllegalStateException if no seats are available
     */
    public boolean bookSeat() {
        try {
            if (availableSeats <= 0) {
                throw new IllegalStateException("No seats available");
            }
            availableSeats--;
            return true;
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Cancels a seat booking by incrementing the available seats count.
     *
     * @throws IllegalStateException if all seats are already available
     */
    public void cancelSeat() {
        try {
            if (availableSeats >= totalSeats) {
                throw new IllegalStateException("All seats are already available");
            }
            availableSeats++;
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
