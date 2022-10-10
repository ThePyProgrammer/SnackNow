import model.algorithm.Item;
import model.algorithm.QuadTree;
import model.algorithm.SuperStore;
import model.base.Point;

public class ExampleTestingThing {
    public static void main(String[] args) {

        QuadTree<SuperStore, Item> tree = new QuadTree<>(new Point(0, 0.01), new Point(0.01, 0));
        // Note that it's x, y, which means that the top left and bottom right are defined a little unintuitively

        SuperStore store1 = new SuperStore();
        SuperStore store2 = new SuperStore();

        store1.addItem(new Item("Cocaine", "Outside", new Point(0.003, 0.003), 50));
        store2.addItem(new Item("Cocaine", "Outside v2", new Point(0.008, 0.004), 100));

        // Note that while superstores have a position, they are implicitly min-size quads

        tree.insert(store1, new Point(0.003, 0.003));
        tree.insert(store2, new Point(0.008, 0.004));

        System.out.println(tree.rangeQuery(new Point(0, 0.01), new Point(0.01, 0)).toString());
    }
}
