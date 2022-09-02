import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

// Main class contains both sequential and parallel implementation.
// In our project we have several different images, ordered by size.
// By processing them we can compare processing time of various approaches, and chack if parallel implementation is really faster.

public class Main {

    // Pass the name of the image that you want to process in "filename".
    public static String filename = "picture8500.jpg";
    public static ImageKernel imageKernel;
    public static BufferedImage inputImage;
    public static BufferedImage seqentionalOutputImage;
    public static BufferedImage parallelOutputImage;
    public static BufferedImage distributedOutputImage;
    public static int N;


    public static void main(String args[])  {
        space();

        //preparation
        prepare();
        showImage(inputImage,"-Input image");
        System.out.println(" ");
        System.out.println("Image name: " + filename);
        System.out.println("Image size: " + inputImage.getWidth() + " x " + inputImage.getHeight() );
        space();

        //seqential calculation together with time consumption
        System.out.println("    ***Sequential calculation***");
        long t0 = System.currentTimeMillis();
        seqentionalProcessing();
        long sequenialTime = System.currentTimeMillis() - t0;
        System.out.println("Sequential calculating time is: " +sequenialTime);
        space();


        //parallel calculation together with time consumption
        System.out.println("    ***Parallel calculation***");
        long t1 = System.currentTimeMillis();
        try {
            parallelProcessing();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long parallelTime = System.currentTimeMillis() - t1;
        System.out.println("Parallel calculating time is: " +parallelTime);
        space();

        double difference = ((double) sequenialTime / parallelTime);
        if (difference >  1) {
            System.out.println("Parallel computation is " + String.format("%.2f",difference) + " times faster.");

        }
    }

    // preparation block, loading and crating objects/images
    public static void prepare(){
        try {
            inputImage = ImageIO.read(new FileInputStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        seqentionalOutputImage = new BufferedImage(width, height, inputImage.getType());
        parallelOutputImage = new BufferedImage(width, height, inputImage.getType());
        distributedOutputImage = new BufferedImage(width, height, inputImage.getType());
        imageKernel = new ImageKernel();
        N = inputImage.getHeight();
    }

    public static void space(){
        System.out.println(" ");
        System.out.println("  ");
    }


    //saving and displaying output image
    public static void showImage(BufferedImage image, String name){
        try {
            ImageIO.write(image,"JPG", new File(name + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ShowImage showImage = new ShowImage();
        showImage.setPath(name);
        showImage.display();
    }

    public static void seqentionalProcessing() {
        SequentialConvolution seqentionalTransformation = new SequentialConvolution(inputImage,seqentionalOutputImage,imageKernel);
//I comment this out because if I display the image I may run out of space even faster
     showImage(seqentionalOutputImage,"-Sequential processed image");
    }

    public static void parallelProcessing() throws InterruptedException {
        int start;
        int end;
        int edge = imageKernel.getOrder() / 2;       // used for detecting the size of edge
        int numCoresAvalable = Runtime.getRuntime().availableProcessors();
        int chunk = N / numCoresAvalable;
        int garbageChunk = N % numCoresAvalable;


        ParallelConvolution[] coenvolutionParallels = new ParallelConvolution[numCoresAvalable];
        for (int i = 0; i < numCoresAvalable; i++) {
                start = i * chunk - edge;           // if start is not 0, then it has to include few row/rows of previous chunk depending on the size of kernel

            if (i == numCoresAvalable - 1)
                end = i * chunk + chunk;
            else
                end = i * chunk + chunk + edge;     //same goes for the last row in the end, it has to include next row from the starting chunk depending on the size of kernel

            if (end + garbageChunk == N)            //if n%numCoresAvalable != 0, last chunk will take the rest (garbageChunk)
                end = N ;
           coenvolutionParallels[i] = new ParallelConvolution(start, end, inputImage, parallelOutputImage, imageKernel, edge);
        }
        for (int i = 0; i < numCoresAvalable; i++) {
            coenvolutionParallels[i].start();
        //    Thread.sleep(500);
        }
        for (int i = 0; i < numCoresAvalable; i++) {
            coenvolutionParallels[i].join();        // barrier implementation, to make sure every thread will finish the job before displaying image
        }
        System.out.println("Number of cores: " + numCoresAvalable + ", Chunk size: " + chunk + ", Garbage Chunk size: " + garbageChunk);

//I comment this out because if I display the image I may run out of space  even faster
        showImage(parallelOutputImage,"-Parallel processed image");
    }


}

