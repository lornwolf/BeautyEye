/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 */
package org.jb2011.lnf.beautyeye.ch7_popup;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolTip;
import javax.swing.JWindow;
import javax.swing.MenuElement;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboPopup;

import org.jb2011.lnf.beautyeye.utils.WindowTranslucencyHelper;
import org.jb2011.lnf.beautyeye.widget.ImageBgPanel;

public class TranslucentPopupFactory extends PopupFactory {

    @Override
    public Popup getPopup(Component owner, Component contents, int x, int y) {
        return new TranslucentPopup(owner, contents, x, y);
    }

    protected class TranslucentPopup extends Popup {
        private Component component;
        public TranslucentPopup(Component owner, Component contents, int x, int y) {
            this(); reset(owner, contents, x, y);
        }
        public TranslucentPopup() {}
        public void show() { if (component != null) { component.setVisible(true); component.repaint(); } }
        public void hide() {
            if (component instanceof JWindow) {
                component.setVisible(false);
                ((JWindow) component).getContentPane().removeAll();
            }
            dispose();
        }
        protected void dispose() {
            if (component instanceof JWindow) ((Window) component).dispose();
        }
        protected void reset(Component owner, Component contents, int ownerX, int ownerY) {
            if (getComponent() == null) component = createComponent(owner);
            if (component instanceof JWindow) {
                JWindow component = (JWindow) getComponent();
                component.setLocation(ownerX, ownerY);
                // 彻底锁定白色背景，确保无缝融合
                component.setBackground(Color.WHITE);

                boolean isTooltip = (contents instanceof JToolTip);
                boolean isComboBoxPopup = (contents instanceof BasicComboPopup);

                WindowTranslucencyHelper.setWindowOpaque(component, false);
                WindowTranslucencyHelper.setOpacity(component, 0.99f);

                // 使用加强版 ImageBgPanel，强制清除每一像素的残留色
                ImageBgPanel imageContentPane = new ImageBgPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        // 强制透写
                        g2.setComposite(AlphaComposite.Clear);
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.dispose();
                        // 如果透明开启，背景不需要画；如果透明受限，它默认会是白色
                        super.paintComponent(g);
                    }
                }.setN9(
                    isTooltip? __Icon9Factory__.getInstance().getTooltipBg()
                    :isComboBoxPopup? org.jb2011.lnf.beautyeye.ch4_scroll.__Icon9Factory__.getInstance().getScrollPaneBorderBg()
                    :__Icon9Factory__.getInstance().getPopupBg()
                );
                imageContentPane.setOpaque(false);
                imageContentPane.setBackground(Color.WHITE);
                imageContentPane.setLayout(new BorderLayout());
                imageContentPane.add(contents, BorderLayout.CENTER);

                component.getRootPane().setOpaque(false);
                component.getRootPane().setBackground(Color.WHITE);
                if (component.getContentPane() instanceof JComponent) {
                    ((JComponent)component.getContentPane()).setOpaque(false);
                    ((JComponent)component.getContentPane()).setBackground(Color.WHITE);
                }

                if (contents instanceof JComponent) {
                    JComponent jc = (JComponent) contents;
                    jc.setOpaque(false);
                    jc.setBackground(Color.WHITE);
                    jc.setBorder(isTooltip ? BorderFactory.createEmptyBorder(6,8,12,12) 
                        : isComboBoxPopup? BorderFactory.createEmptyBorder(6,4,6,4) 
                        : BorderFactory.createEmptyBorder(5,3,6,3));
                }

                component.setContentPane(imageContentPane);
                if (component.isVisible()) pack();
            }
        }
        protected void pack() { if (component instanceof Window) ((Window) component).pack(); }
        protected Window getParentWindow(Component owner) {
            Window window = (owner instanceof Window) ? (Window) owner : (owner != null ? SwingUtilities.getWindowAncestor(owner) : null);
            return (window == null) ? new DefaultFrame() : window;
        }
        protected Component createComponent(Component owner) { return new HeavyWeightWindow(getParentWindow(owner)); }
        protected Component getComponent() { return component; }
        protected class DefaultFrame extends Frame {}
        protected class HeavyWeightWindow extends JWindow {
            public HeavyWeightWindow(Window parent) {
                super(parent);
                setFocusableWindowState(true);
                setName("###overrideRedirect###");
                try { setAlwaysOnTop(true); } catch (SecurityException se) {}
            }
            public void update(Graphics g) { paint(g); }
            public void show() { this.pack(); super.show(); }
        }
    }
}
