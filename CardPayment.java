/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cinematicket2;

import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CardPayment extends Purchase {

    private String cardNumber;
    private String cardCategory;
    private String cvvCode;
    private String cardHolderName;

    String filePath = "CardPaymentRecords.txt";

    public CardPayment(int transactionCount) {
        super(transactionCount);
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n\n\n==========  Card Payment Page   ==========");
        System.out.println(" -- Please enter your card details: --");

        getCardDetails(scanner);

        displayCardInfo();

        confirmPurchse();
        writeReceipt();

        scanner.close();
    }

    // method get card number and category
    private void getCardDetails(Scanner scanner) {
        //enter card number
        while (true) {
            System.out.print("Enter a 16-digit card number (with/without spacing acceptable): ");
            cardNumber = scanner.nextLine().replaceAll("\\s", "");
            if (cardNumber.length() == 16 && cardNumber.matches("\\d+")) {
                char firstDigit = cardNumber.charAt(0);
                switch (firstDigit) {
                    case '3':
                        cardCategory = "American Express";
                        break;
                    case '4':
                        cardCategory = "Visa";
                        break;
                    case '6':
                        cardCategory = "MasterCard";
                        break;
                    case '8':
                        cardCategory = "UnionPay";
                        break;
                    default:
                        cardCategory = "Unknown";
                }
                String maskedCardNumber = maskCardNumber(cardNumber);
                System.out.println("Card Category: " + cardCategory);
                break; //if card number entered with valid, go out
            } else {
                System.out.println("Invalid card number. Please enter a valid 16-digit card number.");
            }
        }

        // enter cvv code
        while (true) {
            System.out.print("Enter CCV/CVC code (3 digits): ");
            cvvCode = scanner.next();

            if (cvvCode.length() == 3 && cvvCode.matches("\\d+")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter exactly 3 digits.");
            }
        }

        // move to next line ( otherwise it cant enter name)
        scanner.nextLine();

        // enter card holder name
        System.out.print("Enter card holder's name: ");
        cardHolderName = scanner.nextLine();
    }

    // display information
    private void displayCardInfo() {
        System.out.println("--------------------------------------------------");
        System.out.println("Please confirm your information below:");
        System.out.println("Card 16-digit number\t: " + cardNumber);
        System.out.println("Card category\t\t: " + cardCategory);
        System.out.println("Card CCV/CVC code\t: " + cvvCode);
        System.out.println("Card holder name\t: " + cardHolderName);
        System.out.println("--------------------------------------------------");
    }

    // confirm purchase
    private void confirmPurchse() {
        boolean validInput = false;
        Scanner scanner = new Scanner(System.in);

        while (!validInput) {
            System.out.print("Do you want to proceed (Y/N)? ");
            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("Y")) {
                generateReceipt();
                System.out.println("Press anything to continue...");
                scanner.nextLine();
                validInput = true;  // Exit the loop when valid input is received
            } else if (userInput.equalsIgnoreCase("N")) {
                System.out.println("Returning to main menu.");
                Cinematicket2.main(null); // Exit the loop when valid input is received
                validInput = true;
            } else {
                System.out.println("Invalid input. Please enter 'Y' or 'N'.");
            }
        }
    }
    String paymentID = PaymentIdGenerator.generatePaymentID();

    private void writeReceipt() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        try {
            FileWriter fileWriter = new FileWriter(filePath, true);

            //fileWriter.write("PaymentID>");
            //fileWriter.write(paymentID);
            //fileWriter.write("#");
            fileWriter.write(cardNumber);
            fileWriter.write("#");
            fileWriter.write(cardCategory);
            fileWriter.write("#");
            fileWriter.write(cvvCode);
            fileWriter.write("#");
            fileWriter.write(cardHolderName);
            fileWriter.write("#");
            //fileWriter.write(price);
            //fileWriter.write("#");
            fileWriter.write(formattedDateTime);
            fileWriter.write(System.lineSeparator());

            fileWriter.close();
            Cinematicket2.main(null);

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    public void generateReceipt() {
        // get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        // Display receipt information
        System.out.println("\n\n==========================   RECEIPT   ==========================");
        System.out.println("Below is your receipt:\n");
        //System.out.println("User ID\t\t:");
        System.out.println("Payment ID\t: " + paymentID);
        System.out.println("Payment Make\t: " + formattedDateTime);
        System.out.println("Payment Method\t: Card\n");
        super.displayPrice();
        System.out.println("\n");
        System.out.println("Paid by Card : [" + maskCardNumber(cardNumber) + "]");
        System.out.println("==================================================================");
        System.out.println("Thank you for purchase!");
        System.out.println("Please come again.");
    }

    // hide the middle card number with '*' and 1st 2 last 2 display out
    private static String maskCardNumber(String cardNumber) {
        if (cardNumber.length() == 16) {
            String masked = cardNumber.substring(0, 2) + "************" + cardNumber.substring(12);
            return masked;
        } else {
            return "Invalid Card Number";
        }
    }
}
