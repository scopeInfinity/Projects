/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfindersolver;
import java.awt.*;
import java.applet.*;
import java.awt.image.PixelGrabber;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Gagan
 */

public class PathFinderSolver extends Applet {
    Image img;
    static int M,N,pixels[],xg=2,yg=2;
    static boolean done=false,a[][];
    static int getpoints=0;
    static coordinate s,e;
    LinkedList path;
    public void addPath(coordinate c)
    {
      path.add(c);
      
    }
    public boolean isBlack(int p)
    {
        int r,g,b;
                 r=0xff & (p>>16);
                
                b=0xff & (p>>8);
                g=0xff & p;
                return (r<100 && b<100 && g<100);
    }
    public void init()
    {
        System.out.println("Started");
        getFile();
        setImage();
        done=true;
        //repaint();
        a=new boolean[M/yg][N/xg];
        for(int i=yg/2;i<M;i+=yg)
            for(int j=xg/2;j<N;j+=xg)
                if(isBlack(pixels[i*N+j])) a[i/yg][j/xg]=true; else a[i/yg][j/xg]=false;
        
        getpoints=1;
        new takingPoints(this);
        path=new LinkedList();
        
    }
    public void setPoints(coordinate d)
    {coordinate c=new coordinate(d.getX()/xg, d.getY()/yg);
    System.out.println("POINT CLICKED :"+c);    
    if(getpoints==2)
        {
            e=c;
        generatePath gP=new generatePath();
                gP.init(a,s , e, (M)/yg, (N)/xg,this);
        System.out.println("Finding Path");
        gP.findpath();
        getpoints=3;;
        }
        else
        if(getpoints==1)
        {s=c;
            getpoints=2;
        }
    repaint();
    }
    public void getFile()
    {       Frame fr=new Frame("Select Image");
            FileDialog fd=new FileDialog(fr);
            fd.setVisible(true);
            String dir,fn;
            dir=fd.getDirectory();
            fn=fd.getFile();
            System.out.println(fn);

                   
            img=getImage(this.getDocumentBase(),fn);
            MediaTracker t=new MediaTracker(this);
            t.addImage(img, 0);
        try {
            t.waitForID(0);
        } catch (InterruptedException ex) {
            Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setImage()
    {
            N=img.getWidth(null);
            M=img.getHeight(null);
            pixels= new int[M*N];
            PixelGrabber pg=new PixelGrabber(img,0,0,N,M,pixels,0,N);
        try {
            pg.grabPixels();
        } catch (InterruptedException ex) {
            Logger.getLogger(PathFinderSolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         
    }
    public void paint(Graphics g)
    {
        if(done)
        {g.setColor(Color.black);
            String str;
            if(getpoints==1) str=new String("Click on start point");
            else if(getpoints==2) str=new String("Click on end point");
            else str=new String("Done! Created By G1K");
        g.drawString(str, 10, 10);
        g.drawImage(img, 0, 20,this);
        if(getpoints==3) {
        
            ListIterator i=path.listIterator();
            coordinate l=(coordinate) i.next();
            g.setColor(Color.RED);
            g.fillRect(l.getX()*xg, 20+l.getY()*yg, xg,yg);
        
            g.setColor(Color.BLUE);
            coordinate n;
            while(i.hasNext())
            {
                n=(coordinate) i.next();
                g.drawLine(l.getX()*xg+xg/2, 20+l.getY()*yg+yg/2, n.getX()*xg+xg/2,20+ n.getY()*yg+yg/2);
                
                l=n;
            }
            g.setColor(Color.RED);
            
        g.fillRect(l.getX()*xg, 20+l.getY()*yg, xg,yg);
        }
        }
    }
    
}
