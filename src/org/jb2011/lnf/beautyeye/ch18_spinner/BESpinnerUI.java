/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * BESpinnerUI.java at 2015-2-1 20:25:39, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye.ch18_spinner;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;

import org.jb2011.lnf.beautyeye.ch18_spinner.BESpinnerUI.GlyphButton.Type;
import org.jb2011.lnf.beautyeye.utils.BEUtils;

/**
 * JSPinner的UI实现类。.
 *
 * @author lornwolf
 * @version 1.0
 */
//参考自WindowsSpinnerUI 由lornwolf修改
public class BESpinnerUI extends BasicSpinnerUI {

    /**
     * Creates the ui.
     *
     * @param c the c
     * @return the component ui
     */
    public static ComponentUI createUI(JComponent c) {
        return new BESpinnerUI();
    }

    /* (non-Javadoc)
     * @see javax.swing.plaf.basic.BasicSpinnerUI#createEditor()
     */
    protected JComponent createEditor() {
        JComponent e = super.createEditor();
        e.setOpaque(false);


        //参见JSpinner.NumberEditor，super.createEditor()返回值就是它的父类
        //（是一个JPanel实例），它是由一个FormatttedTextField及其父JPanel组成
        //的，所以设置完 e.setOpaque(false)，还要把它的子FormatttedTextField
        //设置成透明，其实它的子只有1个，它里为了适用未来的扩展假设它有很多子，
        Component[] childs = e.getComponents();
        BEUtils.componentsOpaque(childs, false);

        return e;
    }

    /**
     * Paint.
     *
     * @param g the g
     * @param c the c
     * {@inheritDoc}
     * @since 1.6
     */
    public void paint(Graphics g, JComponent c) {
        {
            paintXPBackground(g, c);
        }
        super.paint(g,c);
    }

    /**
     * Paint xp background.
     *
     * @param g the g
     * @param c the c
     */
    private void paintXPBackground(Graphics g, JComponent c) {
        if (spinner != null && !spinner.isEnabled())
            __Icon9Factory__.getInstance().getSpinnerDisableBg().
                draw((Graphics2D)g, 0, 0, c.getWidth(), c.getHeight());
        else
            __Icon9Factory__.getInstance().getSpinnerBg().
                draw((Graphics2D)g, 0, 0, c.getWidth(), c.getHeight());
    }

    /* (non-Javadoc)
     * @see javax.swing.plaf.basic.BasicSpinnerUI#createPreviousButton()
     */
    protected Component createPreviousButton() {
        {
            JButton xpButton = new GlyphButton(spinner, Type.down);
            Dimension size = UIManager.getDimension("Spinner.arrowButtonSize");
            xpButton.setPreferredSize(size);
            xpButton.setRequestFocusEnabled(false);
            installPreviousButtonListeners(xpButton);
            return xpButton;
        }
    }


    /* (non-Javadoc)
     * @see javax.swing.plaf.basic.BasicSpinnerUI#createNextButton()
     */
    protected Component createNextButton() {
        {
            JButton xpButton = new GlyphButton(spinner, Type.up);
            Dimension size = UIManager.getDimension("Spinner.arrowButtonSize");
            xpButton.setPreferredSize(size);
            xpButton.setRequestFocusEnabled(false);
            installNextButtonListeners(xpButton);
            return xpButton;
        }
    }

    //copy from com.sun.java.swing.plaf.windows.XPStyle.GlyphButton
    /**
     * The Class GlyphButton.
     */
    static class GlyphButton extends JButton {
        /** The type. */
        private Type type = null;
        
        /**
         * The Enum Type.
         */
        public enum Type {
            /** The down. */
            down,
            /** The up. */
            up
        }

        /**
         * Instantiates a new glyph button.
         *
         * @param parent the parent
         * @param type the type
         */
        public GlyphButton(Component parent, Type type) {
            this.type = type;
            setBorder(null);
            setContentAreaFilled(false);
            setMinimumSize(new Dimension(5, 5));
            setPreferredSize(new Dimension(16, 16));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        }   


        /* (non-Javadoc)
         * @see java.awt.Component#isFocusTraversable()
         */
        public boolean isFocusTraversable() {
            return false;
        }

        /* (non-Javadoc)
         * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
         */
        public void paintComponent(Graphics g) {
            Dimension d = getSize();
            
            if (type == Type.up) {
                if (!isEnabled() || getModel().isPressed()) 
                    __Icon9Factory__.getInstance().getUpButtonBg_pressed().
                        draw((Graphics2D)g, 0, 0, d.width, d.height);
                else
                    __Icon9Factory__.getInstance().getUpButtonBg().
                        draw((Graphics2D)g, 0, 0, d.width, d.height);
            } else if (type == Type.down) {
                if (!isEnabled() || getModel().isPressed()) 
                    __Icon9Factory__.getInstance().getDownButtonBg_pressed().
                        draw((Graphics2D)g, 0, 0, d.width, d.height);
                else
                    __Icon9Factory__.getInstance().getDownButtonBg().
                        draw((Graphics2D)g, 0, 0, d.width, d.height);
            }
        }

        /* (non-Javadoc)
         * @see javax.swing.AbstractButton#paintBorder(java.awt.Graphics)
         */
        protected void paintBorder(Graphics g) {    
        }
    }
}
