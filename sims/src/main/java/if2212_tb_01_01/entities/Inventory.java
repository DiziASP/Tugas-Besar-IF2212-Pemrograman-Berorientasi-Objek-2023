package if2212_tb_01_01.entities;

import java.util.*;
import if2212_tb_01_01.objects.*;

public class Inventory<T extends Objek> {
    private Map<T, Integer> inventory;

    public Inventory() {
        inventory = new HashMap<T, Integer>();
    }

    public Map<T,Integer> getInventory() {
        return inventory;
    }
    
    public boolean isKosong() {
        return inventory.isEmpty();
    }

    public int jumlahItem(T item) {
        if (inventory.containsKey(item)) {
            return inventory.get(item);
        }
        else {
            return 0;
        }
    }

    public void addItem (T item, int quantity) {
        if (inventory.containsKey(item)) {
            inventory.put(item, inventory.get(item) + quantity);
        }
        else {
            inventory.put(item, quantity);
        }
    }

    public void removeItem (T item, int quantity) {
        if (inventory.containsKey(item)) {
            int remainder = inventory.get(item) - quantity;
            if (remainder > 1) {
                inventory.put(item, remainder);
            }
            else if (remainder == 0) {
                inventory.remove(item);
            }
            else {
                System.out.println("Insufficient Materials!");
            }
        }
        else {
            System.out.println("No item(s) to remove!");
        }
    }

    public void clearIsi () {
        inventory.clear();
    }

    public void displayInventory() {
        System.out.println ("Inventory: ");
        if (inventory.isEmpty()) {
            System.out.println("Inventory is Empty");
        }
        else {
            for (Map.Entry<T, Integer> entry : inventory.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
    }

}   