import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**class Transformation
 * @author Matteo Falkenberg
 * @version 1.0, 06.04.2022
 */

public class Transformation extends Thread {

    private ArrayBlockingQueue<Point> queueToTransform;
    private List<Point> listTransformed;
    private int pointAmount;
    private float degree;
    private int depth;
    private int transformLimit;

    public Transformation(ArrayBlockingQueue<Point> queueToTransform, List<Point> listTransformed, int pointAmount, float degree, int depth, int transformLimit) {
        this.queueToTransform = queueToTransform;
        this.listTransformed = listTransformed;
        this.pointAmount = pointAmount;
        this.degree = degree;
        this.depth = depth;
        this.transformLimit = transformLimit;
    }


    @Override
    public void run() {

        while (listTransformed.size() < pointAmount) {
            try {
                if (!queueToTransform.isEmpty()) {
                    Point pointToTransform = transform(queueToTransform.take(), degree, depth);

                    if (pointToTransform.getTransformAmount() == transformLimit)
                        listTransformed.add(pointToTransform);
                    else
                        queueToTransform.add(pointToTransform);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    // transforms a point once/multiple times depending on depth as well as the transformAmount and transformLimit
    private Point transform(Point point, float degree, int depth) {
        int transformAmount = point.getTransformAmount();

        if (depth > 0 && transformAmount < transformLimit) {
            transformAmount++;

            float transX = (float) (point.getX() * Math.cos(Math.toRadians(degree)) - point.getY() * Math.sin(Math.toRadians(degree)));
            float transY = (float) (point.getX() * Math.sin(Math.toRadians(degree)) + point.getY() * Math.cos(Math.toRadians(degree)));
            Point transPoint = transform(new Point(transX, transY, transformAmount), degree, depth - 1);
            return transPoint;
        }
        else
            return point;
    }

}
