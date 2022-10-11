package model.algorithm;

import model.base.ListLike;
import model.base.Mergeable;

import java.util.ArrayList;

public class SuperStore implements Mergeable<SuperStore>, ListLike<Item> {
    public ArrayList<Item> allItems;
    // It is absolutely critical that allItems is always kept in alphabetical order

    public SuperStore() {
        this.allItems = new ArrayList<>();
    }

    public SuperStore(ArrayList<Item> allItems) {
        this.allItems = allItems;
    }

    public void addItem(Item item) {
        for(int i = 0; i < allItems.size(); i++) {
            if(allItems.get(i).itemName.equals(item.itemName)) {
                allItems.set(i, allItems.get(i).minimizeCost(item));
                return;
            }
        }
        allItems.add(item);

        // This basically just adds an item, except if it already exists, in which case it takes the one which costs less
        // There is no removal method, because that doesn't really make sense
    }

    @Override
    public SuperStore merge(SuperStore store) {
        ArrayList<Item> out = new ArrayList<>();
        int p1 = 0;
        int p2 = 0;

        // TODO("Must sort all items")

        while(p1 != allItems.size() && p2 != store.allItems.size()) {
            Item item1 = allItems.get(p1);
            Item item2 = store.allItems.get(p2);

            if(item1.compareTo(item2) == 0) {
                if(item1.price > item2.price) {
                    p1++;
                }
                else {
                    p2++;
                }
                // Break the equality chain eventually
            }
            else if(item1.compareTo(item2) > 0) {
                // If p1 is ahead, change that
                out.add(item2);
                p2++;
            }
            else {
                // Similar
                out.add(item1);
                p1++;
            }
        }

        while(p1 != allItems.size()) {
            out.add(allItems.get(p1));
            p1++;
        }

        while(p2 != store.allItems.size()) {
            out.add(store.allItems.get(p2));
            p2++;
        }

        return new SuperStore(out);
        //TODO hope and pray that this actually works
    }

    public ArrayList<Item> listOut() {
        return allItems;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        for(Item item : allItems) {
            builder.append(item.toString());
        }

        return builder.toString();
    }
}
