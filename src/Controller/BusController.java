/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 * Controller class for managing bus operations in the NationWay Bus Service
 * system. This class handles all business logic related to buses including add,
 * update, delete, search, sort operations, and booking management using various
 * data structures.
 *
 * Data Structures Used: - ArrayList: For storing the main bus collection -
 * Queue (LinkedList): For tracking recently added buses (max 5) - Stack: For
 * maintaining booking history for undo functionality
 *
 * @author lalit
 */
import Model.Bus;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BusController {

    private ArrayList<Bus> busList;
    private Queue<Bus> recentBuses;
    private Stack<Bus> bookingHistory;

    /**
     * Constructs a new BusController and initializes data structures. Also
     * loads predefined bus data for demonstration purposes.
     */
    public BusController() {
        try {
            busList = new ArrayList<>();
            recentBuses = new LinkedList<>();
            bookingHistory = new Stack<>();
            loadPreData();
        } catch (Exception e) {
            System.out.println("Error initializing BusController: " + e.getMessage());
        }
    }

    /**
     * Loads predefined bus data into the system. This method is called during
     * initialization to populate the system with sample bus routes for testing
     * and demonstration.
     */
    private void loadPreData() {
        try {
            addBus(new Bus(101, "NW-01", "Kathmandu - Pokhara", 1200, "6:00 AM", 40));
            addBus(new Bus(102, "NW-02", "Kathmandu - Chitwan", 800, "7:30 AM", 35));
            addBus(new Bus(103, "NW-03", "Pokhara - Butwal", 900, "9:00 AM", 30));
            addBus(new Bus(104, "NW-04", "Dharan - Kathmandu", 1500, "5:00 PM", 45));
            addBus(new Bus(105, "NW-05", "Nepalgunj - Surkhet", 700, "8:00 AM", 25));
        } catch (Exception e) {
            System.out.println("Error loading pre-data: " + e.getMessage());
        }
    }

    /**
     * Adds a new bus to the system with duplicate validation. Checks for
     * duplicate Bus ID and Bus Number before adding. Also updates the recent
     * buses queue (maintains last 5 additions).
     *
     * @param bus The Bus object to be added
     * @return true if bus was successfully added, false otherwise
     * @throws IllegalArgumentException if bus is null, has duplicate ID, or
     * duplicate bus number
     */
    public boolean addBus(Bus bus) {
        try {
            if (bus == null) {
                throw new IllegalArgumentException("Bus cannot be null");
            }

            // Check for duplicate Bus ID
            for (Bus existingBus : busList) {
                if (existingBus.getBusId() == bus.getBusId()) {
                    throw new IllegalArgumentException("Duplicate Bus ID: " + bus.getBusId());
                }

                if (existingBus.getBusNumber().equalsIgnoreCase(bus.getBusNumber().trim())) {
                    throw new IllegalArgumentException("Duplicate Bus Number: " + bus.getBusNumber());
                }
            }

            busList.add(bus);

            // Maintain recent buses queue (max 5)
            recentBuses.add(bus);
            if (recentBuses.size() > 5) {
                recentBuses.poll();
            }

            return true;

        } catch (IllegalArgumentException e) {
            System.out.println("Add Bus Error: " + e.getMessage());
            throw e; // Re-throw to let UI handle it
        } catch (Exception e) {
            System.out.println("Unexpected Error while adding bus: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a bus from the system by its ID.
     *
     * @param busId The ID of the bus to be deleted
     * @return true if bus was successfully deleted, false otherwise
     * @throws IllegalArgumentException if busId <= 0 or bus not found
     */
    public boolean deleteBus(int busId) {
        try {
            if (busId <= 0) {
                throw new IllegalArgumentException("Invalid Bus ID");
            }

            for (int i = 0; i < busList.size(); i++) {
                if (busList.get(i).getBusId() == busId) {
                    busList.remove(i);
                    return true;
                }
            }
            throw new IllegalArgumentException("Bus with ID " + busId + " not found");

        } catch (IllegalArgumentException e) {
            System.out.println("Delete Bus Error: " + e.getMessage());
            throw e; // Re-throw to let UI handle it
        } catch (Exception e) {
            System.out.println("Unexpected error deleting bus: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing bus's information. Validates all fields before
     * updating. The bus is identified by its ID, and all other fields are
     * updated with the new values.
     *
     * @param updatedBus The Bus object containing updated information
     * @return true if bus was successfully updated, false otherwise
     * @throws IllegalArgumentException if updated data is invalid or bus not
     * found
     */
    public boolean updateBus(Bus updatedBus) {
        try {
            if (updatedBus == null) {
                throw new IllegalArgumentException("Bus cannot be null");
            }

            for (Bus bus : busList) {
                if (bus.getBusId() == updatedBus.getBusId()) {
                    // Validate updated data
                    if (updatedBus.getBusNumber() == null || updatedBus.getBusNumber().trim().isEmpty()) {
                        throw new IllegalArgumentException("Bus number cannot be empty");
                    }
                    if (updatedBus.getRoute() == null || updatedBus.getRoute().trim().isEmpty()) {
                        throw new IllegalArgumentException("Route cannot be empty");
                    }
                    if (updatedBus.getFare() < 0) {
                        throw new IllegalArgumentException("Fare cannot be negative");
                    }
                    if (updatedBus.getTotalSeats() <= 0) {
                        throw new IllegalArgumentException("Total seats must be greater than 0");
                    }

                    bus.setBusNumber(updatedBus.getBusNumber());
                    bus.setRoute(updatedBus.getRoute());
                    bus.setFare(updatedBus.getFare());
                    bus.setDepartureTime(updatedBus.getDepartureTime());
                    bus.setTotalSeats(updatedBus.getTotalSeats());
                    return true;
                }
            }

            throw new IllegalArgumentException("Bus with ID " + updatedBus.getBusId() + " not found");

        } catch (IllegalArgumentException e) {
            System.out.println("Update Bus Error: " + e.getMessage());
            throw e; // Re-throw to let UI handle it
        } catch (Exception e) {
            System.out.println("Unexpected error updating bus: " + e.getMessage());
            return false;
        }
    }

    /**
     * Books a seat on the specified bus. Uses binary search to find the bus
     * efficiently and adds the booking to the booking history stack for
     * potential cancellation.
     *
     * @param busId The ID of the bus to book a seat on
     * @return true if seat was successfully booked, false otherwise
     * @throws IllegalArgumentException if busId is invalid or bus not found
     */
    public boolean bookSeat(int busId) {
        try {
            if (busId <= 0) {
                throw new IllegalArgumentException("Invalid Bus ID");
            }

            Bus bus = binarySearchById(busId);

            if (bus == null) {
                throw new IllegalArgumentException("Bus with ID " + busId + " not found");
            }

            if (bus.bookSeat()) {
                bookingHistory.push(bus);
                return true;
            }

            return false;

        } catch (IllegalArgumentException e) {
            System.out.println("Booking Error: " + e.getMessage());
            throw e; // Re-throw to let UI handle it
        } catch (Exception e) {
            System.out.println("Unexpected error booking seat: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cancels the most recent booking using stack-based history. Pops the last
     * booking from the history stack and cancels that seat.
     *
     * @return true if booking was successfully cancelled, false otherwise
     * @throws IllegalStateException if no booking history is available
     */
    public boolean cancelLastBooking() {
        try {
            if (bookingHistory.isEmpty()) {
                throw new IllegalStateException("No booking history available");
            }

            Bus bus = bookingHistory.pop();
            bus.cancelSeat();
            return true;

        } catch (IllegalStateException e) {
            System.out.println("Cancel Booking Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("Unexpected error cancelling booking: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns a copy of all buses in the system.
     *
     * @return ArrayList containing all Bus objects
     */
    public ArrayList<Bus> getAllBuses() {
        return new ArrayList<>(busList);
    }

    /**
     * Returns a copy of the recently added buses queue.
     *
     * @return Queue containing the 5 most recently added buses
     */
    public Queue<Bus> getRecentBuses() {
        return new LinkedList<>(recentBuses);
    }

    /**
     * Searches for buses by keyword matching in bus number or route. Performs
     * case-insensitive search on both bus number and route fields.
     *
     * @param keyword The search term (searches bus number and route)
     * @return ArrayList of buses matching the search criteria
     */
    public ArrayList<Bus> searchBuses(String keyword) {
        try {
            ArrayList<Bus> results = new ArrayList<>();

            if (keyword == null || keyword.trim().isEmpty()) {
                return getAllBuses();
            }

            String lowerKeyword = keyword.toLowerCase().trim();

            for (Bus bus : busList) {
                if (bus.getBusNumber().toLowerCase().contains(lowerKeyword)
                        || bus.getRoute().toLowerCase().contains(lowerKeyword)) {
                    results.add(bus);
                }
            }

            return results;

        } catch (Exception e) {
            System.out.println("Error searching buses: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Performs binary search to find a bus by its ID. First sorts the bus list
     * by ID using bubble sort, then performs binary search algorithm for
     * efficient lookup.
     *
     * Time Complexity: O(n²) for sorting + O(log n) for binary search
     *
     * @param busId The ID of the bus to search for
     * @return The Bus object if found, null otherwise
     * @throws IllegalArgumentException if busId <= 0
     */
    public Bus binarySearchById(int busId) {
        try {
            if (busId <= 0) {
                throw new IllegalArgumentException("Invalid Bus ID");
            }

            // Create sorted copy of bus list
            ArrayList<Bus> sortedList = new ArrayList<>(busList);

            // Bubble sort by bus ID
            for (int i = 0; i < sortedList.size() - 1; i++) {
                for (int j = 0; j < sortedList.size() - i - 1; j++) {
                    if (sortedList.get(j).getBusId() > sortedList.get(j + 1).getBusId()) {
                        Bus temp = sortedList.get(j);
                        sortedList.set(j, sortedList.get(j + 1));
                        sortedList.set(j + 1, temp);
                    }
                }
            }

            // Binary search
            int left = 0;
            int right = sortedList.size() - 1;
            while (left <= right) {
                int mid = left + (right - left) / 2;

                if (sortedList.get(mid).getBusId() == busId) {
                    // Return original bus object from busList
                    for (Bus bus : busList) {
                        if (bus.getBusId() == busId) {
                            return bus;
                        }
                    }
                } else if (sortedList.get(mid).getBusId() < busId) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("Binary Search Error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Unexpected error in binary search: " + e.getMessage());
            return null;
        }
    }

    /**
     * Sorts the bus list by fare in ascending order using Bubble Sort
     * algorithm.
     *
     * Time Complexity: O(n²) Space Complexity: O(1)
     */
    public void sortByFare() {
        try {
            for (int i = 0; i < busList.size() - 1; i++) {
                for (int j = 0; j < busList.size() - i - 1; j++) {
                    if (busList.get(j).getFare() > busList.get(j + 1).getFare()) {
                        Bus temp = busList.get(j);
                        busList.set(j, busList.get(j + 1));
                        busList.set(j + 1, temp);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error sorting by fare: " + e.getMessage());
        }
    }

    /**
     * Sorts the bus list by bus ID in ascending order using Bubble Sort
     * algorithm.
     *
     * Time Complexity: O(n²) Space Complexity: O(1)
     */
    public void sortById() {
        try {
            for (int i = 0; i < busList.size() - 1; i++) {
                for (int j = 0; j < busList.size() - i - 1; j++) {
                    if (busList.get(j).getBusId() > busList.get(j + 1).getBusId()) {
                        Bus temp = busList.get(j);
                        busList.set(j, busList.get(j + 1));
                        busList.set(j + 1, temp);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error sorting by ID: " + e.getMessage());
        }
    }

    /**
     * Gets the total number of buses in the system.
     *
     * @return Total count of buses
     */
    public int getTotalBuses() {
        return busList.size();
    }

    /**
     * Calculates the total number of available seats across all buses.
     *
     * @return Sum of available seats from all buses
     */
    public int getTotalAvailableSeats() {
        try {
            int total = 0;
            for (Bus bus : busList) {
                total += bus.getAvailableSeats();
            }
            return total;
        } catch (Exception e) {
            System.out.println("Error calculating total available seats: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Gets a list of unique routes covered by all buses. Returns only distinct
     * routes without duplicates.
     *
     * @return ArrayList of unique route descriptions
     */
    public ArrayList<String> getRoutesCovered() {
        try {
            ArrayList<String> routes = new ArrayList<>();
            for (Bus bus : busList) {
                if (!routes.contains(bus.getRoute())) {
                    routes.add(bus.getRoute());
                }
            }
            return routes;
        } catch (Exception e) {
            System.out.println("Error getting routes: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
