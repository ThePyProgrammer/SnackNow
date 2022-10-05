public class QuadNode<T extends Comparable<? super T>> extends Node<T> {

    Point itemPos;
    Point topLeft;
    Point bottomRight;

    // For the neighbours array, 0 is top left, 1 is top right, 2 is bottom left, 3 is bottom right

    public QuadNode(T item, Point itemPos, Point topLeft, Point bottomRight) {
        super(item, 4);

        this.itemPos = itemPos;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public QuadNode(T item, Point itemPos, Point topLeft, Point bottomRight, int num_neighbours) {
        super(item, num_neighbours);

        this.itemPos = itemPos;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public QuadNode(QuadNode<T> n) {
        super(n);
        this.itemPos = n.itemPos;
        this.topLeft = n.topLeft;
        this.bottomRight = n.bottomRight;
    }

    public boolean inBounds(Point pos) {
        return topLeft.x < pos.x && pos.x < bottomRight.x
                && topLeft.y > pos.y && pos.y > bottomRight.y;
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
        return getItem() != null;
    }
}
