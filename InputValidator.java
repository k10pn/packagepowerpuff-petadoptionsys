package petadoptionsystem;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public final class InputValidator {
    private InputValidator(){}
    
    public static String readText(Scanner s, String prompt) {
        while (true) {
            System.out.print(prompt); 
            String v = s.nextLine().trim(); 
            if (!v.isEmpty()) 
                return v; 
            System.out.println("Input cannot be blank."); 
        } 
    }
    
    public static int readInt(Scanner s, String prompt, int min, int max) {
        while (true) {
            try {
                int n = Integer.parseInt(readText(s, prompt)); 
                if (n >= min && n <= max) 
                    return n; 
            } 
            catch (NumberFormatException ignored) {} 
            System.out.println("Enter a number from " 
                    + min + " to " + max + "."); 
        } 
    }
    public static boolean yesNo(Scanner s, String prompt) {
        while (true) {
            String v = readText(s, prompt).toLowerCase(); 
            if (v.equals("y") || v.equals("yes")) 
                return true; 
            if (v.equals("n") || v.equals("no")) 
                return false; 
            System.out.println("Please enter Y or N."); 
        } 
    }
    public static LocalDate readDate(Scanner s, String prompt) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-yyyy"); 
        while (true) {
            try {
                LocalDate d = LocalDate.parse(readText(s, prompt), f); 
                if (!d.isBefore(LocalDate.now())) 
                    return d; 
            } 
            catch (DateTimeParseException ignored) {}
            System.out.println("Use DD-MM-YYYY and choose today or a future date."); 
        } 
    }
    public static LocalTime readTime(Scanner s, String prompt) {
        while (true) {
            String v = readText(s, prompt); 
            if (v.equals("08:00")) 
                return LocalTime.of(8, 0); 
            if (v.equals("10:00")) 
                return LocalTime.of(10, 0); 
            if (v.equals("14:00")) 
                return LocalTime.of(14, 0); 
            System.out.println("Available times are 08:00, 10:00, and 14:00."); 
        } 
    }
}
