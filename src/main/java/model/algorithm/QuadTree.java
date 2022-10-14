package model.algorithm;

import model.base.Listlike;
import model.base.Mergeable;
import model.base.Point;

import java.util.ArrayList;

public class QuadTree<T extends Mergeable<? super T> & Listlike<E>, E extends Point> {
    // This thing is an abomination, next time I should just hardcode this nonsense
    private QuadNode<T> root;
    private final double minDim = 0.0009; // 1 lat/long is 110.6 km, resolution of around 100m here
    public int debugging_counter = 0;

    public QuadTree(Point topLeft, Point bottomRight) {
        this.root = new QuadNode<>(null, topLeft, bottomRight);
    }

    public void insert(T item, Point pos) { // Nice and shiny
        if(!root.inBounds(pos)) {
            System.out.println(pos.getX() + ":" + pos.getY());
            throw new IndexOutOfBoundsException("Point outside of bounding box of QuadTree!");
        }
        if(item == null) throw new IllegalArgumentException("Cannot insert element of type \"null\"");
        insert(item, pos, root);
    }

    private void insert(T item, Point pos, QuadNode<T> curr) { // Not so nice

        // Remember, "item" here has its own internal list
        if((curr.topLeft.getY() - curr.bottomRight.getY()) >= minDim && (curr.bottomRight.getX() - curr.topLeft.getX()) >= minDim) {
            // The thing is not too small

            double mid_x = (curr.topLeft.getX() + curr.bottomRight.getX()) / 2;
            double mid_y = (curr.topLeft.getY() + curr.bottomRight.getY()) / 2;

            if(pos.getY() >= mid_x) { // Top Half
                if(pos.getX() <= mid_y) { // Left Half
                    if(curr.neighbours[0] == null) {
                        curr.neighbours[0] = new QuadNode<>(null,
                                new Point(curr.topLeft), new Point(mid_x, mid_y));
                    }

                    insert(item, pos, curr.neighbours[0]);
                }
                else { // Right Half
                    if(curr.neighbours[1] == null) {
                        curr.neighbours[1] = new QuadNode<>(null,
                                new Point(mid_x, curr.topLeft.getY()), new Point(curr.bottomRight.getX(), mid_y));
                    }

                    insert(item, pos, curr.neighbours[1]);
                }
            }
            else { // Bottom Half
                if(pos.getX() <= mid_y) { // Left Half
                    if(curr.neighbours[2] == null) {
                        curr.neighbours[2] = new QuadNode<>(null,
                                new Point(curr.topLeft.getX(), mid_y), new Point(mid_x, curr.bottomRight.getY()));
                    }

                    insert(item, pos, curr.neighbours[2]);
                }
                else { // Right Half
                    if(curr.neighbours[3] == null) {
                        curr.neighbours[3] = new QuadNode<>(null,
                                new Point(mid_x, mid_y), new Point(curr.bottomRight));
                    }

                    insert(item, pos, curr.neighbours[3]);
                }
            }
        }
        // Note that I can't do "early stopping" of the recursion here nicely, because of the superstore thing
        // It's still possible, but I'll only do it if it's necessary

        else if(curr.isEmpty()) { // We can't subdivide further
            // Oh hey, it's conveniently empty, don't mind if I do
            curr.setItem(item);
        }
        else { // We can't subdivide further
            //Oh well, guess I'll merge
            curr.getItem().merge(item); // This is always safe, I mean, it really should be
        }
    }

    public ArrayList<E> rangeQuery(Point topLeft, Point bottomRight) {
        ArrayList<E> out = new ArrayList<>();
        rangeQuery(out, root, topLeft, bottomRight);
        return out;
    }

    private void rangeQuery(ArrayList<E> out, QuadNode<T> curr, Point topLeft, Point bottomRight) {
        // For when the quad is not fully inside the query
        if(curr.isEmpty()) {
            for(QuadNode<T> child : curr.neighbours) {
                if(child == null); // This is intended, and kinda ugly, but works
                else if(child.whollyWithin(topLeft, bottomRight)) rangeQuery(out, child);
                else if(child.hasOverlap(topLeft, bottomRight)) rangeQuery(out, child, topLeft, bottomRight);
            }
        }
        else {
            // Check if the stuff inside actually is inside the bounds
            for(E item : curr.getItem().listOut()) {
                if(item.isInside(topLeft, bottomRight)) out.add(item);
            }
        }
    }

    private void rangeQuery(ArrayList<E> out, QuadNode<T> curr) {
        // For when the query fully covers the quad
        if (curr.isEmpty()) {
            for (QuadNode<T> child : curr.neighbours) {
                if(child != null) rangeQuery(out, child);
            }
            return;
        }
        out.addAll(curr.getItem().listOut());
    }

    public void printAll(QuadNode<T> curr) {
        if(curr.isEmpty()) {
            for (QuadNode<T> child : curr.neighbours) {
                if(child != null) printAll(child);
            }
        }
        else {
            System.out.print(curr.bottomRight);
            System.out.print(" : ");
            System.out.println(curr.topLeft);
            debugging_counter += curr.getItem().listOut().size();
            // Wait, so I am traversing correctly?
        }
    }

    public QuadNode<T> getRoot() {
        return root;
    }
}
