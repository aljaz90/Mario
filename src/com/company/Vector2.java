package com.company;

public class Vector2 {
    private int x;
    private int y;

    public static Vector2 ZERO = new Vector2(0, 0);

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static double distance(Vector2 a, Vector2 b) {
        return Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2));
    }

    public String toString() {
        return "Vector2(" + x + ", " + y + ")";
    }
}
