package petadoptionsystem;

public class AdopterProfile {
    private String fullName, gender, address, livingSituation, phoneNumber;
    private int age;

    public AdopterProfile(String fullName, String gender, String address, int age, String livingSituation) {
        this(fullName, gender, address, age, livingSituation, "00000000000");
    }
    
    public AdopterProfile(String fullName, String gender, String address, int age, String livingSituation, String phoneNumber) {
        update(fullName, gender, address, age, livingSituation, phoneNumber);
    }
    
    public void update(String fullName, String gender, String address, int age, String livingSituation) {
        update(fullName, gender, address, age, livingSituation, "00000000000");
    }
    
    public void update(String fullName, String gender, String address, int age, String livingSituation, String phoneNumber) {
        if (isBlank(fullName) || isBlank(gender) || isBlank(address) || isBlank(livingSituation)
                || phoneNumber == null || !phoneNumber.matches("\\d{10,11}")
                || age < 1 || age > 120)
            throw new IllegalArgumentException("All profile fields must be valid.");
        this.fullName = fullName; 
        this.gender = gender; 
        this.address = address;
        this.age = age;
        this.livingSituation = livingSituation;
        this.phoneNumber = phoneNumber;
    }
    
    private boolean isBlank(String value) { 
        return value == null || value.trim().isEmpty(); 
    }
    
    public String getFullName() { 
        return fullName; 
    } 
    
    public String getGender() { 
        return gender; 
    }
    
    public String getAddress() { 
        return address; 
    } 
    
    public int getAge() {
        return age; 
    }
    
    public String getLivingSituation() { 
        return livingSituation;
    }
    
    public String getPhoneNumber() {
        return phoneNumber; 
    }
    
    public String toFileLine() { 
        return String.join("|", fullName, gender, address, String.valueOf(age), livingSituation, phoneNumber); 
    }
    
    public static AdopterProfile fromFileLine(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length >= 6) {
            return new AdopterProfile(p[0], p[1], p[2], Integer.parseInt(p[3]), p[4], p[5]);
        }
        return new AdopterProfile(p[0], p[1], p[2], Integer.parseInt(p[3]), p[4]);
    }
}
