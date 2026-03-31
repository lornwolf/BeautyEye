/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * FocusCellHighlightBorder.java at 2015-2-1 20:25:40, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye.ch5_table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;

import org.jb2011.lnf.beautyeye.utils.BEUtils;

/**
 * 表格单元获得焦点时的Border实现类。.
 *
 * @author lornwolf
 */
//本border由lornwolf实现，它是表格单元获得焦点时的边框（类似的功能在windows LNF下是一个距形虚线框）
class FocusCellHighlightBorder extends AbstractBorder {

    public Insets getBorderInsets(Component c) {
        return new Insets(2,2,2,2); // @since 3.5
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        return getBorderInsets(c);
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        //* @since 3.5
        BEUtils.draw4RecCorner(g, x, y, width-2, height-2, 5
                , UIManager.getColor("Table.focusCellHighlightBorderColor"));
        BEUtils.draw4RecCorner(g, x+1, y+1, width-2, height-2, 5
                , UIManager.getColor("Table.focusCellHighlightBorderHighlightColor"));
    }
}
