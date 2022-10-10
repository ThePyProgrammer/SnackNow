package model.algorithm;

import model.base.Node;
import model.base.Point;

public class QuadNode<T> extends Node<T> {

    Point itemPos;
    Point topLeft;
    Point bottomRight;

    QuadNode<T>[] neighbours;

    // For the neighbours array, 0 is top left, 1 is top right, 2 is bottom left, 3 is bottom right

    public QuadNode(T item, Point itemPos, Point topLeft, Point bottomRight) {
        super(item, 4);

        this.itemPos = itemPos;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.neighbours = new QuadNode[4];
    }

    public QuadNode(T item, Point itemPos, Point topLeft, Point bottomRight, int num_neighbours) {
        super(item, num_neighbours);

        this.itemPos = itemPos;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.neighbours = new QuadNode[num_neighbours];
    }

    public QuadNode(QuadNode<T> n) {
        super(n);
        this.itemPos = n.itemPos;
        this.topLeft = n.topLeft;
        this.bottomRight = n.bottomRight;
        this.neighbours = n.neighbours;
    }

    public boolean inBounds(Point pos) {
        return topLeft.x <= pos.x && pos.x <= bottomRight.x
                && topLeft.y >= pos.y && pos.y >= bottomRight.y;
    }

    public boolean hasOverlap(Point topLeft, Point bottomRight) {
        // Checks if the two rectangles have any overlap
        return !(topLeft.y < this.bottomRight.y || bottomRight.y > this.topLeft.y
                || topLeft.x > this.bottomRight.x || bottomRight.x < this.topLeft.x);
    }

    public boolean whollyWithin(Point topLeft, Point bottomRight) {
        // Checks if this quad is wholly within the provided rectangle
        return (topLeft.y >= this.topLeft.y && bottomRight.y <= this.bottomRight.y
                && topLeft.x <= this.topLeft.x && bottomRight.x >= this.bottomRight.x);
    }

    public void setItem(T value, Point newPos) {
        setItem(value);
        itemPos = newPos;
    }

    public void clearItem() {
        setItem(null);
        itemPos = null;
    }

    public void clearItem(T value) {
        throw new IllegalArgumentException("Do not actually use the version of clearItem in QuadNode with an argument,"
                + " it exists only to be overridden in MinNode.");
    }

    public boolean isEmpty() {
        return getItem() == null;
    }

    public String serialiseNode() throws NoSuchMethodException {
        // the idea here would be to default to appending to the first, and then every time you want to swap to the next
        // quarter, you have a special symbol inserted. Need to figure this out later
        // MASSIVE TODO

        throw new NoSuchMethodException("Not implemented yet because I am bad");
    }
}
