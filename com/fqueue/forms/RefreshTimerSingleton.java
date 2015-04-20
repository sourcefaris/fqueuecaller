/*
 * Created on Apr 30, 2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fqueue.forms;

import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * @author bill
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RefreshTimerSingleton extends Timer {

	/**
	 * @param delay
	 * @param listener
	 */
	
	private RefreshTimerSingleton(int delay, ActionListener listener) {
		super(delay, listener);
		// TODO Auto-generated constructor stub
	}
	
	public static synchronized RefreshTimerSingleton getInstance(int delay, ActionListener listener) {
		if (ref == null)
	          ref = new RefreshTimerSingleton(delay, listener);
	      return ref;
	}
	
	private static RefreshTimerSingleton ref;
	
}
