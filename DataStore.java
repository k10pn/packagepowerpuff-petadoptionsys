package petadoptionsystem;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataStore {
    private final Path folder = Paths.get("data");
    private final ExecutorService saver = Executors.newSingleThreadExecutor();
    public void save(AdopterProfile profile, List<Appointment> appointments) {
        saver.submit(() -> saveNow(profile, appointments)); 
    }
    
    public synchronized void saveNow(AdopterProfile profile, List<Appointment> appointments) {
        try { Files.createDirectories(folder); 
        if (profile != null) 
            Files.write(folder.resolve("profile.txt"), Collections.singletonList(profile.toFileLine()));
            List<String> lines = new ArrayList<>(); 
            for (Appointment a : appointments) lines.add(a.toFileLine()); 
                Files.write(folder.resolve("appointments.txt"), lines); 
        }
        catch (IOException exception) {
            System.err.println("Could not save data: " + exception.getMessage()); 
        }
    }
    public AdopterProfile loadProfile() {
        Path file = folder.resolve("profile.txt");
        try {
            if (Files.exists(file)) {
                List<String> lines = Files.readAllLines(file); 
                if (!lines.isEmpty()) 
                    return AdopterProfile.fromFileLine(lines.get(0)); 
            } 
        }
        catch (Exception exception) {
            System.err.println("Could not load profile: " + exception.getMessage()); 
        }
        return null;
    }
    
    public List<String> loadAppointmentLines() {
        try { Path file = folder.resolve("appointments.txt"); 
        if (Files.exists(file)) 
            return Files.readAllLines(file); 
        }
        catch (IOException exception) {
            System.err.println("Could not load appointments: " + exception.getMessage()); 
        }
        return new ArrayList<>();
    }
    
    public void close() {
        saver.shutdown(); 
    }
}
