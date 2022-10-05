public class QuadTree<T extends Comparable<? super T> & Mergeable<? super T>> {
    private QuadNode<T> root;
    private final double minDim = 0.0009; // 1 lat/long is 110.6 km, resolution of around 100m here

    public QuadTree(Point topLeft, Point bottomRight) {
        this.root = new QuadNode<>(null, null, topLeft, bottomRight);
    }

    public void insert(T item, Point pos) { // Nice and shiny
        if(!root.inBounds(pos)) {
            throw new IndexOutOfBoundsException("Point outside of bounding box of QuadTree!");
        }
        if(item == null) {
            throw new IllegalArgumentException("Cannot insert element of type \"null\"");
        }
        insert(item, pos, root);
    }

    private void insert(T item, Point pos, QuadNode<T> curr) { // Not so nice

        // Remember, "item" here has its own internal list
        if((curr.topLeft.y - curr.bottomRight.y) >= minDim && (curr.bottomRight.x - curr.topLeft.x) >= minDim) {
            // The thing is not too small

            double mid_x = (curr.topLeft.x + curr.bottomRight.x) / 2;
            double mid_y = (curr.topLeft.y + curr.bottomRight.y) / 2;

            Point currPos = curr.itemPos;

            if(pos.y >= mid_x) { // Top Half
                if(pos.x <= mid_y) { // Left Half
                    if(curr.neighbours[0] == null) {
                        curr.neighbours[0] = new QuadNode<>(null, null,
                                new Point(curr.topLeft), new Point(mid_x, mid_y));
                    }

                    insert(item, pos, (QuadNode<T>) curr.neighbours[0]);
                }
                else { // Right Half
                    if(curr.neighbours[1] == null) {
                        curr.neighbours[1] = new QuadNode<>(null, null,
                                new Point(mid_x, curr.topLeft.y), new Point(curr.bottomRight.x, mid_y));
                    }

                    insert(item, pos, (QuadNode<T>) curr.neighbours[1]);
                }
            }
            else { // Bottom Half
                if(pos.x <= mid_y) { // Left Half
                    if(curr.neighbours[2] == null) {
                        curr.neighbours[2] = new QuadNode<>(null, null,
                                new Point(curr.topLeft.x, mid_y), new Point(mid_x, curr.bottomRight.y));
                    }

                    insert(item, pos, (QuadNode<T>) curr.neighbours[2]);
                }
                else { // Right Half
                    if(curr.neighbours[3] == null) {
                        curr.neighbours[3] = new QuadNode<>(null, null,
                                new Point(mid_x, mid_y), new Point(curr.bottomRight));
                    }

                    insert(item, pos, (QuadNode<T>) curr.neighbours[3]);
                }
            }
        }

        else if(curr.isEmpty()) { // We can't subdivide further

            // Oh hey, it's conveniently empty, don't mind if I do
            curr.setItem(item, pos);
        }
        else { // We can't subdivide further

            //Oh well, guess I'll merge
            curr.setItem((T) curr.getItem().Merge(item)); // This is always safe, I mean, it really should be
        }
    }
}
