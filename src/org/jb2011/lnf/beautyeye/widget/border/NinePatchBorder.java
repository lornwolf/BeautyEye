/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * NinePatchBorder.java at 2015-2-1 20:25:36, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye.widget.border;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

import org.jb2011.ninepatch4j.NinePatch;

/**
 * 一个利用NinePatch图实现边框的border实现类.
 * <p>
 * 本类可以很好地被重用于NinePatch图作为border实现的场景哦.
 * 
 * @author lornwolf, 2012-09-04
 * @version 1.0
 */
public class NinePatchBorder extends AbstractBorder {

    /** The insets. */
    private Insets insets = null;

    /** The np. */
    private NinePatch np = null;

    /**
     * Instantiates a new nine patch border.
     *
     * @param insets the insets
     * @param np the np
     */
    public NinePatchBorder(Insets insets, NinePatch np) {
        this.insets = insets;
        this.np = np;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        return insets;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        this.np.draw((Graphics2D)g, x, y, width, height);
        
        if (c instanceof javax.swing.JRootPane && !org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.__isFrameBorderOpaque()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            
            // 使用 AlphaComposite.Clear 彻底擦除内外黑边自带的尖顶直角！
            g2.setComposite(java.awt.AlphaComposite.Clear);
            
            int ix = x + insets.left;
            int iy = y + insets.top;
            int iw = width - insets.left - insets.right;
            int ih = height - insets.top - insets.bottom;
            
            // 1. 修剪内部窗口内容的4个多余直角
            int bx = ix - 1;
            int by = iy - 1;
            int bw = iw + 2;
            int bh = ih + 2;
            java.awt.geom.Area innerCorners = new java.awt.geom.Area(new java.awt.Rectangle(bx, by, bw, bh));
            innerCorners.subtract(new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Double(bx, by, bw, bh, 26, 26)));
            g2.fill(innerCorners);
            
            // 2. 修剪外部半透明阴影的4个多余直角（因为阴影贴图本身是为直角窗口设计的）
            // 让阴影本身的轮廓边界也拥有圆滑的倒角效果，彻底告别直角的错觉！
            java.awt.geom.Area outerCorners = new java.awt.geom.Area(new java.awt.Rectangle(x, y, width, height));
            outerCorners.subtract(new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Double(x, y, width, height, 44, 44)));
            g2.fill(outerCorners);
            
            g2.dispose();
        }
    }
}
