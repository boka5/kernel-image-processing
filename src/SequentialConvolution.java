import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.System.out;

public class SequentialConvolution {

    private BufferedImage input;
    private BufferedImage output;
    private ImageKernel kernel;


    public SequentialConvolution(BufferedImage input, BufferedImage output, ImageKernel kernel){
        this.input = input;
        this.output = output;
        this.kernel = kernel;

        int width = input.getWidth();
        int height = input.getHeight();

// Pretty much straight forward algorithm, that pixel by pixel adding all the values of the neigbours and the central pixel and summing it up in the new value.
// Considering RGB value for every pixel.

        out.println("[*] Processing the image sequentialy...");
        for(int x=0;x<width;x++)
        {
            for(int y=0;y<height;y++)
            {
                float red=0f,green=0f,blue=0f;
                for(int i = 0; i< kernel.getOrder(); i++)
                {
                    for(int j=0;j<kernel.getOrder();j++)
                    {
                        int imageX = (x - kernel.getOrder() / 2 + i + width) % width;
                        int imageY = (y - kernel.getOrder() / 2 + j + height) % height;

                        int RGB = input.getRGB(imageX,imageY);
                        int R = (RGB >> 16) & 0xff; // Red Value
                        int G = (RGB >> 8) & 0xff;	// Green Value
                        int B = (RGB) & 0xff;		// Blue Value

                        red += (R*kernel.getKernel()[i][j]);
                        green += (G*kernel.getKernel()[i][j]);
                        blue += (B*kernel.getKernel()[i][j]);
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

