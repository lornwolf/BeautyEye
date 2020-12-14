/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * BETabbedPaneUI.java at 2015-2-1 20:25:36, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye.ch2_tab;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import org.jb2011.lnf.beautyeye.utils.BEUtils;

/**
 * JTabbedPane的UI实现类.
 * 
 * @author lornwolf, 2012-01-12
 * @version 1.1
 */
public class BETabbedPaneUI extends BasicTabbedPaneUI {
    
    /**
     * Creates the ui.
     *
     * @param c the c
     * @return the component ui
     */
    public static ComponentUI createUI(JComponent c) {
        return new BETabbedPaneUI();
    }
    
    /* 
     * 本方法的重写copy自 com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI，基本未修改代码
     * 重写父类方法实现rover状态下的tab的UI重绘（父类方法只是实现了rolloverTab的设置，不步及
     * UI重绘制，因Basic LNF中没有实现rover状态的UI样式）
     */
    protected void setRolloverTab(int index)  {
        // Rollover is only supported on XP
        // if (XPStyle.getXP() != null)
        {
            int oldRolloverTab = getRolloverTab();
            super.setRolloverTab(index);
            Rectangle r1 = null;
            Rectangle r2 = null;
            if ( (oldRolloverTab >= 0) && (oldRolloverTab < tabPane.getTabCount()) ) {
                r1 = getTabBounds(tabPane, oldRolloverTab);
            }
            if (index >= 0) {
                r2 = getTabBounds(tabPane, index);
            }
            if (r1 != null) {
                if (r2 != null) {
                    tabPane.repaint(r1.union(r2));
                } else {
                    tabPane.repaint(r1);
                }
            } else if (r2 != null) {
                tabPane.repaint(r2);
            }
        }
    }

    /**
     * this function draws the border around each tab
     * note that this function does now draw the background of the tab.
     * that is done elsewhere
     *
     * @param g the g
     * @param tabPlacement the tab placement
     * @param tabIndex the tab index
     * @param x the x
     * @param y the y
     * @param w the w
     * @param h the h
     * @param isSelected the is selected
     */
    protected void paintTabBorder(Graphics g, int tabPlacement,
            int tabIndex, int x, int y, int w, int h, boolean isSelected ) {
        // g.setColor(lightHighlight);  
        // Graphics2D g2d = (Graphics2D)g;
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.translate(x, y);

        //* true表示该tab当前正处于鼠标rover其上的状态
        //* this.getRolloverTab()的返回值由父类方法 setRolloverTab()设定并实现ui重绘的
        boolean isRover = (this.getRolloverTab() == tabIndex);
        //* true表示该tab处于可用状态，否则表示处于禁用状态
        boolean isEnableAt = this.tabPane.isEnabledAt(tabIndex);

        switch (tabPlacement) 
        {
            case LEFT:
                g2d.scale(-1.0, 1.0);
                g2d.rotate(Math.toRadians(90.0));
                paintTabBorderImpl(g2d, isEnableAt, isSelected, isRover, 0, 0, h, w);
                break;
            case RIGHT:
                g2d.translate(w, 0);
                g2d.rotate(Math.toRadians(90.0));
                paintTabBorderImpl(g2d, isEnableAt, isSelected, isRover, 0, 0, h, w);
                break;              
            case BOTTOM:
                g2d.translate(0, h);
                g2d.scale(-1.0, 1.0);
                g2d.rotate(Math.toRadians(180.0));
                paintTabBorderImpl(g2d, isEnableAt, isSelected, isRover, 10, 0, w, h);
                break;
            case TOP:
            default:
                paintTabBorderImpl(g2d, isEnableAt, isSelected, isRover, 0, 0, w, h);
                break;
        }
    }

    /**
     * paintTabBorder的绘制实现方法。
     * Paint tab border impl.
     *
     * @param g2d the g2d
     * @param isEnableAt the is enable at
     * @param isSelected the is selected
     * @param isRover the is rover
     * @param x the x
     * @param y the y
     * @param w the w
     * @param h the h
     */
    private void paintTabBorderImpl(Graphics2D g2d, boolean isEnableAt, boolean isSelected
            , boolean isRover, int x, int y, int w, int h)
    {
        // 改为NinePatch图片实现，Y + 1 的目的是使得选中时的底线能往下画一个像素（这样好看点）
        if(isSelected) // 选中状态
            __Icon9Factory__.getInstance().getTabbedPaneBgSelected().draw(g2d, x, y + 1, w, h);
        else
        {
            if(isEnableAt && isRover) // rover状态
                __Icon9Factory__.getInstance().getTabbedPaneBgNormal_rover().draw(g2d, x, y + 1 , w, h);
            else // 正常状态
                __Icon9Factory__.getInstance().getTabbedPaneBgNormal().draw(g2d, x, y + 1 , w, h);;
        }
    }

    /*
     * 重写本方法的目的仅是把原来的默认实线变成虚线而已。
     */
    @Override
    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        // 虚线样式
        Stroke oldStroke = ((Graphics2D)g).getStroke();
        Stroke sroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 0, new float[]{2, 2}, 0); // 实线，空白
        ((Graphics2D)g).setStroke(sroke);

        // 调用父类默认实现
        super.paintContentBorder(g, tabPlacement, selectedIndex);

        ((Graphics2D)g).setStroke(oldStroke);
    }

    /*
     * JTabbedPaneUI 内容面板的上边框绘制方法（默认就是内容面板上方的那条灰色）
     */
    protected void paintContentBorderTopEdge(Graphics g, int tabPlacement,
            int selectedIndex, 
            int x, int y, int w, int h) {
        //此模式下不绘制其它3条边框，视觉上好看一些
        if(tabPlacement == TOP)
            //调用父类默认实现
            super.paintContentBorderTopEdge(g, tabPlacement, selectedIndex, x, y, w, h);
    }

    /*
     * JTabbedPaneUI 内容面板的左边框绘制方法
     */
    protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement,
            int selectedIndex,
            int x, int y, int w, int h) { 
        //此模式下不绘制其它3条边框，视觉上好看一些
        if(tabPlacement == LEFT)
            //调用父类默认实现
            super.paintContentBorderLeftEdge(g, tabPlacement, selectedIndex, x, y, w, h);
    }

    /*
     * JTabbedPaneUI 内容面板的底边框绘制方法
     */
    protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement,
            int selectedIndex,
            int x, int y, int w, int h) 
    { 
        // 此模式下不绘制其它3条边框，视觉上好看一些
        if(tabPlacement == BOTTOM)
            // 调用父类默认实现
            super.paintContentBorderBottomEdge(g, tabPlacement, selectedIndex, x, y, w, h);
    }

    /*
     * JTabbedPaneUI 内容面板的右边框绘制方法。
     */
    protected void paintContentBorderRightEdge(Graphics g, int tabPlacement,
            int selectedIndex,
            int x, int y, int w, int h) {
        // 此模式下不绘制其它3条边框，视觉上好看一些
        if (tabPlacement == RIGHT)
            // 调用父类默认实现
            super.paintContentBorderRightEdge(g, tabPlacement, selectedIndex, x, y, w, h);
    }

    /*
     * 获得焦点时的虚线框绘制方法。
     */
    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement,
            Rectangle[] rects, int tabIndex, 
            Rectangle iconRect, Rectangle textRect,
            boolean isSelected) {
        Rectangle tabRect = rects[tabIndex];
        if (tabPane.hasFocus() && isSelected) {
            int x, y, w, h;
            g.setColor(focus);
            switch(tabPlacement) {
                case LEFT:
                    x = tabRect.x + 4; // 父类中默认是+3
                    y = tabRect.y + 6; // 父类中默认是+3
                    w = tabRect.width - 7; //父 类中默认是 - 5
                    h = tabRect.height - 12; // 父类中默认是-6
                    break;
                case RIGHT:
                    x = tabRect.x + 4; // 父类中默认是+ 2
                    y = tabRect.y + 6; // 父类中默认是+ 3
                    w = tabRect.width - 9; // 父类中默认是- 5
                    h = tabRect.height - 12; // 父类中默认是- 6
                    break;
                case BOTTOM:
                    x = tabRect.x + 6; // 父类中默认是+ 3
                    y = tabRect.y + 4; // 父类中默认是+ 2
                    w = tabRect.width - 12; // 父类中默认是- 6
                    h = tabRect.height - 9; // 父类中默认是- 5
                    break;
                case TOP:
                default:
                    // 根据整体效果进行偏移修正
                    x = tabRect.x + 6; // 父类中默认是+3
                    // 根据整体效果进行偏移修正
                    y = tabRect.y + 4; // 父类中默认是+3
                    // 根据整体效果进行偏移修正
                    w = tabRect.width - 12; // 父类中默认是-6
                    // -8的目的是使得焦点虚线框与选中底边保持一个像素的距离，否则挨在一起在视觉上效果会较差
                    h = tabRect.height - 8; // 父类中默认是 - 5
            }

            // 绘制虚线方法改成可以设置虚线步进的方法，步进设为2则更好看一点
            // BasicGraphicsUtils.drawDashedRect(g, x, y, w, h);
            BEUtils.drawDashedRect(g, x, y, w, h);
            // 绘制虚线框的半透明白色立体阴影（因主背景色较淡，效果不明显，但显然比没有要好）
            g.setColor(new Color(255, 255, 255, 255));
            // 立体阴影就是向右下偏移一个像素实现的
            BEUtils.drawDashedRect(g, x + 1, y + 1,w, h);
        }
    }

    /*
     * 重写并修改本方法的目的是修正tab上的文本显示Y坐标方向上的偏移，以便与背景协调
     */
    protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
        Rectangle tabRect = rects[tabIndex];
        int nudge = 0;
        switch(tabPlacement) {
            case BOTTOM:
                nudge = isSelected? 1 : -1;
                break;
            case LEFT:
            case RIGHT:
                nudge = tabRect.height % 2;
                break;
            case TOP:
            default:
                /* 目的是使得选中时和未选中时的文本（包括图标默认实现中之所以要产生它个效果是为了营造立体效果，而BE LNF中并不需要）
                 * 不要往上或往下偏移的太多（太多则相当难看）
                 */
                nudge = isSelected ? -1 : 1; // 本行是原父类中的默认实现
                // nudge = -2; // 让文本相对现在的背景往上偏移一点，好看一些
        }
        return nudge;
    }
}
