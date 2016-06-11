/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mshooter;

/**
 *
 * @author Gagan
 */

import java.net.*;
import java.util.*;
//import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.swing.*;
import java.math.*;

public class MShooter extends JFrame{
    final int ystart=25;
    final int width=640,height=480;
    final int status_height=30;
    final int blockHeight=100,blockWidth=10;
    int p1Points,p2Points;
    int p1Y,p2Y;
    final int bat1X=20,bat2X=width-bat1X;
    int speed,direction;
    double ball_speeD;
    int ballX,ballY;
    final int ballRadius=5;
    int ball_xspeed,ball_yspeed,ball_speed;
    int ball_startTimer;
    boolean meP1;
    static boolean socketGame;
    p2p trasmit;
    boolean isRunning=false;
    controller ControlKeys;
    public static void main(String args[]) throws IOException
    {Scanner input=new Scanner(System.in);
        
        System.out.println("mShooter\n-----------------------\n(Created By : Gagan Kumar(gagan1kumar)\n");
        System.out.println("Info\t:Socket Mode> Play Multiplayer Game On A Network");
        System.out.println("Controls\nSocket Mode : off\n\tW and S key\t- Up and Down (left board)\n\tArrow Key\t- Up and Down (right board)");
        System.out.println("Socket Mode : on\n\t(W and S), Arrow Key and Mouse\t- Control Motion of your board\n");
        
        System.out.print("Do you want a socket game(Y\\N) :");
        String str=input.nextLine();
        str=str.trim();
        
        if(str.length()>=1 && (str.charAt(0)=='Y' || str.charAt(0)=='y'))
        socketGame=true;
        else
            socketGame=false;
        MShooter game=new MShooter();
        game.run();
        System.exit(0);
    }
    
    public void init()
    {
        meP1=true;
        setTitle("MShooter");
        setSize(width, height+ystart+status_height);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.GREEN);
        ControlKeys=new controller(this);
        p1Points=0;p2Points=0;
        p1Y=height/2;
        p2Y=height/2;
        speed=10;
        ball_speed=10;
        ball_speeD=10.0;
        isRunning=true;
        
        ballReset(true);
        
        if(socketGame)
        {
        try{
        trasmit=new p2p(this);
        isRunning=false;
        }catch(Exception e){;}
        }
               try {
            //p2p.send("A");
                    p2p.sendBytes(msgSendBytes((byte)100));
               //p2p.sendBytes(msgSendBytes((byte)3));
              
        } catch (Exception ex) {
        System.out.println(ex);
        }
    
        setVisible(true);
        
    }
    
    
    public void run()
    {
        init();
        updateFrame=0;
        boolean isStop=false;
        while(!isStop)
        {
            if(isRunning) update();
            draw();
            if(ball_startTimer>0) ball_startTimer--;
            
            try {
                Thread.sleep(30);
            } catch (InterruptedException ex) {
                
            }
        }
        setVisible(false);
    }
    public int  updateFrame;
    public void update()
    {
        ballWork();
        updateFrame++;
        if(updateFrame>=5)
        {
        try {
           // p2p.send("B "+ballX+" "+ballY+" "+direction+" "+ball_startTimer);
           //if(meP1)
               p2p.sendBytes(msgSendBytes((byte)3));
               
        } catch (Exception ex) {;}
        updateFrame=0;
        }
        
        }

    public void draw()
    {
        Graphics g=getGraphics();
        g.clearRect(0, ystart, width, height);//Clear History
        g.setColor(Color.BLACK);
        g.fillRect(bat1X-blockWidth, ystart+p1Y-blockHeight/2,blockWidth , blockHeight);
        g.fillRect(bat2X,ystart+ p2Y-blockHeight/2,blockWidth , blockHeight);
        g.setColor(Color.RED);
        g.fillOval(ballX-ballRadius, ballY+ystart-ballRadius, ballRadius*2, ballRadius*2);
        
        g.setColor(Color.WHITE);
        g.fillRect(0, height+ystart, width, status_height);
        g.setColor(Color.BLACK);
        g.drawString("Points :"+p1Points, 10, height+ystart+status_height/2);
        g.drawString("Points :"+p2Points, width-10-60, height+ystart+status_height/2);
             if(!isRunning)g.drawString("WAITING FOR OTHER PLAYER", 100, 100);
   
    }

    public void moveKUp()
    {
           try {
           // p2p.send("U "+ballX+" "+ballY+" "+direction+" "+ball_startTimer);
                p2p.sendBytes(msgSendBytes((byte)1));
           //    p2p.sendBytes(msgSendBytes((byte)3));
                          
//p2p.send("B "+ballX+" "+ballY+" "+direction);
        } catch (Exception ex) {
        //    Logger.getLogger(MShooter.class.getName()).log(Level.SEVERE, null, ex);
        }
           moveUp(false);
      
    }
    public void moveKDown()
    {
           try {
            //p2p.send("D "+ballX+" "+ballY+" "+direction+" "+ball_startTimer);
               p2p.sendBytes(msgSendBytes((byte)2));
         //      p2p.sendBytes(msgSendBytes((byte)3));
               
        } catch (Exception ex) {
         //   Logger.getLogger(MShooter.class.getName()).log(Level.SEVERE, null, ex);
        }
           moveDown(false);
      
    }
    public void moveUp(boolean opp)
    {//false FOR ME ......... true //For other
     
        if(meP1^opp)
        {
         p1Y-=speed;
        if(p1Y-blockHeight/2<0) p1Y=blockHeight/2;
        }
        else
            {
         p2Y-=speed;
        if(p2Y<blockHeight/2) p2Y=blockHeight/2;
        
        }
    }
    public void moveDown(boolean opp)
    {
        if(meP1^opp)
        {   
            p1Y+=speed;
            if(p1Y>height-blockHeight/2)  p1Y=height-blockHeight/2;
        
        }
        else
        {
            p2Y+=speed;
            if(p2Y>height-blockHeight/2)  p2Y=height-blockHeight/2;
        
        }
    }
    public void ballReset(boolean f)
    {
        ball_speed=10;
        ball_speeD=10.0;
        ballX=width/2;
        ballY=height/2;
        
        
        if(f)
            direction=30;
        else
            direction=150;
        
        ball_xspeed=(int) ((int) ball_speed*Math.cos(direction*Math.PI/180));
        ball_yspeed=(int) (- (int) ball_speed*Math.sin(direction*Math.PI/180));
        
        ball_startTimer=30;
    }
    public int makeDirection360(int x)
    {
        x=x%360;
        if(x<0) x+=360;
        return x;
    }
    public void collisionBall()
    {int yd;
        //*Collisions with bat**//
        if(ballY<=p2Y+blockHeight/2 && ballY>=p2Y-blockHeight/2)
        if(ballX>bat2X && ballX-ball_xspeed<=bat2X)
        {
            ballX=2*bat2X-ballX;
            //ball_xspeed=-ball_xspeed;
            yd=-ballY+p2Y;
            direction=makeDirection360( 180-yd);//direction=180-(int) (180/Math.PI*Math.atan2(yd, blockHeight));
            if(direction<180-45) direction=180-45;
            if(direction>180+45) direction=180+45;
            ball_speeD+=0.2;
        }
        
        if(ballY<=p1Y+blockHeight/2 && ballY>=p1Y-blockHeight/2)
        if(ballX<bat1X && ballX-ball_xspeed>=bat1X)
        {
            ballX=2*bat1X-ballX;
            //ball_xspeed=-ball_xspeed;
             yd=ballY-p1Y;
           // direction= 180-direction;//(int) (180/Math.PI*Math.atan2(yd, blockHeight));
             direction=makeDirection360( 180+yd);
             if(direction<180 && direction>45) direction=45;
            if(direction>180 && direction<360-45) direction=360-45;
            ball_speeD+=0.2;
        }
        
        //*Collisions with wall**//
        if(ballX-ballRadius<=0){ p2Points+=10;ballReset(false);}
        if(ballX+ballRadius>=width) {p1Points+=10;ballReset(true);}
        
        if(ballY-ballRadius<0) 
        {
            ballY=2*ballRadius-ballY;
            direction=-direction;
            //ball_yspeed=-ball_yspeed;
        }
        if(ballY+ballRadius>height) 
        {
            ballY=2*height-ballY-2*ballRadius;
            direction=-direction;
            //ball_yspeed=-ball_yspeed;
        }
        ball_speed=(int)ball_speeD;
    }
    public void ballWork()
    {
        ball_xspeed=(int) ((int) ball_speed*Math.cos(direction*Math.PI/180));
        ball_yspeed=(int) (- (int) ball_speed*Math.sin(direction*Math.PI/180));
        if(ball_startTimer==0)
        {
        ballX+=ball_xspeed;
        ballY+=ball_yspeed;
        }
        collisionBall();//With Walls and bat
        
    }
    public void msgReceive(String str)
    {
        if(str.length()<=0) return;
        try
        {
            if(str.length()>=1)
            {
                int ch=str.charAt(0);
                if(ch=='U') moveUp(true);
                if(ch=='D') moveDown(true);
                if(str.length()>1)
                {
                String s[]=str.split(" ");
                ballX=Integer.parseInt(s[1]);//1  *2
                ballY=Integer.parseInt(s[2]);//1   *2
                direction=Integer.parseInt(s[3]);//1 *2
                ball_startTimer=Integer.parseInt(s[4]);//1
                
     //           System.out.println("REC : "+str+"   "+ballX+" "+ballY+" "+direction);
                  
                }
            }
        }catch(Exception e){;}
    }
    
    
    public void msgReceiveBytes(byte[] databuff,int l)
    {
        if(l<=0) return;
        try
        {
            if(l==1)
            {
                if(databuff[0]==1) moveUp(true);
                else if(databuff[0]==2) moveDown(true);
                 
            }
            else
            if(databuff[0]==4)
            {
                int y=(((int)databuff[1])<<7)|databuff[2];
                if(meP1)
                    p2Y=y;
                else p1Y=y;
                // System.out.println("Y rec" +y);
            }
            else
            {
                
                
                ballX=(((int)databuff[1])<<7)|databuff[2];
                ballY=(((int)databuff[3])<<7)|databuff[4];
                direction=(((int)databuff[5])<<7)|databuff[6];
                
                
                ball_startTimer=databuff[7];
        //        System.out.println("ALL DATASET REC="+ballX);
                
            }
        }catch(Exception e){;}
    }
    
    public byte[] msgSendBytes(byte b)
    {
        if(b==1 || b==2 ||b==100)
        {
        byte databuff[]=new byte[1];
        databuff[0]=b;
        return databuff;
        }
        else if(b==4)
        {
        byte databuff[]=new byte[3];
            int y;
            if(meP1)
                y=p1Y;
            else
                y=p2Y;
            databuff[0]=4;
            databuff[1]|=((y>>7)&0x7f);
       databuff[2]=(byte) ((byte)y&0x7f);
       return databuff;
        }
            
     byte databuff[]=new byte[8];
     
       databuff[0]=3;//1  2 for U n D
       databuff[1]=0;
       {
       int x=ballX;
       databuff[1]|=(x>>7)&0x7f;
       databuff[2]=(byte) ((byte)x&0x7f);
       }
       {
       int x=ballY;
       databuff[3]|=((x>>7)&0x7f);
       databuff[4]=(byte) ((byte)x&0x7f);
       }
       {
       int x=direction;
       x=x%360;
       if(x<0) x+=360;
       databuff[5]|=((x>>7)&0x7f);
       databuff[6]=(byte) ((byte)x&0x7f);
       }
       databuff[7]=(byte) ball_startTimer;
      //         System.out.println("ALL DATASET SENT ="+ballX);
                
     return databuff;
       
    }
    
    public void mouseMoved(int y)
    {y-=ystart;
        if(!socketGame) return;
        if(y<blockHeight/2) y=blockHeight/2;
        if(y>height-blockHeight/2) y=height-blockHeight/2;
        if(meP1)
        {
         p1Y=y;
        }
        else
        {
         p2Y=y;
        
        }
        try {
            p2p.sendBytes(msgSendBytes((byte)4));
        } catch (Exception ex) {
        System.out.println("ERROR sending mouse y");
        }
        
    }
    
}
