/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * SplitPaneDividerBorder.java at 2015-2-1 20:25:41, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye.ch17_split;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * 分隔条的border实现类.
 * <p>
 * Draws the border around the divider in a splitpane. To get the appropriate effect, this
 * needs to be used with a SplitPaneBorder.
 * 
 * @author lornwolf
 */
//本类参考自jdk1.6_u18的javax.swing.plaf.basic.BasicBorders
//    .SplitPaneDividerBorder的源码，主要修改了UI填充实现部分
public class SplitPaneDividerBorder implements Border, UIResource {

    /* (non-Javadoc)
     * @see javax.swing.border.Border#paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int)
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width,
            int height) {
        //在目前的视觉效果下不需要这个border的绘制哦
    }

    /* (non-Javadoc)
     * @see javax.swing.border.Border#getBorderInsets(java.awt.Component)
     */
    public Insets getBorderInsets(Component c) {
        Insets insets = new Insets(0, 0, 0, 0);
        if (c instanceof BasicSplitPaneDivider) {
            BasicSplitPaneUI bspui = ((BasicSplitPaneDivider) c)
                    .getBasicSplitPaneUI();

            if (bspui != null) {
                JSplitPane splitPane = bspui.getSplitPane();

                if (splitPane != null) {
                    if (splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
                        insets.top = insets.bottom = 0;
                        insets.left = insets.right = 1;
                        return insets;
                    }
                    // VERTICAL_SPLIT
                    insets.top = insets.bottom = 1;
                    insets.left = insets.right = 0;
                    return insets;
                }
            }
        }
        insets.top = insets.bottom = insets.left = insets.right = 1;
        return insets;
    }

    /* (non-Javadoc)
     * @see javax.swing.border.Border#isBorderOpaque()
     */
    public boolean isBorderOpaque() {
        return true;
    }
}
