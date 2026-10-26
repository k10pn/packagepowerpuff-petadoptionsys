package petadoptionsystem;

public class Dog extends Pet {
    public Dog(String id, String name, String age, 
               String gender, String breed, String color, String size,
               String coat, String care, int price, boolean available) {
        super(id, name, age, gender, breed, color, size, coat, care, price, available);
    }
    @Override public String getSpecies() { return "Dog"; }
}
