/**
 *
 * @author Gagan (scopeInfinity)
 */
import java.awt.*;
import java.awt.image.ImageProducer;
import java.applet.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.image.PixelGrabber;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Img2text  extends Applet {

    Image img;
    int MakeEmpty=-1;
    String dir,fn;
    String str="";
    int pixels[];
    int sx=1,sy=1,width,height,Mw,Mh;
    int cS=48,cE=122;public void getFile()
    {       Frame fr=new Frame("Select Image");
            FileDialog fd=new FileDialog(fr);
            fd.setVisible(true);
            dir=fd.getDirectory();
            fn=fd.getFile();
            System.out.println(fn);

                   
            img=getImage(getDocumentBase(),fn);
            MediaTracker t=new MediaTracker(this);
            t.addImage(img, 0);
        try {
            t.waitForID(0);
        } catch (InterruptedException ex) {
            Logger.getLogger(Img2text.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setImage()
    {
            width=img.getWidth(null);
            height=img.getHeight(null);
            pixels= new int[width*height];
            PixelGrabber pg=new PixelGrabber(img,0,0,width,height,pixels,0,width);
        try {
            pg.grabPixels();
        } catch (InterruptedException ex) {
            Logger.getLogger(Img2text.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    public void init()
    {
        Scanner input=new Scanner(System.in);
            getFile();
            setImage();
            Mh=20;Mw=70;
            try
            {
            System.out.println("Width="+width+" Height="+height+"\nEnter Invalid Input to set to default");
            System.out.print("Enter Number of rows(20):");
            Mh=input.nextInt();
            System.out.print("Enter Number of Columns(70):");
            Mw=input.nextInt();
            System.out.print("Enter Numbe to Make Space(-1):");
            MakeEmpty=input.nextInt();
            
            }catch(Exception e){;}
        try {
            makeText();
            //repaint();    
        } catch (Exception ex) {
            Logger.getLogger(Img2text.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    public void makeText() throws FileNotFoundException, IOException
    {
        
        int r,g,b,p;
        int cL=12;	//cE-cS+1;
        sy=height/Mh;
        sx=width/Mw;
        
	if(sy==0 || sx==0) return;
        for(int i=0;i<height;i+=sy)
        {
            for(int j=0;j<width;j+=sx)
            {
                p=pixels[i*width+j];
                
                r=0xff & (p>>16);
                r=2*r/256;
                b=0xff & (p>>8);
                b=2*b/256;
                g=0xff & p;
                g=g*2/256;
                if(MakeEmpty==g+b*2+r*4)
                    str+=' ';
                else
                str+=(char)('0'+g+b*2+r*4);
            }
            str+="\n";
            
        }
        System.out.println(str);
    }
    public void paint(Graphics g)
    {
        g.drawLine(10, 10, 20, 100);
        g.drawImage(img, 0, 0, this);
    }
}
