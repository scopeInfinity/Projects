/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbox2;

/**
 *
 * @author Gagan
 */
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class socketDataR implements Runnable{
    Thread t;
    int myPort;
    boolean working;
    chatBoxFrame cBF;
    DatagramSocket ds;
    byte buffer[];
    int buffer_size=1024;
    public socketDataR(int p,chatBoxFrame cbf,DatagramSocket dS) {
        myPort=p;
        cBF=cbf;
        ds=dS;
        t=new Thread(this);
        working=true;
        buffer=new byte[buffer_size];
        t.start();
    }

    @Override
    public void run() {
        DatagramPacket dp=new DatagramPacket(buffer,buffer_size);
    while(working)
    {
            try {
                ds.receive(dp);
            } catch (IOException ex) {
            }
            cBF.addText((new String(dp.getData(),0,dp.getLength())));
    }
    
    }
    
    
}
