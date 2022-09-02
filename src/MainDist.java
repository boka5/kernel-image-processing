// Distributed part is not finished... Does not work!

//import mpi.MPI;
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//public class MainDist {
//
//    // Pass the name of the picture that you want to process in "filename".
//    public static String filename = "1900.jpg";
//    public static ImageKernel imageKernel;
//    public static BufferedImage inputImage;
//    public static BufferedImage distributedOutputImage;
//    public static int[] imageArray;
//    public static int[] N;
//    public static int width;
//    public static int height;
//    public static int chunk;
//    public static int garbageChunk;
//
//
//    public static void main(String args[])  {
//
//        MPI.Init(args);
//        int rank = MPI.COMM_WORLD.Rank();
//        int size = MPI.COMM_WORLD.Size();
//
//        System.out.println(" I'm process " +rank + ", of size: " + size);
//
//        N = new int[1];
//        N[0] = height * width;
//        imageArray = new int[N[0]];
//        //showImage(inputImage,filename);
//        System.out.println(" ");
//        System.out.println("Image name: " + filename);
//        System.out.println("Image size: " + width + " x " + height );
//        space();
//
//        //preparation
//        if(rank == 0) {
//            imageKernel = new ImageKernel();
//            try {
//                inputImage = ImageIO.read(new File(filename));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            width = inputImage.getWidth();
//            height = inputImage.getHeight();
//            distributedOutputImage = new BufferedImage(width, height, inputImage.getType());
//
//            System.out.println("width: " + width + " height: " + height);
//
//            System.out.println("Chunk size is " + (N[0] / size) + " pixels.");
//            System.out.println("Number of pixels is " + N[0]);
//            System.out.println("Garbage chunk size is " + N[0] % size);
//
//            imageArray = inputImage.getRGB(0, 0, inputImage.getWidth(), inputImage.getHeight(), null, 0, inputImage.getWidth());
//
//        }
//
//        MPI.COMM_WORLD.Bcast(N[0], 0, 1, MPI.INT, 0);
//
//
//        //distruted calcualtion
//        MPI.COMM_WORLD.Bcast(imageArray, 0, inputImage.getHeight(), MPI.INT, 0);
//
//        for(int x = 0; x<width; x++)
//        {
//            for(int y=0;y<height;y++)
//            {
//                float red=0f,green=0f,blue=0f;
//                for(int i = 0; i< imageKernel.getOrder(); i++)
//                {
//                    for (int j = 0; j < imageKernel.getOrder(); j++) {
//
//                        int imageX = (x - imageKernel.getOrder() / 2 + i + width) % width;
//                        int imageY = (y - imageKernel.getOrder() / 2 + j + height) % height;
//
//                        int RGB = inputImage.getRGB(imageX, imageY);
//                        int R = (RGB >> 16) & 0xff; // Red Value
//                        int G = (RGB >> 8) & 0xff;    // Green Value
//                        int B = (RGB) & 0xff;        // Blue Value
//
//                        red += (R * ImageKernel.getKernel()[i][j]);
//                        green += (G * ImageKernel.getKernel()[i][j]);
//                        blue += (B * ImageKernel.getKernel()[i][j]);
//
//                    }
//                }
//                int outR, outG, outB;
//
//                outR = Math.min(Math.max((int)(red*imageKernel.getMult_factor()),0),255);
//                outG = Math.min(Math.max((int)(green*imageKernel.getMult_factor()),0),255);
//                outB = Math.min(Math.max((int)(blue* imageKernel.getMult_factor()),0),255);
//
//                distributedOutputImage.setRGB(x,y,new Color(outR,outG,outB).getRGB());
//            }
//        }
//        MPI.COMM_WORLD.Gather(imageArray, 0, N[0], MPI.INT,
//                imageArray, 0, N[0], MPI.INT, 0);
//        showImage(distributedOutputImage, "-Distributed processed image");
//        MPI.Finalize();
//    }
//
//    public static void space(){
//        System.out.println(" ");
//        System.out.println("  ");
//    }
//
//    public static void showImage(BufferedImage image, String name){
//
//        try {
//            ImageIO.write(image,"JPG", new File(name+".jpg"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ShowImage showImage = new ShowImage();
//        showImage.setPath(name);
//        showImage.display();
//    }
//}
//
