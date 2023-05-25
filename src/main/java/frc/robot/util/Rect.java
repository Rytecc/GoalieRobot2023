package frc.robot.util;

public class Rect {
    private double x;
    private double y;
    private double w;
    private double h;

    public Rect(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public boolean pointInRect(double x1, double y1) {
        boolean inBoundX = x1 >= x && x1 <= x + w;
        boolean inBoundY = y1 >= y && y1 <= y + h;
        return inBoundX && inBoundY;
    }
}
