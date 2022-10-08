package model.algorithm;

import model.base.Point;

public class Item extends Point implements Comparable<Item> {
    public String itemName;
    public String address;
    public double price;

    public Item(String itemName, String address, Point exactLocation, double price) {
        super(exactLocation);
        this.itemName = itemName;
        this.address = address;
        this.price = price;
    }

    public boolean isEqualTo(Item item) {
        return itemName.equals(item.itemName) && address.equals(item.itemName)
                && x == item.x && y == item.y && price == item.price;
    }

    public Item minimizeCost(Item item) { // Returns the one with the lower cost, of the two
        if(item.price < this.price) return item;
        return this;
    }

    @Override
    public int compareTo(Item item) {
        return itemName.compareTo(item.itemName);
    }
}
