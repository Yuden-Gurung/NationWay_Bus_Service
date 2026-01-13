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
        addBus(new Bus(101, "NW-01", "Kathmandu - Pokhara", 1200, "6:00 AM", 40));
        addBus(new Bus(102, "NW-02", "Kathmandu - Chitwan", 800, "7:30 AM", 35));
        addBus(new Bus(103, "NW-03", "Pokhara - Butwal", 900, "9:00 AM", 30));
        addBus(new Bus(104, "NW-04", "Dharan - Kathmandu", 1500, "5:00 PM", 45));
        addBus(new Bus(105, "NW-05", "Nepalgunj - Surkhet", 700, "8:00 AM", 25));
    }

    // ADD BUS (NO DUPLICATES)
    public boolean addBus(Bus bus) {
        try {
            if (bus == null) {
                throw new IllegalArgumentException("Bus cannot be null");
            }

            for (int i = 0; i < busList.size(); i++) {
                Bus existingBus = busList.get(i);

                if (existingBus.getBusId() == bus.getBusId()) {
                    throw new IllegalArgumentException("Duplicate Bus ID");
                }

                if (existingBus.getBusNumber()
                        .equalsIgnoreCase(bus.getBusNumber().trim())) {
                    throw new IllegalArgumentException("Duplicate Bus Number");
                }
            }

            busList.add(bus);

            recentBuses.add(bus);
            if (recentBuses.size() > 5) {
                recentBuses.poll();
            }

            return true;

        } catch (IllegalArgumentException e) {
            System.out.println("Add Bus Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Unexpected Error while adding bus");
            return false;
        }
    }

    public boolean updateBus(Bus updatedBus) {
        try {
            if (updatedBus == null) {
                throw new IllegalArgumentException("Bus is null");
            }

            for (int i = 0; i < busList.size(); i++) {
                Bus bus = busList.get(i);

                if (bus.getBusId() == updatedBus.getBusId()) {
                    bus.setBusNumber(updatedBus.getBusNumber());
                    bus.setRoute(updatedBus.getRoute());
                    bus.setFare(updatedBus.getFare());
                    bus.setDepartureTime(updatedBus.getDepartureTime());
                    bus.setTotalSeats(updatedBus.getTotalSeats());
                    return true;
                }
            }

            return false;

        } catch (IllegalArgumentException e) {
            System.out.println("Update Bus Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBus(int busId) {
        try {
            for (int i = 0; i < busList.size(); i++) {
                if (busList.get(i).getBusId() == busId) {
                    busList.remove(i);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error deleting bus: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Bus> getAllBuses() {
        return new ArrayList<>(busList);
    }

    public Queue<Bus> getRecentBuses() {
        return recentBuses;
    }

    // SEARCH
    public ArrayList<Bus> searchBuses(String keyword) {
        ArrayList<Bus> results = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBuses();
        }

        for (Bus bus : busList) {
            if (bus.getBusNumber().toLowerCase().contains(keyword.toLowerCase())
                    || bus.getRoute().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(bus);
            }
        }
        return results;
    }

    // SORT BY FARE
    public void sortByFare() {
        for (int i = 0; i < busList.size() - 1; i++) {
            for (int j = 0; j < busList.size() - i - 1; j++) {
                if (busList.get(j).getFare() > busList.get(j + 1).getFare()) {
                    Bus temp = busList.get(j);
                    busList.set(j, busList.get(j + 1));
                    busList.set(j + 1, temp);
                }
            }
        }
    }

    public Bus binarySearchById(int busId) {
        try {
            // Sort by ID (Bubble Sort)
            for (int i = 0; i < busList.size() - 1; i++) {
                for (int j = 0; j < busList.size() - i - 1; j++) {
                    if (busList.get(j).getBusId() > busList.get(j + 1).getBusId()) {
                        Bus temp = busList.get(j);
                        busList.set(j, busList.get(j + 1));
                        busList.set(j + 1, temp);
                    }
                }
            }

            int left = 0;
            int right = busList.size() - 1;

            while (left <= right) {
                int mid = (left + right) / 2;

                if (busList.get(mid).getBusId() == busId) {
                    return busList.get(mid);
                } else if (busList.get(mid).getBusId() < busId) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            return null;

        } catch (Exception e) {
            System.out.println("Binary Search Error: " + e.getMessage());
            return null;
        }
    }

    public boolean bookSeat(int busId) {
        try {
            Bus bus = binarySearchById(busId);
            if (bus != null && bus.bookSeat()) {
                bookingHistory.push(bus);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean cancelLastBooking() {
        try {
            if (!bookingHistory.isEmpty()) {
                Bus bus = bookingHistory.pop();
                bus.cancelSeat();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public int getTotalBuses() {
        return busList.size();
    }

    public int getTotalAvailableSeats() {
        int total = 0;
        for (Bus bus : busList) {
            total += bus.getAvailableSeats();
        }
        return total;
    }

    public ArrayList<String> getRoutesCovered() {
        ArrayList<String> routes = new ArrayList<>();
        for (Bus bus : busList) {
            if (!routes.contains(bus.getRoute())) {
                routes.add(bus.getRoute());
            }
        }
        return routes;
    }
}
