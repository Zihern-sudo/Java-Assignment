/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cinematicket2;

/**
 *
 * @author Acer
 */
import java.io.*;

public class PaymentIdGenerator {
    private static final String FILE_NAME = "payment_ids.txt";

    public static String generatePaymentID() {
        int lastPaymentID = getLastPaymentID();
        int newPaymentID = lastPaymentID + 1;
        savePaymentID(newPaymentID);
        return String.format("%08d", newPaymentID);
    }

    private static int getLastPaymentID() {
        int lastPaymentID = 0;	
        File file = new File(FILE_NAME);
        
        if(!file.exists()){
            return lastPaymentID;
        }
            
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lastPaymentID = Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lastPaymentID;
    }

    private static void savePaymentID(int paymentID) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(Integer.toString(paymentID));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

