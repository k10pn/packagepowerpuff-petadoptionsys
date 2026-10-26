package petadoptionsystem;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class ConsoleMain {
    private final Scanner scanner = new Scanner(System.in);
    private final AdoptionService service = new AdoptionService();

    public static void main(String[] args) {
        new ConsoleMain().run();
    }

    private void run() {
        boolean running = true;
        while (running) {
            System.out.println("\n "
                    + "============================= \n \t "
                    + "PET SHELTER \n "
                    + "=============================");
            System.out.println("[1] Find a Pet / Browse");
            System.out.println("[2] List of Appointments");
            System.out.println("[3] Fill in Information");
            System.out.println("[4] Exit and Save");
            
            int choice = InputValidator.readInt(scanner, "Choose: ", 1, 4);
            
            switch (choice) {
                case 1: browse(); 
                    break;
                case 2: appointments(); 
                    break;
                case 3: collectProfile(); 
                    break;
                case 4: service.saveNow(); 
                    running = false; 
                    System.out.println("Data saved. Goodbye!"); 
                    break;
                default: 
                    break;
            }
        }
    }

    private void browse() {
        int page = 0;
        while (true) {
            List<Pet> pets = service.getPage(page, "ALL");
            System.out.println("\n "
                    + "============================= "
                    + "\n \t AVAILABLE PETS " + (page + 1) + "/" + service.pageCount("ALL") 
                    + "\n =============================");
            for (int i = 0; i < pets.size(); i++) 
                System.out.println((i + 1) 
                        + ". " 
                        + pets.get(i).shortDescription());
            System.out.println("[N] Next  | [P] Previous  | [B] Back");
            String answer = InputValidator.readText(scanner, "Choose: ").toUpperCase();
            if (answer.equals("N") && page + 1 < service.pageCount("ALL")) page++;
            else if (answer.equals("P") && page > 0) page--;
            else if (answer.equals("B")) return;
            else if (answer.matches("[1-5]") && Integer.parseInt(answer) <= pets.size()) adopt(pets.get(Integer.parseInt(answer) - 1));
            else System.out.println("Invalid choice.");
        }
    }

    private void adopt(Pet pet) {
        System.out.println("\n" + pet.fullDescription());
        if (!InputValidator.yesNo(scanner, "Are you sure you want to adopt this pet? (Y/N): ")) return;
            AdopterProfile profile = service.getProfile();
        if (profile == null) 
            profile = readProfile();
        if (profile.getAge() < 18) {
            System.out.println("You must be at least 18 years old to adopt."); 
            return; 
        }
        
        LocalDate date = InputValidator.readDate(scanner, "Appointment date (DD-MM-YYYY): ");
        LocalTime time = InputValidator.readTime(scanner, "Time (08:00, 10:00, or 14:00): ");
        try {
            service.bookAppointment(pet, profile, date, time);
            System.out.println("Appointment set successfully.");
        } 
        catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage()); 
        }
    }

    private void appointments() {
        List<Appointment> list = service.getAppointments();
        if (list.isEmpty()) {
            System.out.println("No appointments found."); 
            return; 
        }
        for (int i = 0; i < list.size(); i++) 
            System.out.println((i + 1) 
                    + ". " 
                    + list.get(i).summary());
        int number = InputValidator.readInt(scanner, "Enter number to view, or 0 to go back: ", 0, list.size());
        if (number > 0) 
            System.out.println(list.get(number - 1).details());
        if (number > 0 && InputValidator.yesNo(scanner, "Cancel this appointment? (Y/N): ")) 
            service.cancelAppointment(list.get(number - 1).getId());
    }

    private void collectProfile() {
        service.setProfile(readProfile()); 
        service.saveNow(); 
        System.out.println("Information saved."); 
    }

    private AdopterProfile readProfile() {
        String name = InputValidator.readText(scanner, "Full name: ");
        String gender = InputValidator.readText(scanner, "Gender: ");
        String address = InputValidator.readText(scanner, "Address: ");
        int age = InputValidator.readInt(scanner, "Age: ", 1, 120);
        String living = InputValidator.readText(scanner, "Describe your living situation: ");
        return new AdopterProfile(name, gender, address, age, living);
    }
}
