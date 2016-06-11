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
import java.awt.event.*;

class cFunc {
   // String 
    int p1Up,p1Down,p2Up,p2Down;
    MShooter MS;
    cFunc(MShooter ms)
    {
        p1Up=KeyEvent.VK_W;
        p1Down=KeyEvent.VK_S;
        p2Up=KeyEvent.VK_UP;
        p2Down=KeyEvent.VK_DOWN;
        MS=ms;
    }
    void keyPressed(int x)
    {
        if(!MS.socketGame) 
        {
        if(x==p1Up)   MS.moveUp(false);
        else if(x==p1Down)  MS.moveDown(false);
        else if(x==p2Up)   MS.moveUp(true);
        else if(x==p2Down)  MS.moveDown(true);
        
        }
        else
        {
            if(x==p1Up || x==p2Up) MS.moveKUp();
            else if(x==p1Down || x==p2Down) MS.moveKDown();
            
        }
            
    }
    int keyUP()
    {
        if(MS.meP1)
            return p1Up;
        else return p2Up;
    }
    int keyDOWN()
    {
        if(MS.meP1)
            return p1Down;
        else return p2Down;
    }
}
