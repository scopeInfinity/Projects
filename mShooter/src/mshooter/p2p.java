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
import java.io.IOException;
import java.util.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
class p2p {
  static int port=1711,portC=1712,portT,portR;
    static int buffer_size=1024;
    static byte buffer[]=new byte[buffer_size];
    static DatagramSocket ds;
    static InetAddress other;
    MShooter MS;
    
    
    public static void send(String str) throws Exception
    {if(!MShooter.socketGame) return;
      //  System.out.println("\nTrying to send : "+str);
      //  System.out.println("Other : "+other+" portT : "+portT+" portR"+portR);
        for(int i=0;i<str.length();i++)
                buffer[i]=(byte) str.charAt(i);
            ds.send(new DatagramPacket(buffer,str.length(),other, portT));
        //System.out.println(str+"     send at port:"+portOther);
    }
    public static void sendBytes(byte[] x) throws Exception
    {if(!MShooter.socketGame) return;
      //  System.out.println("\nTrying to send : "+str);
      //  System.out.println("Other : "+other+" portT : "+portT+" portR"+portR);
            ds.send(new DatagramPacket(x,x.length,other, portT));
        //System.out.println(str+"     send at port:"+portOther);
    }
    
    
    p2p(MShooter ms) throws Exception
    {
        MS=ms;
   
     Scanner input=new Scanner(System.in);
    System.out.println("My Ip\t:"+InetAddress.getLocalHost());
    System.out.print("Other IP Address (leave blank for 'localhost') : " );
    String otherip=input.nextLine();
    otherip=otherip.trim();
    if(otherip.length()==0) otherip=new String("localhost");
    /*
    System.out.print("Enter Transmitting Port Number(leave blank for 'default') : " );
    String portStr=input.nextLine();
    portStr=portStr.trim();
    if(!portStr.equals(""))
    port=Integer.parseInt(portStr);
    */
        
   
    boolean host;
    System.out.print("Want to Take Left Board(y\\n) : " );
    int ch=System.in.read();
    host=ch=='y'||ch=='Y';
    
    if(host)
        {
            portR=port;
            portT=portC;
            MS.meP1=true;
        }
    else 
        {
            portR=portC;
            portT=port;
            MS.meP1=false;
        }
    
    
    ds=new DatagramSocket(portR);
    
    other=InetAddress.getByName(otherip);
    
    p2pR rec=new p2pR(ds, buffer_size,MS);
    System.out.println("Other\t:"+other);
    
    
    }
    
}

class p2pR implements Runnable
{   Thread t;
    static byte buffer[];
    static DatagramSocket ds;
    static boolean working=true;
    String str;
    MShooter MS;
    p2pR(DatagramSocket d,int buffer_size,MShooter ms)
    {       MS=ms;
        ds=d;
        buffer=new byte[buffer_size];
        t=new Thread(this);
        t.start();
        
    }
    public void run()
    {      
        while(working)
        {
            try{
            DatagramPacket p=new DatagramPacket(buffer, buffer.length);
            ds.receive(p);
            if(working)
            {
            //str=new String(p.getData(),0,p.getLength());
            MS.msgReceiveBytes(p.getData(),p.getLength());
            
            }
            if(!MS.isRunning) {    p2p.sendBytes((new byte[1]));
         //System.out.println("STARTED");
            MS.isRunning=true;}
            //MS.msgReceive(str);
            
            }catch(Exception e){;}
        }
    }
    
    
}

