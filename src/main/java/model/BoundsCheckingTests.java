package model;

import model.algorithm.Item;
import model.algorithm.QuadNode;
import model.algorithm.QuadTree;
import model.algorithm.SuperStore;
import model.base.Point;

public class BoundsCheckingTests {

    public static void main(String[] args) {
        QuadNode<SuperStore> node = new QuadNode<>(null, null, new Point(0, 0.01), new Point(0.01, 0));

        System.out.println(node.whollyWithin(new Point(0, 0.01), new Point(0.01, 0)));
        System.out.println(node.hasOverlap(new Point(0, 0.001), new Point(0.001, 0)));
    }
}
