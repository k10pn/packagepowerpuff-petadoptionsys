package petadoptionsystem;

public abstract class Pet {
    private final String id, name, ageDescription, gender, 
            breed, color, size, coatLength, careBehavior;
    private final int price;
    private boolean available;

    protected Pet(String id, String name, String ageDescription, String gender, 
                  String breed, String color, String size, String coatLength, 
                  String careBehavior, int price, boolean available) {
        if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty() || price < 200 || price > 5000)
            throw new IllegalArgumentException("Invalid pet data.");
        this.id = id; 
        this.name = name; 
        this.ageDescription = ageDescription; 
        this.gender = gender;
        this.breed = breed; 
        this.color = color; 
        this.size = size; 
        this.coatLength = coatLength;
        this.careBehavior = careBehavior; 
        this.price = price; 
        this.available = available;
    }

    public abstract String getSpecies();
    
    public String getId() {
        return id; 
    } 
    
    public String getName() {
        return name; 
    }
    
    public String getAgeDescription() {
        return ageDescription; 
    } 
    
    public String getGender() {
        return gender; 
    }
    
    public String getBreed() {
        return breed; 
    } 
    
    public String getColor() {
        return color; 
    }
    
    public String getSize() {
        return size; 
    } 
    
    public String getCoatLength() {
        return coatLength; 
    }
    
    public String getCareBehavior() {
        return careBehavior; 
    } 
    
    public int getPrice() {
        return price; 
    }
    
    public boolean isAvailable() {
        return available; 
    } 
    
    public void setAvailable(boolean value) {
        available = value; 
    }
    
    public String shortDescription() {
        return name + " | " + getSpecies() + " | " + breed + " | PHP " + price; 
    }
    
    public String fullDescription() { return "Name: " + name + "\nSpecies: " + getSpecies() + "\nAge: " + ageDescription
            + "\nGender: " + gender + "\nBreed: " + breed + "\nColor: " + color + "\nSize: " + size
            + "\nCoat: " + coatLength + "\nCare & behavior: " + careBehavior + "\nAdoption fee: PHP " + price; 
    }
}
