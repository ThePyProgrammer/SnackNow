public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0, 0);
    }

    public Point(Point p) {
        this(p.x, p.y);
    }

    public boolean isEqualTo(Point p) {
        return x == p.x && y == p.y;
    }

    public boolean isInside(Point topLeft, Point bottomRight) {
        // WARNING: Both edges are inclusive, if we get duplicates, it's probably this
        return (x <= topLeft.x && y <= topLeft.y && x >= bottomRight.x && y >= bottomRight.y);
    }
}
