package cinematicket2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

public class Register {

    private String encryptPassword(String password) {
        // Implement your encryption logic here, e.g., shifting characters by a fixed amount
        int shiftAmount = 2; // You can adjust this value
        StringBuilder encryptedPassword = new StringBuilder();

        for (char c : password.toCharArray()) {
            char encryptedChar = (char) (c + shiftAmount);
            encryptedPassword.append(encryptedChar);
        }

        return encryptedPassword.toString();
    }

    public void register() {
        Scanner scanner = new Scanner(System.in);

        String userId;
        String password;

        do {
            System.out.print("Please enter your user ID: ");

            userId = scanner.nextLine().trim();
            if (userId.contains(" ")) {
                System.out.println("User ID cannot contain spaces. Please try again.");
            } else if (userId.isEmpty()) {
                System.out.println("User ID cannot be empty. Please try again.");
            } else if (userExists("UserInfo.txt", userId)) {
                System.out.println("User ID already exists. Please choose a different one.");
            }
        } while (userId.contains(" ") || userId.isEmpty() || userExists("UserInfo.txt", userId));

        do {

            System.out.print("Please enter your password: ");
            password = scanner.nextLine().trim();
            if (password.contains(" ")) {
                System.out.println("Password cannot contain spaces. Please try again.");
            } else if (password.isEmpty()) {
                System.out.println("Password cannot be empty. Please try again.");
            } else if (password.length() <= 5) {
                System.out.println("Password must be longer than 5 characters. Please try again.");
            } else if (!containsNumericCharacter(password)) {
                System.out.println("Password must contain at least one numeric character. Please try again.");
            }
        } while (password.contains(" ") || password.isEmpty() || password.length() <= 5 || !containsNumericCharacter(password));

        String encryptedPassword = encryptPassword(password);

        writeToTextFile("UserInfo.txt", userId, encryptedPassword);

        System.out.println("User ID and password registered successfully.");

    }

    private boolean userExists(String fileName, String userId) {
        File file = new File(fileName);
        
        if (!file.exists()){
            return false;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean checkUserId = false;

            while ((line = reader.readLine()) != null) {
                String storedUserId = line.trim();
                if (userId.equals(storedUserId)) {
                    checkUserId = true; // User ID already exists
                }
            }

            return checkUserId;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // User ID does not exist
    }

    public void writeToTextFile(String fileName, String userId, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Write data to the file
            writer.write(userId);
            writer.newLine();
            writer.write(password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean containsNumericCharacter(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

}
