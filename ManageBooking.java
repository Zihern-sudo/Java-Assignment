package cinematicket2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManageBooking {

    int numRows = 7;
    int numColumns = 10;
    int numHalls = 4;

    public String displayUserBookings(String userId) {
    try (BufferedReader reader = new BufferedReader(new FileReader("orderhistory.txt"))) {
        String line;
        List<String> userBookings = new ArrayList<>();
        boolean isFirstBooking = true; // Flag to track the first booking

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("#");
            if (parts.length == 5 && parts[0].equals(userId)) {
                int hallNumber = Integer.parseInt(parts[1]);
                int timeslot = Integer.parseInt(parts[2]);
                int row = Integer.parseInt(parts[3]);
                int column = Integer.parseInt(parts[4]);
                String bookingInfo = "Hall " + hallNumber
                        + " Timeslot " + timeslot + " Row " + row + " Column " + column;
                userBookings.add(bookingInfo);
            }
        }

        if (!userBookings.isEmpty()) {
            System.out.println("Bookings for user " + userId + ":");
            for (String bookingInfo : userBookings) {
                System.out.println(bookingInfo);
            }

            // Ask the user if they want to cancel a booking
            Scanner scanner = new Scanner(System.in);
            System.out.print("Do you want to cancel a booking (y/n)? ");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("y")) {
                // Prompt the user to enter the details of the booking to cancel
                System.out.print("Enter the details of the booking to cancel (Hall Timeslot Row Column): ");
                String[] bookingToCancel = scanner.nextLine().trim().split(" ");

                if (bookingToCancel.length == 4) {
                    return String.join(" ", bookingToCancel); // Return the user's choice
                } else {
                    System.out.println("Invalid input format. Booking was not canceled.");
                }
            } else {
                System.out.println("No bookings were canceled.");
            }
        } else {
            System.out.println("No bookings found for this user.");
        }
    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Failed to read order history.");
    }

    return null; // No booking is canceled
}


    public boolean cancelUserBooking(String userId, int hallNumber, int timeslot, int row, int column) {
    List<String> lines = new ArrayList<>();
    boolean foundBooking = false;

    try (BufferedReader reader = new BufferedReader(new FileReader("orderhistory.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("#");
            if (parts.length == 5
                    && parts[0].equals(userId)
                    && Integer.parseInt(parts[1]) == hallNumber
                    && Integer.parseInt(parts[2]) == timeslot
                    && Integer.parseInt(parts[3]) == row
                    && Integer.parseInt(parts[4]) == column) {
                foundBooking = true;
            } else {
                lines.add(line);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Failed to read order history.");
        return false;
    }

    if (!foundBooking) {
        System.out.println("Booking not found for cancellation.");
        return false;
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("orderhistory.txt"))) {
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Failed to write order history.");
        return false;
    }

    // Call SeatManager's method to mark the seat as available
    SeatManager seatManager = new SeatManager(numRows, numColumns, numHalls);
    return seatManager.markSeatAsAvailable(hallNumber, timeslot, row, column);
}



}
