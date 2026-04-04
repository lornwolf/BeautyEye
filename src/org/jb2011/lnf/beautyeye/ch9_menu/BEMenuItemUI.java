/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * BEMenuItemUI.java at 2015-2-1 20:25:38, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */

package org.jb2011.lnf.beautyeye.ch9_menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;

/**
 * JMenuItem的UI实现类。.
 *
 * @author lornwolf
 */
public class BEMenuItemUI extends BasicMenuItemUI {

    /** 是否强制单项透明(当强制不透明时，在普通状态下该item将不会被绘制背景）. */
    private static boolean enforceTransparent = true;
    
    /**
     * Creates the ui.
     *
     * @param c the c
     * @return the component ui
     */
    public static ComponentUI createUI(JComponent c) {
        return new BEMenuItemUI();
    }


    @Override
    protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
        // see parent!
        ButtonModel model = menuItem.getModel();
        Color oldColor = g.getColor();
        int menuWidth = menuItem.getWidth();
        int menuHeight = menuItem.getHeight();
        
        Graphics2D g2 = (Graphics2D)g;
        
        if (model.isArmed()
                || (menuItem instanceof JMenu && model.isSelected())) {
            //菜单项的样式绘制(用NinePatch图来填充)
            __Icon9Factory__.getInstance().getBgIcon_ItemSelected()
                    .draw(g2, 0, 0, menuWidth, menuHeight);
        } else {
            // 用父容器背景色清除高亮残留（模拟透明效果，与含子菜单的菜单项保持一致）
            java.awt.Container parent = menuItem.getParent();
            g.setColor(parent != null ? parent.getBackground() : menuItem.getBackground());
            g.fillRect(0, 0, menuWidth, menuHeight);
        }
        g.setColor(oldColor);
    }
}
