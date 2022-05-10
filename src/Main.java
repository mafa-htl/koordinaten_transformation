import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.*;

/**class Main
 * @author Matteo Falkenberg
 * @version 1.1, 10.05.2022
 */

public class Main {

    public static void main(String[] args) {

        // amount of points and thread safe containers
        int pointAmount = 1000000;   // amount of points to be created
        //pointAmount = 1;
        ArrayBlockingQueue<Point3D> queueToBeTransformed = new ArrayBlockingQueue<>(pointAmount);   // contains points to be transformed
        List<Point3D> listTransformed = new Vector<>();   // contains points that finished transformation

        // amount of threads and ThreadPool
        int transformationThreadAmount = 3;
        ExecutorService executor = Executors.newFixedThreadPool(transformationThreadAmount);

        // variables for Transformation threads
        float transformDegrees = 5f;   // degrees per transform

        // filling ThreadPool with Transformation threads
        for (int i = 0; i < transformationThreadAmount; i++) {
            executor.execute(new Transformation(queueToBeTransformed, listTransformed, pointAmount, transformDegrees));
        }

        // variables for creation of points
        int pointsCreated = 0;
        Random r = new Random();
        float xMax = 12f;
        float yMax = 24f;
        float zMax = 12f;

        // signal/track when the process actually starts
        System.out.println("Start creating points and transforming them ...\n");
        long startTime = System.currentTimeMillis();   // to calculate the time taken for the processes

        // create pointAmount random points and put them into queueToBeTransformed
        while (pointsCreated < pointAmount) {
            float x = r.nextFloat() * xMax;
            float y = r.nextFloat() * yMax;
            float z = r.nextFloat() * zMax;
            Point3D point3D = new Point3D(x, y, z);
            queueToBeTransformed.add(point3D);

            //System.out.println("Initial transform:");
            //System.out.printf("%f\n%f\n%f\n\n", x, y, z);

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
