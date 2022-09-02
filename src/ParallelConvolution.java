import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import static java.lang.System.out;

public class ParallelConvolution extends Thread{

    private int start;
    private int end;
    private BufferedImage input;
    private BufferedImage output;
    private ImageKernel kernel;
    private int width;
    private int height;
    private int edge;


// Constructing the thread with parts of the image from the start to the end value of input image.
// Assigning new value to the global variable output image, considering the kernel.

    public ParallelConvolution(int start, int end, BufferedImage input, BufferedImage output, ImageKernel kernel, int edge){
        this.start = start;
        this.end = end;
        this.input = input;
        this.output = output;
        this.kernel = kernel;
        this.width = input.getWidth();
        this.height = input.getHeight();
        this.edge = edge;
    }


// Block obliged for processing chunk that is assigned to the thread
// Pretty much straight forward algorithm, simular to the sequential algorithm. Takes pixel by pixel adding all the values of its neigbours and the central pixel,
// then assigning it up in the new value.
// Considering RGB value for every pixel.
// Only difference is that it takes only part of the input image. Other words thread process only chunk with additional few rows if needed, because of edge.

    @Override
    public void run() {
        out.println("[*] Processing the image: " + Thread.currentThread());
                for(int x = 0; x<width; x++)
                {
                    for(int y=start + edge; y<end - edge; y++)
                    {
                        float red=0f,green=0f,blue=0f;
                        for(int i = 0; i< kernel.getOrder(); i++)
                        {
                                for (int j = 0; j < kernel.getOrder(); j++) {

                                    int imageX = (x - kernel.getOrder() / 2 + i + width) % width;
                                    int imageY = (y - kernel.getOrder() / 2 + j + end) % end;

                                    int RGB = input.getRGB(imageX, imageY);
                                    int R = (RGB >> 16) & 0xff; // Red Value
                                    int G = (RGB >> 8) & 0xff;    // Green Value
                                    int B = (RGB) & 0xff;        // Blue Value

                                    red += (R * ImageKernel.getKernel()[i][j]);
                                    green += (G * ImageKernel.getKernel()[i][j]);
                                    blue += (B * ImageKernel.getKernel()[i][j]);

                                }
                        }
                        int outR, outG, outB;

                        outR = Math.min(Math.max((int)(red*kernel.getMult_factor()),0),255);
                        outG = Math.min(Math.max((int)(green*kernel.getMult_factor()),0),255);
                        outB = Math.min(Math.max((int)(blue* kernel.getMult_factor()),0),255);

                        output.setRGB(x,y,new Color(outR,outG,outB).getRGB());
                    }
                }
   }
}

