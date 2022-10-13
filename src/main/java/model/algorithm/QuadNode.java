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
        return topLeft.getX() <= pos.getX() && pos.getX() <= bottomRight.getX()
                && topLeft.getY() >= pos.getY() && pos.getY() >= bottomRight.getY();
    }

    public boolean hasOverlap(Point topLeft, Point bottomRight) {
        // Checks if the two rectangles have any overlap

        // If the bottom of this is above
        // Or if the top of this is below
        // Or if the left of this is right
        // Or if the right of this is left
        // Then it has no overlap
        return !(topLeft.getY() < this.bottomRight.getY() || bottomRight.getY() > this.topLeft.getY()
                || topLeft.getX() > this.bottomRight.getX() || bottomRight.getX() < this.topLeft.getX());
    }

    public boolean whollyWithin(Point topLeft, Point bottomRight) {
        // Checks if this quad is wholly within the provided rectangle
        return (topLeft.getY() >= this.topLeft.getY() && bottomRight.getY() <= this.bottomRight.getY()
                && topLeft.getX() <= this.topLeft.getX() && bottomRight.getX() >= this.bottomRight.getX());
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
