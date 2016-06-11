//package snake;

/**
 * Name     :   Snake
 * @author  :   Gagan Kumar(gagan1kumar)
 */
import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;
import java.util.*;
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
public class Snake extends Applet implements KeyListener,Runnable{//
    
    Thread t;
    Random r;
    int score;
    boolean DEAD=false,started=false;
    int x[],y[];
    final int xsize=20,ysize=20,xblock=10,yblock=10,totalL=xsize*ysize;
    int top,dir,lKey,s;// 0-right 1-top 2-left 3-down
    int tX,tY;
    public void setNewT()
    {
        do
        {
            tX=r.nextInt(xsize);
            tY=r.nextInt(ysize);
            System.out.println("Treasure :"+tX+","+tY);
        }while(checkSnake(tX, tY));
    }
    public void add(int a,int b)
    {   if(s==-1) s=0;
        top++;
        if(top==totalL) top=0;
        x[top]=a;
        y[top]=b;
        System.out.println("Start :"+s+" Top :"+top);
    }
    public int xTop()
    {
        return x[top];
    }
    public int yTop()
    {
        return y[top];
    }
    public void delete()
    {   if(s==top) 
        {
            s=top=-1;
            return;
        }
        s++;
        if(s==totalL) s=0;
    }
    public void init()
    {   t=new Thread(this);
        r=new Random();
        x=new int[xsize*ysize];
        y=new int[xsize*ysize];
        top=-1;
        s=-1;
        add(5,5);
        add(6,5);
        
        score=0;
        dir=0;
        lKey=0;
        
        System.out.println("Program Started");
        for(int i=top;i>=s;i--)
            System.out.println(x[i]+","+y[i]);
    
    //    new Controller(this);
        t.start();
        addKeyListener(this);
        requestFocus();
        System.out.println("Controller Started");
        setNewT();
    }
   public void drawBlock(Graphics g,int a,int b)
   {
       g.fillRect(a*xblock+xblock/5,b*yblock+yblock/5,3*xblock/5,3*yblock/5);
       System.out.print("  "+a+","+b);
   }
   
   public void drawBlockB(Graphics g,int a,int b)
   {
       g.fillRect(a*xblock,b*yblock,xblock,yblock);
       System.out.print(" >"+a+","+b);
   }
   public void drawline(Graphics g,int a,int b,int c,int d)
   {
    g.drawLine(a*xblock+xblock/2, b*yblock+yblock/2, c*xblock+xblock/2, d*yblock+yblock/2);
   }
        
     public void paint(Graphics g)
    {
        if(!started)
        {
            g.drawString("Simple Snake Game", 40, 40);
            g.drawString("Creator", 10, 80);
            g.drawString("> Gagan Kumar", 10, 100);
            g.drawString("  (gagan1kumar)", 10, 120);
            g.drawString("Use Arrow Keys to Control", 10, 160);
            g.drawString("Press Any Key to continue...", 10, 180);
            return;
            
        }
        g.drawRect(0, 0, xsize*xblock-1, ysize*yblock-1);
        System.out.print("Blocks :");
    drawBlockB(g,xTop(),yTop());
    if(s<=top)
        for(int i=s;i<top;i++)
        {
            drawBlock(g,x[i],y[i]);
            drawline(g,x[i],y[i],x[i+1],y[i+1]);
        }
    else
    {
        for(int i=s;i<totalL;i++)
        {
            drawBlock(g,x[i],y[i]);
             drawline(g,x[i],y[i],x[i+1],y[i+1]);
        }
        for(int i=0;i<top;i++)
        {
            drawBlock(g,x[i],y[i]);
             drawline(g,x[i],y[i],x[i+1],y[i+1]);
        }
        
    }
        g.drawString("O", tX*xblock, (tY+1)*yblock);
    }
     public boolean checkSnake(int a,int b)
     {
     boolean f=false;
     if(s<=top)
     {   for(int i=s;i<=top;i++)
            if(x[i]==a && y[i]==b) {f=true;break;}
     
     }
    else
    {
        for(int i=s;i<totalL;i++)
            if(x[i]==a && y[i]==b) {f=true;break;}
        for(int i=0;i<=top;i++)
            if(x[i]==a && y[i]==b) {f=true;break;}
        
    }
     return f;
     }
    public void makeDead()
    {
        DEAD=true;
        showStatus("Dead, Score :"+score);
                
    }
    public boolean checkDead(int a,int b)
    {
        if(a>=xsize || b>=ysize || a<0 || b<0) {makeDead();return true;}
        if(checkSnake(a, b)) {makeDead();return true;}
        return false;
        
    
    }
    public void move()
    {
        int a=xTop(),b=yTop();
        if(dir==0 || dir==2){ if(lKey==1 || lKey==3) dir=lKey;}
        else if(dir==1 || dir==3) if(lKey==2 || lKey==0) dir=lKey;
        
        switch(dir)
        {
            case 0: a=xTop()+1;
                    b=yTop();
                break;
            case 1: a=xTop();
                    b=yTop()-1;
                break;
            case 2: a=xTop()-1;
                    b=yTop();
                break;
            case 3: a=xTop();
                    b=yTop()+1;
                break;
                
        }
        if(checkDead(a,b)){return;}
       
        add(a,b);
        if(xTop()==tX && yTop()==tY)
            {score+=10;
            setNewT();
            showStatus("Score:"+score);
            }
        
        else
            delete();
    }
    public void run()
    {
        while(!started)
        {
           try{ t.sleep(100);}catch(Exception e){;}
        }
        while(true)
        {
            System.out.println("draw");
        
            try
            {
                if(!DEAD) move();
                repaint();
                t.sleep(500);
            }catch(Exception e){showStatus("Error : "+e);}
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    System.out.print("Controller : ");
    started=true;
    int k=e.getKeyCode();
    switch(k)
    {
        case KeyEvent.VK_UP : lKey=1;
        System.out.println("UP");
            break;
        case KeyEvent.VK_LEFT : lKey=2;
        System.out.println("LEFT");
            break;
        case KeyEvent.VK_DOWN : lKey=3;
        System.out.println("DOWN");
            break;
        case KeyEvent.VK_RIGHT : lKey=0;
        System.out.println("RIGHT");
            break;
            
    }
    }

    public void keyReleased(KeyEvent e) {
    }

    
    
}