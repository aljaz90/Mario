package cyou.equinox;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;

public class Vector2 implements Serializable {
    private int x;
    private int y;

    public static Vector2 ZERO = new Vector2(0, 0);

    public  Vector2() { x = 0; y = 0; }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;

        return x == vector2.x && y == vector2.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public JSONObject toJsonObject() {
        JSONObject objectToReturn = new JSONObject();

        objectToReturn.put("x", x);
        objectToReturn.put("y", y);

        return objectToReturn;
    }

    public String toString() {
        return "Vector2(" + x + ", " + y + ")";
    }

    public static double distance(Vector2 a, Vector2 b) {
        return Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2));
    }
}
