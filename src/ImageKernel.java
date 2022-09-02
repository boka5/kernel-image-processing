import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import static java.lang.System.out;

// This class is in charge of crating a kernel for image processing.
// There are few default proposals but also the option to create your own.

public class ImageKernel {
    private static  float[][] kernel;
    private static float mult_factor = 1.0f;
    private static float bias = 0f;
    private static int order = 0;
    int q=1;
    private int inputOrder=0;

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public ImageKernel()  {
        try {
            question();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public float[][] question() throws IOException {
        out.println("In order to start image processing, choose kernel: ");
        out.println("Options: " );
        out.println("* Edge detection,               press '1'.");
        out.println("* Blur box,                     press '2'.");
        out.println("* Blur box with size of 5,      press '5'.");
        out.println("* Create your own,              press 'other number'.");
        out.print("Enter your choise: \n");
        q = Integer.parseInt(reader.readLine());
        if(q==1){
            defaultKernel();
           return kernel;
        }
        else if(q==2){
            blurKernel();
            return kernel;
        }
        else if(q==5){
            blurKernelx5();
            return kernel;
        }
        else{
            coenvolutionKernel();
            return kernel;
        }
    }

    public void displayKernel(){
        out.println("The Kernel Matrix is:");
        for(int i=0; i < order; i++)
        {
            for(int j=0; j < order; j++)
            {   if(kernel[i][j]<0)
                out.print("\t"+ kernel[i][j]);
                else
                out.print("\t "+ kernel[i][j]);

            }
            out.println();
        }
    }

    public void defaultKernel() {
        float[][] kernel = {
                {-1,-1,-1},
                {-1, 8,-1},
                {-1,-1,-1}};
        setOrder(3);
        this.mult_factor=1.0f;
        this.bias = 0f;
        this.kernel = kernel;
        displayKernel();
    }

    public void blurKernel() {
        float[][] kernel = {
                {1,1,1},
                {1,1,1},
                {1,1,1}};
        setOrder(3);
        this.mult_factor=0.333333333333333333333333f;
        this.bias = 0f;
        this.kernel = kernel;
        displayKernel();
    }

    public void blurKernelx5() {
                float[][] kernel = {
                {1,1,1,1,1},
                {1,1,1,1,1},
                {1,1,1,1,1},
                {1,1,1,1,1},
                {1,1,1,1,1}};
        setOrder(5);
        this.mult_factor=0.333333333333333333333333f;
        this.bias = 0f;
        this.kernel = kernel;
        displayKernel();
    }


    public void coenvolutionKernel() throws IOException {
        enterOrder();
        setOrder(inputOrder);
        this. kernel = new float[getOrder()][getOrder()];
        // Getting the Kernel Matrix as input from the user
        for(int i=0; i < getOrder(); i++)
            for(int j=0; j <getOrder(); j++)
            {
                out.print(i+","+j+":");
               this.kernel[i][j] = Float.parseFloat(reader.readLine());
            }
        displayKernel();

        out.print("\nMultiplication Factor: ");
        float inputMult_factor=0;
        inputMult_factor = Float.parseFloat(reader.readLine());
        setMult_factor(inputMult_factor);

        out.print("Bias: ");
        float inputBias = 0;
        bias = Float.parseFloat(reader.readLine());
        setBias(inputBias);
    }

    public int enterOrder(){
        out.println("Enter the order of Kernel matrix:");
        try {
            inputOrder = parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputOrder;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public float getMult_factor() {
        return mult_factor;
    }

    public void setMult_factor(float mult_factor) {
        this.mult_factor = mult_factor;
    }

    public void setBias(float bias) {
        this.bias = bias;
    }

    public static float[][] getKernel() {
        return kernel;
    }


}