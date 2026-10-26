package petadoptionsystem;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class AdoptionService {
    private static final int PAGE_SIZE = 5;
    private final List<Pet> pets = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();
    private final Set<String> occupiedSlots = new HashSet<>();
    private final DataStore store = new DataStore();
    private AdopterProfile profile;

    public AdoptionService() { seedPets(); loadSavedData(); }
    private void seedPets() {
        pets.add(new Dog("D001", "Bubbles", "Puppy (2 weeks old)", "Female", "Aspin", "Brown and white", "Small", "Short", "House-trained", 800, true));
        pets.add(new Cat("C001", "Blossom", "Adult (2 years old)", "Female", "Puspin", "Calico", "Small", "Short", "House-trained", 600, true));
        pets.add(new Dog("D002", "Buttercup", "Adult (3 years old)", "Female", "Shih Tzu", "White", "Small", "Long", "Special needs: regular grooming", 2500, true));
        pets.add(new Cat("C002", "Mojo", "Adult (1 year old)", "Male", "Maine Coon", "Gray", "Large", "Long", "House-trained", 4200, true));
        pets.add(new Dog("D003", "Sugar", "Adult (4 years old)", "Female", "Golden Retriever", "Gold", "Large", "Medium", "House-trained", 3500, true));
        pets.add(new Cat("C003", "Puff", "Kitten (3 months old)", "Male", "Siamese", "Cream", "Small", "Short", "Special needs: quiet room", 1800, true));
        pets.add(new Dog("D004", "Ace", "Adult (2 years old)", "Male", "German Shepherd", "Black and tan", "Large", "Medium", "House-trained", 3900, true));
        pets.add(new Cat("C004", "Fifi", "Adult (5 years old)", "Female", "British Shorthair", "Blue-gray", "Medium", "Short", "House-trained", 2200, true));
        pets.add(new Dog("D005", "Taffy", "Puppy (4 months old)", "Male", "Pomeranian", "Orange", "Small", "Long", "Special needs: daily brushing", 3000, true));
        pets.add(new Cat("C005", "Ming", "Adult (3 years old)", "Male", "Puspin", "Black", "Small", "Short", "House-trained", 400, true));
        pets.add(new Dog("D006", "Kiko", "Adult (6 years old)", "Male", "Aspin", "White", "Medium", "Short", "House-trained", 300, true));
        pets.add(new Cat("C006", "Peach", "Adult (2 years old)", "Female", "Puspin", "Orange", "Small", "Short", "House-trained", 500, true));
    }
    private void loadSavedData() {
        profile = store.loadProfile();
        Map<String, Pet> petMap = new HashMap<>();
        for (Pet pet : pets) petMap.put(pet.getId(), pet);
        for (String line : store.loadAppointmentLines()) {
            try {
                String[] p = line.split("\\|", -1);
                if (p.length != 6 || !petMap.containsKey(p[1])) continue;
                AdopterProfile adopter = AdopterProfile.fromFileLine(Appointment.normalizeProfile(p[2]));
                Appointment appointment = new Appointment(p[0], petMap.get(p[1]), adopter,
                        LocalDate.parse(p[3]), LocalTime.parse(p[4]), Appointment.Status.valueOf(p[5]));
                appointments.add(appointment);
                if (appointment.getStatus() != Appointment.Status.CANCELLED) {
                    occupiedSlots.add(appointment.getDate() + "@" + appointment.getTime());
                    petMap.get(p[1]).setAvailable(false);
                }
            } catch (RuntimeException ignored) { /* Ignore malformed old records and keep the application usable. */ }
        }
    }
    public List<Pet> getPage(int page, String category) {
        List<Pet> filtered = filtered(category); 
        int start = page * PAGE_SIZE; 
        if (start >= filtered.size()) return Collections.emptyList(); 
        return new ArrayList<>(filtered.subList(start, Math.min(start + PAGE_SIZE, filtered.size()))); 
    }
    
    public int pageCount(String category) {
        return Math.max(1, (filtered(category).size() + PAGE_SIZE - 1) / PAGE_SIZE); 
    }
    
    private List<Pet> filtered(String category) {
        List<Pet> result = new ArrayList<>(); 
        for (Pet p : pets) 
            if (p.isAvailable() && (category.equals("ALL") || p.getSpecies().equalsIgnoreCase(category))) 
                result.add(p); 
        return result; 
    }
    
    public List<Pet> getAvailablePets() {
        return filtered("ALL"); 
    }
    
    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments); 
    }
    
    public AdopterProfile getProfile() {
        return profile; 
    } 
    
    public void setProfile(AdopterProfile p) {
        profile = p; saveAsync(); 
    }
    
    public synchronized void bookAppointment(Pet pet, AdopterProfile adopter, LocalDate date, LocalTime time) {
        if (adopter.getAge() < 18) 
            throw new IllegalArgumentException("You must be at least 18 years old to adopt.");
        if (!Arrays.asList(LocalTime.of(8,0), LocalTime.of(10,0), LocalTime.of(14,0)).contains(time)) 
            throw new IllegalArgumentException("That time is not available.");
        String key = date + "@" + time; 
        if (occupiedSlots.contains(key)) 
            throw new IllegalArgumentException("That date and time are already unavailable.");
        appointments.add(new Appointment("A" + String.format("%03d", appointments.size() + 1), pet, adopter, date, time, Appointment.Status.SCHEDULED)); 
        occupiedSlots.add(key); 
        pet.setAvailable(false); 
        profile = adopter; 
        saveAsync();
    }
    
    public synchronized void cancelAppointment(String id) {    
            for (Appointment a : appointments) 
            if (a.getId().equals(id) && a.getStatus() == Appointment.Status.SCHEDULED) {
                a.setStatus(Appointment.Status.CANCELLED); 
                occupiedSlots.remove(a.getDate() + "@" + a.getTime()); 
                a.getPet().setAvailable(true); 
                saveAsync(); 
                return; 
            } 
    }
    
    public void markCompleted(String id) { 
            for (Appointment a : appointments) 
            if (a.getId().equals(id)) {    
                a.setStatus(Appointment.Status.COMPLETED); 
                saveAsync(); return; 
            }
    }
    
    private void saveAsync() {
        store.save(profile, appointments); 
    }
    
    public void saveNow() {
        store.saveNow(profile, appointments); 
        store.close(); 
    }
}
