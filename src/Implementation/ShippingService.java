package Implementation;

import java.util.List;

import Contracts.Shippable;

public class ShippingService {
    public void shipItems(List<Shippable> shippableItems) {
        System.out.println("Shipment notice");
        double totalWeight = 0;

        for (Shippable item : shippableItems) {
            System.out.printf("%dx %s    %.0fg%n", 
                shippableItems.stream().filter(i -> i.getName().equals(item.getName())).count(),
                item.getName(), 
                item.getWeight() * 1000);
            totalWeight += item.getWeight();
        }

        System.out.printf("Total package weight %.1fkg%n%n", totalWeight);
    }
}