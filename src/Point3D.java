/**class Point3D
 * @author Matteo Falkenberg
 * @version 1.1, 10.05.2022
 */

public class Point3D {

    private float transform[];

    private boolean isTransformedX;
    private boolean isTransformedY;
    private boolean isTransformedZ;

    public Point3D(float x, float y, float z) {
        this.transform = new float[]{x, y, z, 1};
    }

    public Point3D(float transform[]) {
        this.transform = transform;
    }

    public float[] getTransform() {
        return transform;
    }

    public void setTransform(float[] transform) {
        this.transform = transform;
    }


    public float getX() {
        return transform[0];
    }

    public float getY() {
        return transform[1];
    }

    public float getZ() {
        return transform[2];
    }


    public boolean isTransformedX() {
        return isTransformedX;
    }

    public boolean isTransformedY() {
        return isTransformedY;
    }

    public boolean isTransformedZ() {
        return isTransformedZ;
    }

    public void setTransformedX(boolean transformedX) {
        isTransformedX = transformedX;
    }

    public void setTransformedY(boolean transformedY) {
        isTransformedY = transformedY;
    }

    public void setTransformedZ(boolean transformedZ) {
        isTransformedZ = transformedZ;
    }


    public boolean isFullyTransformed() {
        if (isTransformedX && isTransformedY && isTransformedZ)
            return true;

        return false;
    }

}
