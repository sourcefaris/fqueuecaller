package com.rubean.rcms.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.UIManager;

public class RubeanUI
{

    public RubeanUI()
    {
    }

    public static void setRubeanUI()
        throws Exception
    {
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        UIManager.put("Button.select", RUBEAN_SELECTION_COLOR);
        UIManager.put("CheckBox.background", Color.white);
        UIManager.put("CheckBox.foreground", Color.black);
        UIManager.put("ComboBox.background", Color.white);
        UIManager.put("ComboBox.foreground", Color.black);
        UIManager.put("ComboBox.selectionBackground", RUBEAN_SELECTION_COLOR);
        UIManager.put("ComboBox.disabledBackground", Color.white);
        UIManager.put("ComboBox.disabledForeground", Color.gray);
        UIManager.put("EditorPane.selectionForeground", Color.black);
        UIManager.put("EditorPane.selectionBackground", RUBEAN_SELECTION_COLOR);
        UIManager.put("HorizontalLineComponent.background", RUBEAN_COLOR);
        UIManager.put("HorizontalLineComponent.foreground", RUBEAN_COLOR);
        UIManager.put("Label.background", Color.white);
        UIManager.put("Label.foreground", Color.black);
        UIManager.put("Label.font", new Font(_FONT_TYPE, 0, _FONT_SIZE));
        UIManager.put("List.font", new Font(_FONT_TYPE, 0, _FONT_SIZE));
        UIManager.put("List.selectionForeground", Color.black);
        UIManager.put("List.selectionBackground", RUBEAN_SELECTION_COLOR);
        UIManager.put("MenuItem.selectionBackground", RUBEAN_SELECTION_COLOR);
        UIManager.put("OptionPane.background", Color.white);
        UIManager.put("Panel.background", Color.white);
        UIManager.put("PasswordField.selectionForeground", Color.black);
        UIManager.put("PasswordField.selectionBackground", RUBEAN_SELECTION_COLOR);
        UIManager.put("ProgressBar.foreground", RUBEAN_COLOR);
        UIManager.put("ProgressBar.background", Color.white);
        UIManager.put("ScrollBar.background", Color.white);
        UIManager.put("ScrollBar.thumbHighlight", RUBEAN_COLOR);
        UIManager.put("ScrollBar.thumb", RUBEAN_COLOR);
        UIManager.put("SplitPane.background", Color.white);
        UIManager.put("TabbedPane.selected", RUBEAN_SELECTION_COLOR);
        UIManager.put("TabbedPane.background", Color.white);
        UIManager.put("TabbedPane.darkShadow", Color.lightGray);
        UIManager.put("TabbedPane.selectHighlight", UIManager.get("TabbedPane.darkShadow"));
        UIManager.put("Table.foreground", Color.black);
        UIManager.put("Table.background", Color.white);
        UIManager.put("Table.selectionForeground", Color.black);
        UIManager.put("Table.selectionBackground", RUBEAN_SELECTION_COLOR);
        UIManager.put("Table.scrollPaneBorder", BorderFactory.createLineBorder(Color.lightGray, 1));
        UIManager.put("TableHeader.font", new Font(_FONT_TYPE, 1, _FONT_SIZE));
        UIManager.put("TableHeader.background", RUBEAN_COLOR);
        UIManager.put("TableHeader.foreground", Color.white);
        UIManager.put("TextField.selectionForeground", Color.black);
        UIManager.put("TextField.selectionBackground", RUBEAN_SELECTION_COLOR);
        UIManager.put("TitledBorder.titleColor", RUBEAN_COLOR);
        UIManager.put("TitledBorder.font", new Font(_FONT_TYPE, 0, _FONT_SIZE));
        UIManager.put("ToolTip.background", RUBEAN_TOOLTIP_BACK);
        UIManager.put("ToolTip.foreground", RUBEAN_TOOLTIP_FORE);
        UIManager.put("Tree.collapsedIcon", new Icon() {

            public int getIconHeight()
            {
                return 0;
            }

            public int getIconWidth()
            {
                return 0;
            }

            public void paintIcon(Component component, Graphics g, int i, int j)
            {
            }

        });
        UIManager.put("Tree.expandedIcon", new Icon() {

            public int getIconHeight()
            {
                return 0;
            }

            public int getIconWidth()
            {
                return 0;
            }

            public void paintIcon(Component component, Graphics g, int i, int j)
            {
            }

        });
        UIManager.put("Tree.hash", RUBEAN_SELECTION_COLOR);
        UIManager.put("Tree.background", Color.white);
        UIManager.put("Tree.selectionBackground", RUBEAN_COLOR);
        UIManager.put("Tree.selectionForeground", Color.white);
		UIManager.put("TextArea.selectionBackground", RUBEAN_SELECTION_COLOR);
    }

    public static Color getRubeanColor(){
        return new Color(71, 147, 43);
    }
    
    public static Color getRubeanSelectionColor(){
        return new Color (221, 247, 204);
    }
    
    public static Font getFont(){
        return new Font("Arial", Font.PLAIN, 12);
    }
    
    public static Font getFontBold(){
        return new Font("Arial", Font.BOLD, 12);
    }
    
    public static final Color RUBEAN_COLOR = new Color(71, 147, 43);
    public static final Color RUBEAN_TOOLTIP_BACK = new Color(255, 255, 175);
    public static final Color RUBEAN_TOOLTIP_FORE = new Color(1, 1, 148);
    public static final Color RUBEAN_SELECTION_COLOR = new Color(221, 247, 204);
    public static int _FONT_SIZE=12;
    public static String _FONT_TYPE = "Arial";
//    public static final LineBorder lbor = (LineBorder)BorderFactory.createLineBorder(RUBEAN_SELECTION_COLOR);
}