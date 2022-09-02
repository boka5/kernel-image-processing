import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//This class help us displaying image.

public class ShowImage extends Component {

    private static String path;
    private String name;
    private BufferedImage img;

    public ShowImage() {
        try {
            this.img = ImageIO.read(new File(getPath()));
        } catch (IOException e) {
        }
    }

    public static String getPath() { return path+".jpg"; }          // adding ".jbg" because we use this as a path of input image

    public void setPath(String path) {
        this.path = path;
    }



    public void display(){
            JFrame f = new JFrame(name);
            f.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            f.add(new ShowImage());
            f.pack();
            f.setResizable(true);
            f.setVisible(true);
        }

    public void paint(Graphics g) {
        if(img.getWidth()<1920 || img.getHeight()<1080){
            g.drawImage(img, 0, 0, img.getWidth(),img.getHeight(), this);
        }else
            g.drawImage(img, 0, 0,1920,1010,  this);            // Fixed frame size because otherwise we can not see whole image
    }

    public Dimension getPreferredSize() {
        if (img == null) {
            return new Dimension(200,200);
        } else {
            return new Dimension(img.getWidth(null), img.getHeight(null));
        }
    }
    }


