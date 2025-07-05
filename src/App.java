import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Contracts.Shippable;
import Implementation.Cart;
import Implementation.Customer;
import Implementation.NonPerishableProduct;
import Implementation.PerishableProduct;
import Implementation.Product; 
import Implementation.ShippableProduct;
import Implementation.ShippingService;

public class App {
    public static void main(String[] args) {
        PerishableProduct cheese = new PerishableProduct("Cheese", 100, 10, 
            LocalDate.now().plusDays(10));
        NonPerishableProduct biscuits = new NonPerishableProduct("Biscuits", 150, 5);
        ShippableProduct tv = new ShippableProduct("TV", 5000, 3, 7.5);
        NonPerishableProduct scratchCard = new NonPerishableProduct("Scratch Card", 50, 20);

        Customer customer = new Customer("Ahmed", 1000);

        Cart cart = new Cart();
        cart.add(cheese, 2);
        cart.add(biscuits, 1);
        cart.add(scratchCard, 1);

        checkout(customer, cart);
    }

    public static void checkout(Customer customer, Cart cart) {
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            if (product.isExpired()) {
                throw new IllegalStateException(product.getName() + " is expired");
            }
            if (product.getQuantity() < quantity) {
                throw new IllegalStateException("Not enough stock for " + product.getName());
            }
        }

        double shippingFees = cart.getItems().keySet().stream()
                .filter(p -> p instanceof Shippable)
                .count() * 10;

        double subtotal = cart.calculateSubtotal();
        double total = subtotal + shippingFees;

        if (customer.getBalance() < total) {
            throw new IllegalStateException("Insufficient balance");
        }

        List<Shippable> shippableItems = new ArrayList<>();
        cart.getItems().forEach((product, quantity) -> {
            if (product instanceof Shippable) {
                for (int i = 0; i < quantity; i++) {
                    shippableItems.add((Shippable) product);
                }
            }
        });

        if (!shippableItems.isEmpty()) {
            new ShippingService().shipItems(shippableItems);
        }

        System.out.println("Checkout receipt");
        cart.getItems().forEach((product, quantity) -> {
            System.out.printf("%dx %s    %.0f%n", quantity, product.getName(), 
                product.getPrice() * quantity);
        });
        System.out.println("---");
        System.out.printf("Subtotal    %.0f%n", subtotal);
        System.out.printf("Shipping    %.0f%n", shippingFees);
        System.out.printf("Amount    %.0f%n%n", total);

        customer.setBalance(customer.getBalance() - total);
        cart.getItems().forEach((product, quantity) -> {
            product.setQuantity(product.getQuantity() - quantity);
        });

        System.out.printf("Customer %s's new balance: %.0f%n", 
            customer.getName(), customer.getBalance());
        System.out.println("END.");
    }
}
