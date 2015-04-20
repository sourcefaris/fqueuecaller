/*
 * Created on Apr 28, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.rubean.rcms.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author pkristia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RubeanImageButtonNew extends RubeanImageButton{
    private MouseListener mouseListener = null;
    public RubeanImageButtonNew(){
        super();
        
        setForeground(new Color(71, 147, 43));
        
        mouseListener = new MouseListener()
        {
            public void mouseClicked(MouseEvent e) { }

            public void mouseEntered(MouseEvent e) {
                setCursor(cursorHand);
                setForeground(new Color(255, 10, 10));
            }

            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
                setForeground(new Color(71, 147, 43));
            }

            public void mousePressed(MouseEvent e) { }

            public void mouseReleased(MouseEvent e) { }
        };
            
        addMouseListener(mouseListener);
        
    }
    
    private Cursor cursorHand = new Cursor(Cursor.HAND_CURSOR);
}