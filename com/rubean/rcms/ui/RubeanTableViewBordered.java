/*
 * Created on May 8, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.rubean.rcms.ui;

import java.awt.Color;

import javax.swing.BorderFactory;

import com.rubean.ui.beantable.BeanTableView;

/**
 * @author pkristia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RubeanTableViewBordered extends BeanTableView{
    
    public RubeanTableViewBordered(){
        super();
        setBorder(BorderFactory.createLineBorder(new Color(71, 147, 43)));
    }    
}
