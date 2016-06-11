/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfindersolver;

/**
 *
 * @author Gagan
 */
import java.awt.event.*;
public class takingPoints  implements MouseListener,MouseMotionListener{
PathFinderSolver PFS;
    public takingPoints(PathFinderSolver obj) {
        obj.addMouseListener(this);
        obj.addMouseMotionListener(this);
        PFS=obj;
    }

    
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
            coordinate c=new coordinate();
        c.set(e.getX(), e.getY()-20);
        //System.out.println(c);
        PFS.setPoints(c);

    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }
    
}
