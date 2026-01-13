/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
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

    // ADD BUS (NO DUPLICATES)
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

    public ArrayList<Bus> getAllBuses() {
        return new ArrayList<>(busList);
    }

    public Queue<Bus> getRecentBuses() {
        return new LinkedList<>(recentBuses);
    }

    // SEARCH
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

    public Bus binarySearchById(int busId) {
        try {
            if (busId <= 0) {
                throw new IllegalArgumentException("Invalid Bus ID");
            }
            ArrayList<Bus> sortedList = new ArrayList<>(busList);
            for (int i = 0; i < sortedList.size() - 1; i++) {
                for (int j = 0; j < sortedList.size() - i - 1; j++) {
                    if (sortedList.get(j).getBusId() > sortedList.get(j + 1).getBusId()) {
                        Bus temp = sortedList.get(j);
                        sortedList.set(j, sortedList.get(j + 1));
                        sortedList.set(j + 1, temp);
                    }
                }
            }
            int left = 0;
            int right = sortedList.size() - 1;
            while (left <= right) {
                int mid = left + (right - left) / 2; 

                if (sortedList.get(mid).getBusId() == busId) {
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

    // SORT BY FARE (Bubble Sort)
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

    // SORT BY BUS ID (Bubble Sort)
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

    public int getTotalBuses() {
        return busList.size();
    }

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
