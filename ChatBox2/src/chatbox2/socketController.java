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

public class socketController {
    int myPort=1711,otherPort=1711;//Default Ports
    DatagramSocket ds;
    InetAddress IP;
    
    chatBoxFrame cBF;
    socketDataR sDR;
    socketController(chatBoxFrame cbf)
    {
        cBF=cbf;
        
    }
    boolean enableChat()
    {
        myPort=cBF.getMyPort();
        otherPort=cBF.getMyPort();
        try {
            ds=new DatagramSocket(myPort);
        } catch (SocketException ex) {
            cBF.setStatus("Error in creating Datagram Socket at PORT "+myPort);
            return false;
        }
        try {
            IP=InetAddress.getByName(cBF.getIP());
        } catch (UnknownHostException ex) {
        cBF.setStatus("Invalid IP :"+ex);
        return false;
        }
        sDR=new socketDataR(myPort,cBF,ds);
        return true;
    }
    void disableChat()
    {
        sDR.working=false;
        //ds.disconnect();
        ds.close();
    }
    void send(String str) throws IOException
    {
        otherPort=cBF.getOtherPort();
        try {
            IP=InetAddress.getByName(cBF.getIP());
        } catch (UnknownHostException ex) {
        cBF.setStatus("Invalid IP :"+ex);
        return;
        }
        str=cBF.getPersonName()+"> "+str;
        byte buf[]=str.getBytes();
        
        DatagramPacket dp=new DatagramPacket(buf,buf.length,IP,otherPort);
        //cBF.setStatus("Sending '"+str+"' at "+otherPort);
            
        try {
            ds.send(dp);
        } catch (IOException ex) {
        cBF.setStatus("Error in sending packet :"+ex);
        return;
        }
        cBF.addText(str);
    }
    
}
