/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbox2;

import java.io.IOException;

/**
 *
 * @author Gagan
 */
public class ChatBox2 {

    /**
     * @param args the command line arguments
     */
    static chatBoxFrame cBF;
    static socketController sC;
    static boolean working;
    public static void main(String[] args) {
        // TODO code application logic here
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                cBF= new chatBoxFrame();
                cBF.setVisible(true);
                sC=new socketController(cBF);
                working=false;
                
            }
        });
    }
    public static boolean enableChat()
    {
        if(sC.enableChat())
            working=true;
        else working=false;
     if(working)  cBF.setStatus("Status : enabled");
        return working;
    }
    public static void disableChat()
    {
        sC.disableChat();
        working=false;
        cBF.setStatus("Status : idle");
    }
    public static void send(String str) throws IOException
    {
        if(str.length()>0)
        {
            sC.send(str);
        }
    }
  /*  public static void changeMyPort(String str)
    {
        try
        {
            sC.changeMyPort(Integer.parseInt(str));
        }catch(Exception e){cBF.setStatus("Invalid Receiving Port :"+e);}
    }
     public static void changeOtherPort(String str)
    {
        try
        {
            sC.changeOtherPort(Integer.parseInt(str));
        }catch(Exception e){cBF.setStatus("Invalid Transmitting Port :"+e);}
    }
    
    */
}
