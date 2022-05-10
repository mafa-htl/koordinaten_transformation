import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.*;

/**class Main
 * @author Matteo Falkenberg
 * @version 1.0, 06.04.2022
 */

public class Main {

    public static void main(String[] args) {

        // amount of points and thread safe containers
        int pointAmount = 1000000;   // amount of points to be created
        ArrayBlockingQueue<Point> queueToBeTransformed = new ArrayBlockingQueue<>(pointAmount);   // contains points to be transformed
        List<Point> listTransformed = new Vector<>();   // contains points that finished transformation

        // amount of threads and ThreadPool
        int transformationThreadAmount = 3;
        ExecutorService executor = Executors.newFixedThreadPool(transformationThreadAmount);

        // variables for Transformation threads
        float transformDegrees = 5f;   // degrees per transform
        int transformDepth = 5;   // depth of recursive transform function
        int transformLimit = 5;   // max amount of transforms that should be carried out

        // filling ThreadPool with Transformation threads
        for (int i = 0; i < transformationThreadAmount; i++) {
            executor.execute(new Transformation(queueToBeTransformed, listTransformed, pointAmount, transformDegrees, transformDepth, transformLimit));
        }

        // variables for creation of points
        int pointsCreated = 0;
        Random r = new Random();
        float xMax = 12f;
        float yMax = 24f;

        // signal/track when the process actually starts
        System.out.println("Start creating points and transforming them ...\n");
        long startTime = System.currentTimeMillis();   // to calculate the time taken for the processes

        // create pointAmount random points and put them into queueToBeTransformed
        while (pointsCreated < pointAmount) {
            float x = r.nextFloat() * xMax;
            float y = r.nextFloat() * yMax;
            Point point = new Point(x, y, 0);
            queueToBeTransformed.add(point);

            pointsCreated++;
        }

        // signal/track completion of point creation
        long timeTakenCreate = System.currentTimeMillis() - startTime;
        System.out.println("Time taken to create points: " + timeTakenCreate + " ms");

        /*
           #######################################################################################################
           #    --------------------------------     D I S C L A I M E R     --------------------------------    #
           # sometimes not all threads finish, even-though everything should be thread safe and working properly #
           #######################################################################################################
        */

        // shutdown executor (ThreadPool) after all threads have finished + signal/track completion of transformations
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);   // Long.MAX_VALUE -> wait forever (until Threads finished)
            long timeTakenTransform = System.currentTimeMillis() - startTime;
            System.out.println("Time taken to transform points: " + timeTakenTransform + " ms");
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
