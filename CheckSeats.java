package cinematicket2;

import java.io.*;

public class CheckSeats {

    private int numRows;
    private int numColumns;

    public CheckSeats(int numRows, int numColumns) {
        this.numRows = numRows;
        this.numColumns = numColumns;
    }

    public void displayHall(int hallNumber, int timeslot) {
    if (hallNumber >= 1 && hallNumber <= 4 && timeslot >= 1 && timeslot <= 4) {
        String fileName = "hall" + hallNumber + "time" + timeslot + ".txt";
        char[][] seatsToDisplay = loadSeatsFromFile(fileName);

        if (seatsToDisplay != null) {
            System.out.println("Seat Map for Hall " + hallNumber + " - Timeslot " + timeslot + ":");
            System.out.println("=============SCREEN===========");

            // Display column indicators
            System.out.print("   ");
            for (int col = 0; col < numColumns; col++) {
                System.out.print(col + 1 + " ");
            }
            System.out.println();

            for (int row = 0; row < numRows; row++) {
                System.out.print((row + 1) + "  "); // Row indicator
                for (int col = 0; col < numColumns; col++) {
                    System.out.print(seatsToDisplay[row][col] + " ");
                }
                System.out.println();
            }
            System.out.println("==DOOR==================DOOR==");
        } else {
            System.out.println("Failed to load seat data from file: " + fileName);
        }
    } else {
        System.out.println("Invalid hall number or timeslot.");
    }
}



    private char[][] loadSeatsFromFile(String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("File does not exist: " + fileName);
            return null;
        }

        char[][] seatsToLoad = new char[numRows][numColumns];

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int row = 0;

            while ((line = reader.readLine()) != null && row < numRows) {
                if (line.length() == numColumns) {
                    seatsToLoad[row] = line.toCharArray();
                    row++;
                }
            }

            return seatsToLoad;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
