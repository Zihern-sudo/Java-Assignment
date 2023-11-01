/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cinematicket2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SeatManager {

    //Login login = new Login();
    private List<List<char[][]>> halls = new ArrayList<>();
    private int numRows;
    private int numColumns;

    public SeatManager(int numRows, int numColumns, int numHalls) {
        this.numRows = numRows;
        this.numColumns = numColumns;

        // Initialize seats for all halls if the file doesn't exist
        for (int hallNumber = 1; hallNumber <= numHalls; hallNumber++) {
            List<char[][]> hallSeats = new ArrayList<>();

            for (int timeslot = 1; timeslot <= 4; timeslot++) {
                String fileName = "hall" + hallNumber + "time" + timeslot + ".txt";
                char[][] hall = new char[numRows][numColumns];
                boolean fileExists = loadSeatsFromFile(fileName, hall);

                if (!fileExists) {
                    createFileWithInitialData(fileName); // Create and initialize the file
                }

                hallSeats.add(hall);
            }

            halls.add(hallSeats);
        }

    }

    private char[][] initializeSeats(int numRows, int numColumns) {
        char[][] seats = new char[numRows][numColumns];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                seats[i][j] = 'O'; // Initialize each seat as 'O'
            }
        }
        return seats;
    }

    public boolean bookSeat(int hallNumber, int timeslot, int row, int column) {
        if (hallNumber >= 1 && hallNumber <= halls.size() && timeslot >= 1 && timeslot <= 4) {
            List<char[][]> hallSeats = halls.get(hallNumber - 1);
            char[][] seatsToBook = hallSeats.get(timeslot - 1); // Get the hall array

            // Adjust row and column to 0-based index for array manipulation
            int adjustedRow = row - 1;
            int adjustedColumn = column - 1;

            if (adjustedRow >= 0 && adjustedRow < numRows && adjustedColumn >= 0 && adjustedColumn < numColumns) {
                if (seatsToBook[adjustedRow][adjustedColumn] == 'O') {
                    seatsToBook[adjustedRow][adjustedColumn] = 'X'; // 'X' represents a booked seat
                    System.out.println("Seat booked successfully!");

                    // Update the seat data in the file for the specific hall and timeslot
                    updateSeatDataInFile("hall" + hallNumber + "time" + timeslot + ".txt", seatsToBook, hallNumber);

                    return true;
                } else {
                    System.out.println("Seat is already booked.");
                }
            } else {
                System.out.println("Invalid row or column.");
            }
        } else {
            System.out.println("Invalid hall number or timeslot.");
        }
        return false;
    }

    public static void addBookingToOrderHistory(String enteredUserId, int hallNumber, int timeslot, int row, int column) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("orderhistory.txt", true))) {
            // Append the booking information to the order history file
            String bookingInfo = enteredUserId + "#" + hallNumber + "#" + timeslot + "#" + row + "#" + column;
            writer.write(bookingInfo);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to update order history.");
        }
    }

    private void updateSeatDataInFile(String fileName, char[][] hall, int hallNumber) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (char[] row : hall) {
                writer.write(row);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to update seat data in the file for Hall " + hallNumber + ".");
        }
    }

    public boolean loadSeatsFromFile(String fileName, char[][] seatsToLoad) {
        File file = new File(fileName);

        if (!file.exists()) {
            return false; // Return false to indicate that the file does not exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int row = 0;

            while ((line = reader.readLine()) != null && row < numRows) {
                if (line.length() == numColumns) {
                    seatsToLoad[row] = line.toCharArray();
                    row++;
                }
            }
            return true; // Return true to indicate successful loading
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Return false to indicate an error occurred while loading
        }
    }

    private void createFileWithInitialData(String fileName) {
        char[][] initialSeats = initializeSeats(numRows, numColumns);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (char[] row : initialSeats) {
                writer.write(row);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean removeBooking(String adminID, int hallNumber, int timeslot, int row, int column) {
        // Check if the admin ID is valid (you can compare it to a list of valid admin IDs)
        if (!isAdmin(adminID)) {
            System.out.println("Admin authorization failed.");
            return false;
        }

        // Check if the hall number, timeslot, row, and column are valid
        if (!isValidBookingInput(hallNumber, timeslot, row, column)) {
            System.out.println("Invalid booking details.");
            return false;
        }


        char[][] seats = halls.get(hallNumber - 1).get(timeslot - 1);
        int adjustedRow = row - 1;
        int adjustedColumn = column - 1;

        if (seats[adjustedRow][adjustedColumn] == 'X') {
            seats[adjustedRow][adjustedColumn] = 'O'; // Mark the seat as available
            updateSeatDataInFile("hall" + hallNumber + "time" + timeslot + ".txt", seats, hallNumber);
            return true;
        } else {
            System.out.println("Seat is not booked.");
            return false;
        }
    }

    private boolean isAdmin(String adminID) {
       
        return adminID.equals("admin8086"); // Example: Check if the admin ID matches a known value
    }

    private boolean isValidBookingInput(int hallNumber, int timeslot, int row, int column) {

        boolean isValidHallNumber = hallNumber >= 1 && hallNumber <= halls.size();
        boolean isValidTimeslot = timeslot >= 1 && timeslot <= 4;
        boolean isValidRow = row >= 1 && row <= numRows;
        boolean isValidColumn = column >= 1 && column <= numColumns;

        return isValidHallNumber && isValidTimeslot && isValidRow && isValidColumn;
    }

    //MANAGEBOOKING
    public boolean cancelBooking(String enteredUserId, int hallNumber, int timeslot, int row, int column) {
        // Check if the entered user ID matches the user ID in the booking history

        // Check if the booking details (hall, timeslot, row, column) are valid
        if (!isValidBookingInput(hallNumber, timeslot, row, column)) {
            System.out.println("Invalid booking details.");
            return false;
        }


        char[][] seats = halls.get(hallNumber - 1).get(timeslot - 1);
        int adjustedRow = row - 1;
        int adjustedColumn = column - 1;

        if (seats[adjustedRow][adjustedColumn] == 'X') {
            seats[adjustedRow][adjustedColumn] = 'O'; // Mark the seat as available
            updateSeatDataInFile("hall" + hallNumber + "time" + timeslot + ".txt", seats, hallNumber);
            removeBookingFromOrderHistory(enteredUserId, hallNumber, timeslot, row, column);
            return true;
        } else {
            System.out.println("Seat is not booked.");
            return false;
        }
    }

    private void removeBookingFromOrderHistory(String enteredUserId, int hallNumber, int timeslot, int row, int column) {
        // Define the file name for the order history
        String orderHistoryFileName = "orderhistory.txt";

        try {
            // Read the existing order history file
            List<String> orderHistoryLines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(orderHistoryFileName));
            String line;

            while ((line = reader.readLine()) != null) {
                // Split each line into user ID, hall number, timeslot, row, and column
                String[] bookingInfo = line.split("#");

                if (bookingInfo.length == 5) {
                    String userId = bookingInfo[0];
                    int bookedHallNumber = Integer.parseInt(bookingInfo[1]);
                    int bookedTimeslot = Integer.parseInt(bookingInfo[2]);
                    int bookedRow = Integer.parseInt(bookingInfo[3]);
                    int bookedColumn = Integer.parseInt(bookingInfo[4]);

                    // Check if the booking matches the one to be canceled
                    if (userId.equals(enteredUserId) && bookedHallNumber == hallNumber
                            && bookedTimeslot == timeslot && bookedRow == row && bookedColumn == column) {
                        // Skip this booking line to effectively remove it
                        continue;
                    }
                }

                // Add the current line to the updated order history
                orderHistoryLines.add(line);
            }

            // Close the reader
            reader.close();

            // Write the updated order history back to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(orderHistoryFileName));
            for (String updatedBookingInfo : orderHistoryLines) {
                writer.write(updatedBookingInfo);
                writer.newLine();
            }

            // Close the writer
            writer.close();

            System.out.println("Booking removed from order history.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to remove booking from order history.");
        }
    }

    public boolean markSeatAsAvailable(int hallNumber, int timeslot, int row, int column) {
        // Check if the hall number, timeslot, row, and column are valid
        if (!isValidBookingInput(hallNumber, timeslot, row, column)) {
            System.out.println("Invalid booking details.");
            return false;
        }


        char[][] seats = halls.get(hallNumber - 1).get(timeslot - 1);
        int adjustedRow = row - 1;
        int adjustedColumn = column - 1;

        if (seats[adjustedRow][adjustedColumn] == 'X') {
            seats[adjustedRow][adjustedColumn] = 'O'; // Mark the seat as available
            updateSeatDataInFile("hall" + hallNumber + "time" + timeslot + ".txt", seats, hallNumber);
            return true;
        } else {
            System.out.println("Seat is not booked.");
            return false;
        }
    }

}
