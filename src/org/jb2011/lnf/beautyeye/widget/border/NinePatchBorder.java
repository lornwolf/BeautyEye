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
        if (c instanceof javax.swing.JRootPane && !org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.__isFrameBorderOpaque()) {
            // FORGET straight 9-patch textures! They have baked-in sharp corners that cause floating artifacts when sheared!
            // We mathematically draw a perfectly round, flawless 15-layer soft drop shadow.
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            
            int ix = x + insets.left;
            int iy = y + insets.top;
            int iw = width - insets.left - insets.right;
            int ih = height - insets.top - insets.bottom;
            
            // Drop shadow parameters
            int shadowSize = 18;
            int yOffset = 4;
            int alphaStep = 4; // Additive alpha per layer
            
            // Draw from outside in
            for (int i = shadowSize; i >= 0; i--) {
                g2.setColor(new java.awt.Color(0, 0, 0, alphaStep));
                // ensure outer radiuses perfectly follow concentricity: r_outer = r_inner + border_thickness
                int arc = 26 + i * 2; 
                g2.fillRoundRect(ix - i, iy - i + yOffset, iw + i * 2, ih + i * 2, arc, arc);
            }
            
            g2.dispose();
        } else {
            // Normal execution for other components or opaque frames
            this.np.draw((Graphics2D)g, x, y, width, height);
        }
    }
}
