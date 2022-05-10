/**class Point
 * @author Matteo Falkenberg
 * @version 1.0, 06.04.2022
 */

public class Point {

    private float x;
    private float y;
    private int transformAmount;

    public Point(float x, float y, int transformAmount) {
        this.x = x;
        this.y = y;
        this.transformAmount = transformAmount;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getTransformAmount() {
        return transformAmount;
    }

}
