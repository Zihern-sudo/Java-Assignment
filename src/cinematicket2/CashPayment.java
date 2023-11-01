/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cinematicket2;

/**
 *
 * @author Acer
 */
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class CashPayment extends Purchase {

    private String phoneNumber;
    private String customerName;
    private double cashPay; 
    private double balance; 

    String paymentID = PaymentIdGenerator.generatePaymentID();
    Scanner scanner = new Scanner(System.in);
    public CashPayment(int transactionCount) {
        super(transactionCount);
        

        System.out.println("\n\n\n==========  Cash Payment Page   ==========\n");
        System.out.println(" --Please enter your payment details : --");

        System.out.print("Enter your name: ");
        customerName = scanner.nextLine();

        //ask user to enter the contact number 
        do {
            System.out.print("Enter your contact number (9-11 digits): +60");
            phoneNumber = scanner.nextLine().trim().replace(" ", ""); // Remove spaces
        } while (!phoneNumber.matches("^\\d{9,11}$")); // Check if the input is 9 to 11 digits

        do {
            System.out.print("Enter the amount to pay: RM ");
            while (!scanner.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a numeric value.");
                scanner.next();
            }
            cashPay = scanner.nextDouble();

            if (cashPay < subtotal) {
                System.out.println("Payment amount must be greater than or equal to the subtotal.");
            }
        } while (cashPay < subtotal);

        balance = cashPay - this.subtotal; // Calculate balance

        // Date Time formatting
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        // use generateReceipt Method，trsf formattedDateTime & paymentID
        generateReceipt(formattedDateTime, paymentID);

        try {
            String filePath = "CashPaymentRecords.txt";
            // Create a FileWriter object to write to the file (true parameter appends to the file)
            FileWriter fileWriter = new FileWriter(filePath, true);

            // Write the user's details to the file
            fileWriter.write("#+60");
            fileWriter.write(phoneNumber);
            fileWriter.write("#");
            fileWriter.write(customerName);
            fileWriter.write("#(price>)");
            fileWriter.write(Double.toString(this.subtotal));
            fileWriter.write("(pay>)");
            fileWriter.write(Double.toString(cashPay));
            fileWriter.write("#(balance>)");
            fileWriter.write(Double.toString(balance));
            fileWriter.write("#(PaymentID>)");
            fileWriter.write(paymentID); // Write the cash payment ID
            fileWriter.write("#");
            fileWriter.write(formattedDateTime); // Write the cash payment ID
            fileWriter.write(System.lineSeparator());  // Go to a new line

            fileWriter.close();
            Cinematicket2.main(null);

            // Close the scanner
            scanner.close();
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    public void generateReceipt(String formattedDateTime, String paymentID) {
        // Display receipt information
        System.out.println("\n\n==========================   RECEIPT   ==========================");
        System.out.println("Below is your receipt:\n");
        System.out.println("Payment ID\t: " + paymentID); // Display the provided paymentID
        System.out.println("Payment Make\t: " + formattedDateTime); // Display the provided formattedDateTime
        System.out.println("Payment Method\t: Cash\n");
        displayPrice();
        System.out.println("\n");
        System.out.println("-- Paid by Cash --");
        System.out.println("Amount Paid\t: RM " + cashPay);
        System.out.println("Subtotal Price\t: RM " + this.subtotal);
         System.out.println("Balance\t\t: RM " + String.format("%.2f", this.balance));
        System.out.println("==================================================================");
        System.out.println("Thank you for purchase!");
        System.out.println("Please come again.");
        
        System.out.println("Press anything to continue...");
        
        scanner.nextLine();
        scanner.nextLine();
        
    }
}
