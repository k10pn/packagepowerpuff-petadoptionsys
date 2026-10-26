package petadoptionsystem;

public class AdopterProfile {
    private String fullName, gender, address, livingSituation;
    private int age;

    public AdopterProfile(String fullName, String gender, String address, int age, String livingSituation) {
        update(fullName, gender, address, age, livingSituation);
    }
    public void update(String fullName, String gender, String address, int age, String livingSituation) {
        if (isBlank(fullName) || isBlank(gender) || isBlank(address) || isBlank(livingSituation) || age < 1 || age > 120)
            throw new IllegalArgumentException("All profile fields must be valid.");
        this.fullName = fullName; this.gender = gender; this.address = address; this.age = age; this.livingSituation = livingSituation;
    }
    private boolean isBlank(String value) { return value == null || value.trim().isEmpty(); }
    public String getFullName() { return fullName; } public String getGender() { return gender; }
    public String getAddress() { return address; } public int getAge() { return age; }
    public String getLivingSituation() { return livingSituation; }
    public String toFileLine() { return String.join("|", fullName, gender, address, String.valueOf(age), livingSituation); }
    public static AdopterProfile fromFileLine(String line) { String[] p = line.split("\\|", -1); return new AdopterProfile(p[0], p[1], p[2], Integer.parseInt(p[3]), p[4]); }
}
