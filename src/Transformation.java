import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**class Transformation
 * @author Matteo Falkenberg
 * @version 1.1, 10.05.2022
 */

public class Transformation extends Thread {

    private ArrayBlockingQueue<Point3D> queueToTransform;
    private List<Point3D> listTransformed;
    private int pointAmount;
    private float degree;

    public Transformation(ArrayBlockingQueue<Point3D> queueToTransform, List<Point3D> listTransformed, int pointAmount, float degree) {
        this.queueToTransform = queueToTransform;
        this.listTransformed = listTransformed;
        this.pointAmount = pointAmount;
        this.degree = degree;
    }


    @Override
    public void run() {

        while (listTransformed.size() < pointAmount) {
            try {
                if (!queueToTransform.isEmpty()) {
                    Point3D point3DToTransform = transform(queueToTransform.take(), degree);

                    if (point3DToTransform.isFullyTransformed())
                        listTransformed.add(point3DToTransform);
                    else
                        queueToTransform.add(point3DToTransform);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    // transforms a point once/multiple times depending on depth as well as the transformAmount and transformLimit
    private Point3D transform(Point3D point3D, float degree) {

        if (!point3D.isTransformedZ()) {
            float pointTransform[] = point3D.getTransform();
            float zTransform[][] = {
                    {(float)Math.cos(Math.toRadians(degree)), (float)-Math.sin(Math.toRadians(degree)), 0f, 0f},
                    {(float)Math.sin(Math.toRadians(degree)), (float)Math.cos(Math.toRadians(degree)), 0f, 0f},
                    {0f, 0f, 1f, 0f},
                    {0f, 0f, 0f, 1f},
            };

            //System.out.println("\nTransform 1 (z):");

            float newTransform[] = matrixMultiplication(pointTransform, zTransform);

            point3D.setTransform(newTransform);
            point3D.setTransformedZ(true);
        }
        else if (!point3D.isTransformedY()) {
            float pointTransform[] = point3D.getTransform();
            float yTransform[][] = {
                    {(float)Math.cos(Math.toRadians(degree)), 0f, (float)Math.sin(Math.toRadians(degree)), 0f},
                    {0f, 1f, 0f, 0f},
                    {(float)-Math.sin(Math.toRadians(degree)), 0f, (float)Math.cos(Math.toRadians(degree)), 0f},
                    {0f, 0f, 0f, 1f},
            };

            //System.out.println("\nTransform 2 (y):");

            float newTransform[] = matrixMultiplication(pointTransform, yTransform);

            point3D.setTransform(newTransform);
            point3D.setTransformedY(true);
        }
        else if (!point3D.isTransformedX()) {
            float pointTransform[] = point3D.getTransform();
            float xTransform[][] = {
                    {1f, 0f, 0f, 0f},
                    {0f, (float)Math.cos(Math.toRadians(degree)), (float)-Math.sin(Math.toRadians(degree)), 0f},
                    {0f, (float)Math.sin(Math.toRadians(degree)), (float)Math.cos(Math.toRadians(degree)), 0f},
                    {0f, 0f, 0f, 1f},
            };

            //System.out.println("\nTransform 3 (x):");

            float newTransform[] = matrixMultiplication(pointTransform, xTransform);

            point3D.setTransform(newTransform);
            point3D.setTransformedX(true);
        }

        return point3D;
    }


    private float[] matrixMultiplication(float pointTransform[], float axisTransform[][]) {
        float newTransform[] = new float[4];

        for (int i = 0; i < 4; i++) {
            newTransform[i] = 0;
            for (int k = 0; k < 4; k++) {
                newTransform[i] += axisTransform[i][k] * pointTransform[k];
            }
            //System.out.println(newTransform[i]);  //printing matrix element
        }

        return newTransform;
    }

}
