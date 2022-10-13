import model.algorithm.Item;
import model.algorithm.QuadTree;
import model.algorithm.SuperStore;
import model.base.Point;

import java.util.ArrayList;

public class ExampleTestingThing {
    public static void main(String[] args) {

        QuadTree<SuperStore, Item> tree = new QuadTree<>(new Point(0, 0.01), new Point(0.01, 0));
        // Note that it's x, y, which means that the top left and bottom right are defined a little unintuitively


        // Note that while superstores have a position, they are implicitly min-size quads

        for(double i = 0; i < 0.01; i = i + 0.001) {
            for(double j = 0; j < 0.01; j = j + 0.001) {
                Point p = new Point(i, j);
                SuperStore store = new SuperStore();
                store.addItem(new Item("dummy"+i+j, "test", p, 1));
                tree.insert(store, p);
            }
        }

        System.out.println();
        System.out.println("Printing Query 1");
        printPositions(tree.rangeQuery(new Point(0.00050, 0.00815), new Point(0.0019, 0.00094)));
        System.out.println(tree.debugging_counter);
    }

    public static void printPositions(ArrayList<Item> arr) {
        System.out.println("Number of elements: " + arr.size());
        for(Item i : arr) {
            System.out.println(i.getX() + " : " + i.getY());
        }
    }
}
