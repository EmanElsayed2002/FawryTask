package Implementation;

public abstract class Product {
    private String name;
    private double price;
    private int quanity;
    
    public Product(String name , double price , int quantity) {
        this.name = name;
        this.price = price;
        this.quanity = quantity;
    }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quanity; }
    public void setQuantity(int quantity) { this.quanity = quantity;}
    public abstract boolean isExpired();

}
