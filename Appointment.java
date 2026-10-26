package petadoptionsystem;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    public enum Status { SCHEDULED, COMPLETED, CANCELLED }
    private final String id, petId; 
    private final Pet pet; 
    private final AdopterProfile profile;
    private final LocalDate date; 
    private final LocalTime time; 
    private Status status;
    
    public Appointment(String id, Pet pet, AdopterProfile profile, LocalDate date, LocalTime time, Status status) {
        this.id = id; 
        this.pet = pet; 
        this.petId = pet.getId(); 
        this.profile = profile; 
        this.date = date;
        this.time = time; 
        this.status = status;
    }
    
    public String getId() {
        return id; 
    } 
    
    public Pet getPet() {
        return pet; 
    } 
    
    public AdopterProfile getProfile() {
        return profile; 
    }
    
    public LocalDate getDate() {
        return date; 
    } 
    
    public LocalTime getTime() {
        return time; 
    } 
    
    public Status getStatus() {
        return status; 
    }
    
    public void setStatus(Status status) {
        this.status = status; 
    }
    
    public String summary() {
        return date + " " + time + " | " + pet.getName() + " | " + status; 
    }
    
    public String details() {
        return "Appointment: " + id + "\nPet: " + pet.getName() + " (" + pet.getSpecies() + ")\nAdopter: " + profile.getFullName() + "\nDate: " + date + "\nTime: " + time + "\nStatus: " + status; 
    }
    
    public String toFileLine() {
        return String.join("|", id, petId, profile.toFileLine().replace("|", "~"), date.toString(), time.toString(), status.toString()); 
    }
    
    public static String normalizeProfile(String encoded) {
        return encoded.replace("~", "|"); 
    }
}
