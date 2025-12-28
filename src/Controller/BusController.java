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

    public boolean addBus(Bus bus) {
        try {
            if (bus == null) {
                throw new IllegalArgumentException("Bus is null");
            }
            for (Bus existingBus : busList) {
                if (existingBus.getBusId() == bus.getBusId()) {
                    return false;
                }
                if (existingBus.getBusNumber()
                        .equalsIgnoreCase(bus.getBusNumber().trim())) {
                    return false;
                }
            }
            busList.add(bus);
            recentBuses.add(bus);
            if (recentBuses.size() > 5) {
                recentBuses.poll();
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding bus: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Bus> getAllBuses() {
        return busList;
    }

    public Queue<Bus> getRecentBuses() {
        return recentBuses;
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

    public boolean updateBus(Bus updatedBus) {
        try {
            if (updatedBus == null) {
                return false;
            }

            for (int i = 0; i < busList.size(); i++) {
                Bus existingBus = busList.get(i);

                if (existingBus.getBusId() == updatedBus.getBusId()) {

                    existingBus.setBusNumber(updatedBus.getBusNumber());
                    existingBus.setRoute(updatedBus.getRoute());
                    existingBus.setFare(updatedBus.getFare());
                    existingBus.setDepartureTime(updatedBus.getDepartureTime());

                    // Update total seats ONLY if valid
                    if (updatedBus.getAvailableSeats() >= existingBus.getAvailableSeats()) {
                        existingBus.setTotalSeats(updatedBus.getAvailableSeats());
                    }

                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            System.out.println("Error updating bus: " + e.getMessage());
            return false;
        }
    }


    public boolean bookSeat(int busId) {
        try {
            Bus bus = binarySearchById(busId);
            if (bus != null && bus.bookSeat()) {
                bookingHistory.push(bus);   // STACK PUSH
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error booking seat: " + e.getMessage());
            return false;
        }
    }


    public boolean cancelLastBooking() {
        try {
            if (!bookingHistory.isEmpty()) {
                Bus bus = bookingHistory.pop();  // STACK POP
                bus.cancelSeat();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error cancelling booking: " + e.getMessage());
            return false;
        }
    }

  
    public Bus binarySearchById(int busId) {
        try {
            sortById();

            int low = 0;
            int high = busList.size() - 1;

            while (low <= high) {
                int mid = (low + high) / 2;

                if (busList.get(mid).getBusId() == busId) {
                    return busList.get(mid);
                } else if (busList.get(mid).getBusId() < busId) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
            return null;

        } catch (Exception e) {
            System.out.println("Error searching bus: " + e.getMessage());
            return null;
        }
    }

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

    private void sortById() {
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
            System.out.println("Error calculating seats: " + e.getMessage());
            return 0;
        }
    }

    public ArrayList<String> getRoutesCovered() {
        ArrayList<String> routes = new ArrayList<>();
        try {
            for (Bus bus : busList) {
                if (!routes.contains(bus.getRoute())) {
                    routes.add(bus.getRoute());
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting routes: " + e.getMessage());
        }
        return routes;
    }
}
