package cinematicket2;

import java.util.Scanner;

public class Cinematicket2 {

    private static Login login;
    private static CheckSeats checkSeats;
    private static SeatManager seatManager;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Register register = new Register();
        login = new Login();
        CheckSeats checkSeats = new CheckSeats(7, 10);
        SeatManager seatManager = new SeatManager(7, 10, 4);
        Admin admin = new Admin();
        ManageBooking manageBooking = new ManageBooking();

        int transactionCount = 0;

        System.out.println("__________           .___   _________.__.__                      _________ .__                              ");
        System.out.println("\\______   \\ ____   __| _/  /   _____/|__|  |___  __ ___________  \\_   ___ \\|__| ____   ____   _____ _____   ");
        System.out.println(" |       _// __ \\ / __ |   \\_____  \\ |  |  |\\  \\/ // __ \\_  __ \\ /    \\  \\/|  |/    \\_/ __ \\ /     \\\\__  \\  ");
        System.out.println(" |    |   \\  ___// /_/ |   /        \\|  |  |_\\   /\\  ___/|  | \\/ \\     \\___|  |   |  \\  ___/|  Y Y  \\/ __ \\_");
        System.out.println(" |____|_  /\\___  >____ |  /_______  /|__|____/\\_/  \\___  >__|     \\______  /__|___|  /\\___  >__|_|  (____  /");
        System.out.println("        \\/     \\/     \\/          \\/                   \\/                \\/        \\/     \\/      \\/     \\/ ");

        boolean isLoggedIn = false; // Flag to check if the user is logged in

        while (true) {
            if (!isLoggedIn) {
                // If the user is not logged in, show login and register options
                System.out.println("Cinema Ticketing System");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Admin");
                System.out.println("0. Exit");

                int logchoice = -1;

                do {
                    System.out.print("Enter your choice: ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Invalid input.");
                        scanner.nextLine(); // Consume invalid input
                    }
                    logchoice = scanner.nextInt();
                    if (logchoice > 3 && logchoice <= 9) {
                        System.out.println("Invalid input.");
                    }
                    scanner.nextLine(); // Consume newline
                } while (logchoice < 0 || logchoice > 3);

                switch (logchoice) {
                    case 1:
                        // User selected login
                        boolean isAuthenticated = login.authenticateUser();
                        if (isAuthenticated) {
                            //   System.out.println("Login successful!");
                            isLoggedIn = true; // Set the flag to true upon successful login
                            transactionCount = 0;
                        } else {
                            System.out.println("Authentication failed. Please try again.");
                        }
                        break;

                    case 2:
                        // User selected register
                        register.register();
                        break;

                    case 3:

                        admin.login();

                        break;

                    case 0:
                        // User selected exit
                        System.out.println("Exiting the Cinema Ticketing System.");
                        scanner.close();
                        System.exit(0);
                        break; // Exit the program

                    default:
                        System.out.println("Invalid option. Please choose again.");
                }
            } else {
                // If the user is logged in, show the main menu options
                int choice = -1;
                System.out.println("Cinema Ticketing System");
                System.out.println("1. View Movies List");
                System.out.println("2. Check Seat Available");
                System.out.println("3. Add Booking");
                System.out.println("4. Manage Booking");
                System.out.println("5. Checkout");
                System.out.println("0. Logout and Exit");
                System.out.println();

                while (true) {
                    System.out.print("Enter your choice: ");
                    if (scanner.hasNextInt()) {
                        choice = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        break; // Break the loop if valid input is received
                    } else {
                        System.out.println("Invalid input. Please enter a valid numeric choice.");
                        scanner.nextLine(); // Consume the invalid input
                    }
                }
                String enteredUserId = login.getEnteredUserId();
                switch (choice) {
                    case 1:
                        MovieList.browseMovieList();
                        break;

                    case 2: // Option to check seats
                        System.out.print("Enter the hall number (1, 2, 3, or 4): ");
                        int checkHallNumber = scanner.nextInt();
                        System.out.print("Enter the timeslot (1, 2, 3, or 4): ");
                        int checkTimeslot = scanner.nextInt();

                        // Prompt for hall and timeslot
                        if (checkHallNumber >= 1 && checkHallNumber <= 4 && checkTimeslot >= 1 && checkTimeslot <= 4) {
                            checkSeats.displayHall(checkHallNumber, checkTimeslot);
                        } else {
                            System.out.println("Invalid hall number or timeslot.");
                        }
                        break;

                    case 3: // Option to book a seat
                        System.out.print("Enter the hall number (1, 2, 3, or 4): ");
                        int hallNumber = scanner.nextInt();
                        System.out.print("Enter the timeslot (1, 2, 3, or 4): ");
                        int timeslot = scanner.nextInt();
                        System.out.print("Enter the row number: ");
                        int row = scanner.nextInt();
                        System.out.print("Enter the column number: ");
                        int column = scanner.nextInt();

                        // Prompt for hall and timeslot
                        if (hallNumber >= 1 && hallNumber <= 4 && timeslot >= 1 && timeslot <= 4) {

                            boolean bookingResult = seatManager.bookSeat(hallNumber, timeslot, row, column);

                            if (bookingResult) {
                                transactionCount++;
                                enteredUserId = login.getEnteredUserId();
                                int bookedHall = hallNumber;
                                int bookedTimeslot = timeslot;
                                int bookedRow = row;
                                int bookedColumn = column;
                                SeatManager.addBookingToOrderHistory(enteredUserId, bookedHall, bookedTimeslot, bookedRow, bookedColumn);
                            }
                        } else {
                            System.out.println("Invalid hall number or timeslot.");
                        }

                        break;

                    case 4: // Option to remove booking
                        // Call displayUserBookings and get the user's choice
                        String userChoice = manageBooking.displayUserBookings(login.getEnteredUserId());

                        if (userChoice != null) {
                            String[] bookingToCancel = userChoice.split(" ");
                            hallNumber = Integer.parseInt(bookingToCancel[0]);
                            timeslot = Integer.parseInt(bookingToCancel[1]);
                            row = Integer.parseInt(bookingToCancel[2]);
                            column = Integer.parseInt(bookingToCancel[3]);

                            // Call the cancelUserBooking method to cancel the selected booking
                            boolean cancellationResult = manageBooking.cancelUserBooking(login.getEnteredUserId(), hallNumber, timeslot, row, column);

                            if (cancellationResult) {
                                System.out.println("Booking canceled successfully!");
                                transactionCount--;
                            } else {
                                System.out.println("Failed to cancel booking.");
                            }
                        }

                        break;

                    case 5:

                        System.out.println("Are you sure you want to purchase " + transactionCount + " tickets?");

                        Purchase purchase = new Purchase(transactionCount);
                        purchase.displayPrice();
                        purchase.selectPaymentMethod();
                        break;
                    case 0:
                        // User selected logout and exit
                        System.out.println("Logging out and exiting the system.");
                        isLoggedIn = false; // Set the flag to false to return to the login menu
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }
}
