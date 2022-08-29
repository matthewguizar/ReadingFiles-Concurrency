import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


public class Main {
 
    static String[] files = new String[] { "data/sales1.csv", "data/sales2.csv", "data/sales3.csv"};
   
    // static int sampleSize = 0;
    static AtomicInteger sampleSize = new AtomicInteger(0);
    // static int quantitySold = 0;
    static AtomicInteger quantitySold = new AtomicInteger(0);


    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(3);
        // ReentrantLock lock = new ReentrantLock();
        ExecutorService executor = Executors.newFixedThreadPool(3);
       for (String string : files) {
            executor.submit(() -> increment(string, latch));
       }
     
        Scanner scan = new Scanner(System.in);
        System.out.print("Please enter your name to access the Global Superstore data: ");
        String name = scan.nextLine();
        System.out.println("\nThank you " + name + ".\n");
        scan.close();
        latch.await();
        executor.shutdown();

        System.out.println("Sample size: " + sampleSize);
        System.out.println("Quantity sold: " + quantitySold);
            
     
    }

    public static void increment(String file, CountDownLatch latch){
        try {
            Path path = Paths.get(Thread.currentThread().getContextClassLoader().getResource(file).toURI());
            Files.lines(path)
                .skip(1)
                .mapToInt((line) ->Integer.parseInt(line.split(",")[2]))
                .forEach((quantity) -> {
                    // lock.lock();
                    sampleSize.addAndGet(1);
                    quantitySold.addAndGet(quantity);
                    // lock.unlock();
                });
        } catch (URISyntaxException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        latch.countDown();
    }


}
