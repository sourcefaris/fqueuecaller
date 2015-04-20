
package com.fqueue.commons;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.border.Border;

import com.rubean.rcms.ui.RubeanDialog;
import com.rubean.rcms.ui.RubeanLabel;

public class WaitDialog extends RubeanDialog {

	public WaitDialog(){
		waitImage = new ImageIcon(getClass().getResource("WAITING.GIF"));
		
		try
		{
			jbInit();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		pack();
	}
 
 public WaitDialog(String imageName){
 	 if (imageName==null)
 	 	waitImage = new ImageIcon(getClass().getResource("WAITING.GIF"));
 	 else
 	 	waitImage = new ImageIcon(getClass().getResource(imageName));
 	 	
     try
     {
         jbInit();
     }
     catch(Exception exception)
     {
         exception.printStackTrace();
     }
     pack();
 }

 public WaitDialog(Frame frame, boolean flag)
 {
     super(frame);
//     res = ResourceBundle.getBundle("com.rubean.rcms.msf.Res");
     gridBagLayout1 = new GridBagLayout();
     jLabel1 = new RubeanLabel();
//     waitImage = new ImageIcon(getClass().getResource("WAITING.GIF"));
     waitImage = new ImageIcon(getClass().getResource("CONNECT.GIF"));
     super.setModal(flag);
     try
     {
         jbInit();
     }
     catch(Exception exception)
     {
         exception.printStackTrace();
     }
     pack();
 }

 
 private void jbInit()
     throws Exception
 {
     border1 = BorderFactory.createEmptyBorder(4, 16, 4, 16);
     gridBagLayout1 = new GridBagLayout();
     jLabel1 = new RubeanLabel();
     jLabel2 = new RubeanLabel();
     jLabel1.setFont(new Font("Dialog", 0, 14));
     jLabel1.setBorder(border1);
     jLabel1.setIcon(waitImage);
     jLabel1.setIconTextGap(10);
//     jLabel1.setText(infoWait);
     jLabel1.setBackground(Color.white);
     
     jLabel2.setText(infoWait);
     getContentPane().setLayout(gridBagLayout1);
     getContentPane().setBackground(Color.white);
     setSize(500,350);
     setModal(true);
     setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
     getContentPane().add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
     getContentPane().add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
     
 }
 
 public void setInfoWait(String infoWait) {
 	jLabel2.setText(infoWait);
 }

  
 GridBagLayout gridBagLayout1;
 RubeanLabel jLabel1;
 RubeanLabel jLabel2;
 ImageIcon waitImage;
 Border border1;
 String infoWait="Wait";
 String imageWait="WAITING.GIF";
 
}