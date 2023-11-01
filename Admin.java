package cinematicket2;

import java.util.Scanner;

public class Admin {

    public static final String ADMIN_ID = "admin8086";
    private static final int ADMIN_PASS = 8086;
    private boolean isLoggedIn = false;
    int numRows = 7;
    int numColumns = 10;
    int numHalls = 4;

    public void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter Admin ID: ");
        String tempID = scanner.nextLine();
        if (tempID.equals(ADMIN_ID)) {
            System.out.print("Please Enter Password: ");
            int tempPass = scanner.nextInt();
            if (tempPass == ADMIN_PASS) {
                isLoggedIn = true;
                System.out.println("\nLogged in to Admin Page");
                manageAdminActions();
            } else {
                System.out.println("\nInvalid Password!!");
            }
        } else {
            System.out.println("\nInvalid ID!!");
        }
    }

    public void manageAdminActions() {
        if (!isLoggedIn) {
            System.out.println("Please log in as admin first.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Admin Menu");
            System.out.println("1. Remove Booking");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter the hall number (1, 2, 3, or 4): ");
                    int hallNumber = scanner.nextInt();
                    System.out.print("Enter the timeslot (1, 2, 3, or 4): ");
                    int timeslot = scanner.nextInt();
                    System.out.print("Enter the row number: ");
                    int row = scanner.nextInt();
                    System.out.print("Enter the column number: ");
                    int column = scanner.nextInt();

                    // Check if the admin credentials match
                    if (removeBooking(ADMIN_ID, hallNumber, timeslot, row, column)) {
                        System.out.println("Booking removed successfully!");
                    } else {
                        System.out.println("Failed to remove booking.");
                    }

                    break;

                case 0:
                    exit = true; // Exit the admin menu
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // This method integrates with the SeatManager class to remove bookings
    private boolean removeBooking(String adminID, int hallNumber, int timeslot, int row, int column) {
        SeatManager seatManager = new SeatManager(numRows, numColumns, numHalls);
        return seatManager.removeBooking(adminID, hallNumber, timeslot, row, column);
    }
}
