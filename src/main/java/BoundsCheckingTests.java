import model.algorithm.QuadNode;
import model.algorithm.SuperStore;
import model.base.Point;

public class BoundsCheckingTests {

    public static void main(String[] args) {
        QuadNode<SuperStore> node = new QuadNode<>(null, new Point(0.0025, 0.0075), new Point(0.0075, 0.0025));

        System.out.println(node.whollyWithin(new Point(0, 0.01), new Point(0.01, 0)));
        System.out.println(node.hasOverlap(new Point(0, 0.0075), new Point(0.0025, 0.0070)));

        Point p = new Point(0.001, 0.008);
        System.out.println(p.isInside(new Point(0.00050, 0.0085), new Point(0.0015, 0.00095)));
        // Ok wait this works, so now I have a propagation error somewhere?
    }
}
