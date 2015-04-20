//Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
//Jad home page: http://kpdus.tripod.com/jad.html
//Decompiler options: packimports(3) 

package com.rubean.rcms.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

public class RubeanImageButton extends JButton
{

 public RubeanImageButton()
 {
     try
     {
         jbInit();
     }
     catch(Exception exception)
     {
         exception.printStackTrace();
     }
 }

 private void jbInit()
     throws Exception
 {
     defaultBorder = BorderFactory.createLineBorder(Color.white);
     selectedBorder = BorderFactory.createLineBorder(Color.gray);
     setBorder(defaultBorder);
     setBackground(Color.white);
     addMouseListener(new MouseListener()
     {
         public void mouseClicked(MouseEvent e) { }
         public void mouseEntered(MouseEvent e) {
             setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
             setForeground(new Color(255, 10, 10));
         }

         public void mouseExited(MouseEvent e) {
             setCursor(Cursor.getDefaultCursor());
//             setForeground(new Color(71, 147, 43));
             setForeground(Color.BLACK);
         }

         public void mousePressed(MouseEvent e) { }

         public void mouseReleased(MouseEvent e) { }
     });

     addFocusListener(new FocusAdapter() {

         public void focusGained(FocusEvent focusevent)
         {
             if(!focusIndicated)
             {
                 setBorder(selectedBorder);
                 focusIndicated = true;
             }
         }

         public void focusLost(FocusEvent focusevent)
         {
             setBorder(defaultBorder);
             focusIndicated = false;
         }

     });
 }

 public boolean isFocusTraversable()
 {
     return false;
 }

 boolean focusIndicated;
 Border defaultBorder;
 Border selectedBorder;
}
