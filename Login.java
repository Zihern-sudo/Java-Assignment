package cinematicket2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.PrintWriter;

public class Login {

    private static final String FILE_NAME = "UserInfo.txt"; // File where user data is stored
    private String enteredUserId;

    public boolean authenticateUser() {
        Scanner scanner = new Scanner(System.in);

        String storedUserId = null;
        String storedPassword = null;

        System.out.print("Please enter your user ID: ");
        String enteredUserId = scanner.nextLine().trim(); // Remove leading/trailing spaces
        System.out.print("Please enter your password: ");
        String enteredPassword = scanner.nextLine().trim(); // Remove leading/trailing spaces

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            new PrintWriter("orderhistory.txt").close();
            String line;
            storedUserId = null;
            storedPassword = null;
            boolean isUserIdLine = true;

            while ((line = reader.readLine()) != null) {
                if (isUserIdLine) {
                    storedUserId = line.trim(); // Remove leading/trailing spaces
                } else {
                    storedPassword = line.trim(); // Remove leading/trailing spaces
                    String decryptedPassword = decryptPassword(storedPassword);

                    // Check if entered user ID and password match stored values
                    if (enteredUserId.equals(storedUserId) && enteredPassword.equals(decryptedPassword)) {
                        System.out.println("Login successful!");
                        System.out.println("");
                        this.setEnteredUserId(enteredUserId);
                        return true;
                    }

                    // Reset for the next user
                    storedUserId = null;
                    storedPassword = null;
                }

                isUserIdLine = !isUserIdLine; // Toggle between user ID and password lines
            }
            System.out.println("Login failed. Please check your user ID and password.");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void setEnteredUserId(String enteredUserId) {
        this.enteredUserId = enteredUserId;
    }

    public String getEnteredUserId() {
        return enteredUserId;
    }

    private String decryptPassword(String encryptedPassword) {

        int shiftAmount = 2; // Should match the encryption shift amount
        StringBuilder decryptedPassword = new StringBuilder();

        for (char c : encryptedPassword.toCharArray()) {
            char decryptedChar = (char) (c - shiftAmount);
            decryptedPassword.append(decryptedChar);
        }

        return decryptedPassword.toString();
    }
}
