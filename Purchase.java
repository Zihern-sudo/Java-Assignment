/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cinematicket2;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Purchase {

    public int qty;
    public double total;
    public double subtotal;

    public Purchase(int transactionCount) {
        this.qty = transactionCount;
        this.total = qty * 15.00;
        this.subtotal = total + (total * 0.05);
    }
    
    public double getSubtotal() {
        return subtotal;
    }

    public void displayPrice() {

        System.out.println("\tDetail\t\t\tRM\tQty\t|\tTOTAL");
        System.out.println("-----------------------------------------------------------------|");
        System.out.println("\tMovie Ticket\t\t15.00\t" + qty + "\t|\t" + total);
        System.out.println("-----------------------------------------------------------------|");
        System.out.println("\tTax (5%)\t\t\t\t|\t" + (total * 0.05));
        System.out.println("-----------------------------------------------------------------|");
        System.out.println("\tSubtotal\t\t\t\t|\t" + subtotal);
        System.out.println("=================================================================|");
    }

    public void selectPaymentMethod() {
        int paymentChoice;
        Scanner scanner = new Scanner(System.in);
        PaymentIdGenerator paymentIdGenerator = new PaymentIdGenerator();

        if (this.qty == 0) {
            System.out.println("Nothing inside cart. Please add booking.\n\n");
            //main(null);
        } else {
            do {
                System.out.println("Payment method:");
                System.out.println("1. Card");
                System.out.println("2. Cash");
                System.out.println("3. Cancel & back");
                System.out.print("Enter your preferred payment selection: ");

                try {
                    paymentChoice = scanner.nextInt();
                    switch (paymentChoice) {
                        case 1:
                            new CardPayment(qty);
                            break;
                        case 2:
                            new CashPayment(qty);
                            break;
                        case 3:
                            System.out.println("Main Menu.");
                            break;
                        default:
                            System.out.println("Invalid payment selection.");
                            break;
                    }
                } catch (InputMismatchException e) {
                    scanner.next();
                    System.out.println("Cannot enter characters!\n");
                    paymentChoice = 0;
                }
            } while (paymentChoice != 1 && paymentChoice != 2 && paymentChoice != 3);
        }

    }
}
