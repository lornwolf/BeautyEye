/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * BERootPaneUI.java at 2015-2-1 20:25:40, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye.ch1_titlepane;

import org.jb2011.lnf.beautyeye.utils.BEUtils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicRootPaneUI;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.utils.WindowTranslucencyHelper;

/**
 * 绐椾綋鐨刄I瀹炵幇.
 * 
 * @author lornwolf
 */
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 涓€浜涜鏄?Start
//* 鏈被鐨勫疄鐜板弬鑰冧簡java1.5涓殑MetalRootPaneUI.
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 涓€浜涜鏄?END
public class BERootPaneUI extends BasicRootPaneUI 
{
    @Override
    public void paint(Graphics g, JComponent c) {
        boolean rounded = BEUtils.isFrameRound(c);
        if (rounded) {
            // 缁堟瀬鍔寔锛氬彧瑕佹湁灞炴€э紝涓嶇 LNF 鐘舵€佸浣曪紝寮哄埗寮€鍚€忔槑
            Window win = SwingUtilities.getWindowAncestor(c);
            if (win != null && win.isOpaque()) {
                WindowTranslucencyHelper.setWindowOpaque(win, false);
            }
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            
            Insets i = c.getInsets();
            int ix = i.left;
            int iy = i.top;
            int iw = c.getWidth() - i.left - i.right;
            int ih = c.getHeight() - i.top - i.bottom;
            
            // 瀹炴椂瑁佸垏锛岀粷瀵逛笉鐣欐瑙?
            Shape oldClip = g2.getClip();
            RoundRectangle2D.Float roundClip = new RoundRectangle2D.Float(ix, iy, iw, ih, 26, 26);
            if (oldClip == null) {
                g2.setClip(roundClip);
            } else {
                Area area = new Area(oldClip);
                area.intersect(new Area(roundClip));
                g2.setClip(area);
            }
        }
        super.paint(g, c);
    }

    @Override
    public void update(Graphics g, JComponent c) {
        boolean rounded = BEUtils.isFrameRound(c);
        if (rounded || !BeautyEyeLNFHelper.__isFrameBorderOpaque()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            
            // 基础参数
            int radius = rounded ? 26 : 0;
            Insets i = c.getInsets();
            int ix = i.left, iy = i.top, iw = c.getWidth() - i.left - i.right, ih = c.getHeight() - i.top - i.bottom;

            // 1. 绘制高质量分层阴影
            int shadowSize = 18;
            for (int j = shadowSize; j >= 0; j--) {
                g2.setColor(new java.awt.Color(0, 0, 0, 2)); 
                int arc = radius + j * 2; 
                g2.fillRoundRect(ix - j, iy - j + 4, iw + j * 2, ih + j * 2, arc, arc);
            }

            // 2. 绘制圆角背景
            JRootPane root = (JRootPane) c;
            Color bg = (root.getContentPane() != null) ? root.getContentPane().getBackground() : UIManager.getColor("Panel.background");
            g2.setComposite(AlphaComposite.SrcOver);
            g2.setColor(bg);
            g2.fillRoundRect(ix, iy, iw, ih, radius, radius);
            g2.dispose();
        } else if (c.isOpaque()) {
            g.setColor(c.getBackground());
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
        }
        paint(g, c);
    }

    /**
     * Keys to lookup borders in defaults table.
     */
    private static final String[] borderKeys = new String[] {
        null
        , "RootPane.frameBorder"
        , "RootPane.plainDialogBorder"
        , "RootPane.informationDialogBorder"
        , "RootPane.errorDialogBorder"
        , "RootPane.colorChooserDialogBorder"
        , "RootPane.fileChooserDialogBorder"
        , "RootPane.questionDialogBorder"
        , "RootPane.warningDialogBorder"
    };
    
    //* 2012-09-19 鍦˙eautyEye v3.2涓甯搁噺琚玪ornwolf鍙栨秷浜嗭紝鍥犱负
    //* v3.2涓惎鐢ㄤ簡鐩告瘮鍘烳etalRootPaneUI涓洿绮剧‘鏇村ソ鐨勮竟妗嗘嫋鏀剧畻娉?

    /**
     * Region from edges that dragging is active from.
     */
    //绐楀彛鍙嫋鍔ㄦ晱鎰熻Е鐐瑰尯鍩熷ぇ灏忚璁剧疆澶氬ぇ鍙栧喅浜庝綘鐭ュ畾涔塨order鐨刬nsets锛岄粯璁ゆ槸 5;
    private static final int BORDER_DRAG_THICKNESS = 5;
        //BeautyEyeLNFHelper.__getFrameBorder_BORDER_DRAG_THICKNESS();//涓轰簡渚?寰楃敤鎴风殑鏁忔劅瑙︾偣鍖烘洿澶э紝鎻愰珮鐢ㄦ埛浣撻獙锛屾鍊煎彲鍔犲ぇ

    /**
     * Window the <code>JRootPane</code> is in.
     */
    private Window window;

    /**
     * <code>JComponent</code> providing window decorations. This will be
     * null if not providing window decorations.
     */
    private JComponent titlePane;

    /**
     * <code>MouseInputListener</code> that is added to the parent
     * <code>Window</code> the <code>JRootPane</code> is contained in.
     */
    private MouseInputListener mouseInputListener;

    /**
     * The <code>LayoutManager</code> that is set on the
     * <code>JRootPane</code>.
     */
    private LayoutManager layoutManager;

    /**
     * <code>LayoutManager</code> of the <code>JRootPane</code> before we
     * replaced it.
     */
    private LayoutManager savedOldLayout;

    /**
     * <code>JRootPane</code> providing the look and feel for.
     */
    private JRootPane root;

    /**
     * <code>Cursor</code> used to track the cursor set by the user.  
     * This is initially <code>Cursor.DEFAULT_CURSOR</code>.
     */
    private Cursor lastCursor =
        Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    
    //* 鐢眏b2011 澧炲姞锛氱敤浜庡湪绐楀彛琚縺娲讳笌涓嶆縺娲绘椂鑷姩璁剧疆瀹冪殑閫忔槑搴︼紙涓嶆縺娲绘椂璁句负鍗婇€忔槑锛?
    /** The windows listener. */
    private WindowListener windowsListener = null;
    
    /**
     * Creates a UI for a <code>JRootPane</code>.
     *
     * @param c the JRootPane the RootPaneUI will be created for
     * @return the RootPaneUI implementation for the passed in JRootPane
     */
    public static ComponentUI createUI(JComponent c) 
    {
        return new BERootPaneUI();
    }

    /**
     * Invokes supers implementation of <code>installUI</code> to install
     * the necessary state onto the passed in <code>JRootPane</code>
     * to render the metal look and feel implementation of
     * <code>RootPaneUI</code>. If
     * the <code>windowDecorationStyle</code> property of the
     * <code>JRootPane</code> is other than <code>JRootPane.NONE</code>,
     * this will add a custom <code>Component</code> to render the widgets to
     * <code>JRootPane</code>, as well as installing a custom
     * <code>Border</code> and <code>LayoutManager</code> on the
     * <code>JRootPane</code>.
     *
     * @param c the JRootPane to install state onto
     */
    /**
     * 透明层级面板：它是所有组件的“出口”，在这里强制实施 26px 的圆角裁切。
     */
    private class BEClipLayeredPane extends JLayeredPane {
        @Override
        public void paintChildren(Graphics g) {
            if (BEUtils.isFrameRound(root)) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                Insets i = root.getInsets();
                // 这里的坐标是相对于 LayeredPane 的，所以通常是 0,0
                int iw = root.getWidth() - i.left - i.right;
                int ih = root.getHeight() - i.top - i.bottom;
                
                // 终极拦截：无论子组件如何画，在这里都被统一修剪
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, iw, ih, 26, 26));
                super.paintChildren(g2);
                g2.dispose();
            } else {
                super.paintChildren(g);
            }
        }
    }

    public void installUI(JComponent c) 
    { 
        super.installUI(c);
        
        root = (JRootPane)c;
        int style = root.getWindowDecorationStyle();
        root.addPropertyChangeListener(this);
        
        boolean rounded = BEUtils.isFrameRound(root);
        // 信号同步：如果开启了圆角，强行劫持透明度与层级面板
        if (rounded || !BeautyEyeLNFHelper.__isFrameBorderOpaque()) {
            root.setOpaque(false);
            root.setBackground(new java.awt.Color(0,0,0,0));
            
            // 物理替换内容容器
            if (!(root.getLayeredPane() instanceof BEClipLayeredPane)) {
                JLayeredPane oldPane = root.getLayeredPane();
                BEClipLayeredPane newPane = new BEClipLayeredPane();
                newPane.setOpaque(false);
                
                // 迁移组件
                Component[] comps = oldPane.getComponents();
                for (Component comp : comps) {
                    newPane.add(comp, oldPane.getLayer(comp));
                }
                root.setLayeredPane(newPane);
            }
            
            if (root.getContentPane() instanceof JComponent) {
                ((JComponent) root.getContentPane()).setOpaque(false);
            }
        }
        
        if (style != JRootPane.NONE) 
        {
            installClientDecorations(root);
        }
    }


    /**
     * Invokes supers implementation to uninstall any of its state. This will
     * also reset the <code>LayoutManager</code> of the <code>JRootPane</code>.
     * If a <code>Component</code> has been added to the <code>JRootPane</code>
     * to render the window decoration style, this method will remove it.
     * Similarly, this will revert the Border and LayoutManager of the
     * <code>JRootPane</code> to what it was before <code>installUI</code>
     * was invoked.
     *
     * @param c the JRootPane to uninstall state from
     */
    public void uninstallUI(JComponent c) 
    {
        super.uninstallUI(c);
        uninstallClientDecorations(root);

        layoutManager = null;
        mouseInputListener = null;
        root = null;
    }

    /**
     * Installs the appropriate <code>Border</code> onto the
     * <code>JRootPane</code>.
     *
     * @param root the root
     */
    void installBorder(JRootPane root) 
    {
        int style = root.getWindowDecorationStyle();

        if (style == JRootPane.NONE) 
        {
            LookAndFeel.uninstallBorder(root);
        }
        else 
        {
            Border b = root.getBorder();
            if (b == null || b instanceof UIResource) 
            {
                root.setBorder(null);
                root.setBorder(UIManager.getBorder(borderKeys[style]));
            }
        }
    }

    /**
     * Removes any border that may have been installed.
     *
     * @param root the root
     */
    private void uninstallBorder(JRootPane root) 
    {
        LookAndFeel.uninstallBorder(root);
    }

    /**
     * Installs the necessary Listeners on the parent <code>Window</code>,
     * if there is one.
     * <p>
     * This takes the parent so that cleanup can be done from
     * <code>removeNotify</code>, at which point the parent hasn't been
     * reset yet.
     *
     * @param root the root
     * @param parent The parent of the JRootPane
     */
    private void installWindowListeners(JRootPane root, Component parent) 
    {
        if (parent instanceof Window)
        {
            window = (Window)parent;
        }
        else 
        {
            window = SwingUtilities.getWindowAncestor(parent);
        }
        if (window != null) 
        {
            if (mouseInputListener == null) 
            {
                mouseInputListener = createWindowMouseInputListener(root);
            }
            
            window.addMouseListener(mouseInputListener);
            window.addMouseMotionListener(mouseInputListener);
            
            //* add by JS 2011-12-27,缁欑獥鍙ｅ鍔犵洃鍚櫒锛氬湪涓嶆椿鍔ㄦ椂璁剧疆绐楀彛鍗婇€忔槑锛屾椿鍔ㄦ椂杩樺師
            if(BeautyEyeLNFHelper.translucencyAtFrameInactive)
            {
                if(windowsListener == null)
                {
                    windowsListener = new WindowAdapter(){
                        public void windowActivated(WindowEvent e) {
                            if(window != null)
                                WindowTranslucencyHelper.setOpacity(window, 1.0f);
                        }
                        public void windowDeactivated(WindowEvent e) {
                            if(window != null)
                                WindowTranslucencyHelper.setOpacity(window, 0.94f);
                        }
                    };
                }
                window.addWindowListener(windowsListener);
            }
        }
    }

    /**
     * Uninstalls the necessary Listeners on the <code>Window</code> the
     * Listeners were last installed on.
     *
     * @param root the root
     */
    private void uninstallWindowListeners(JRootPane root)
    {
        if (window != null)
        {
            window.removeMouseListener(mouseInputListener);
            window.removeMouseMotionListener(mouseInputListener);
        }
    }

    /**
     * Installs the appropriate LayoutManager on the <code>JRootPane</code>
     * to render the window decorations.
     *
     * @param root the root
     */
    private void installLayout(JRootPane root)
    {
        if (layoutManager == null)
        {
            layoutManager = createLayoutManager();
        }
        savedOldLayout = root.getLayout();
        root.setLayout(layoutManager);
    }

    /**
     * Uninstalls the previously installed <code>LayoutManager</code>.
     *
     * @param root the root
     */
    private void uninstallLayout(JRootPane root) 
    {
        if (savedOldLayout != null) 
        {
            root.setLayout(savedOldLayout);
            savedOldLayout = null;
        }
    }

    /**
     * Installs the necessary state onto the JRootPane to render client
     * decorations. This is ONLY invoked if the <code>JRootPane</code>
     * has a decoration style other than <code>JRootPane.NONE</code>.
     *
     * @param root the root
     */
    private void installClientDecorations(JRootPane root)
    {
        installBorder(root);

        JComponent titlePane = createTitlePane(root);

        setTitlePane(root, titlePane);
        installWindowListeners(root, root.getParent());
        installLayout(root);
        
        //鍙湁鍦ㄧ獥鍙ｈ竟妗嗘槸鍗婇€忔槑鐨勬儏鍐典笅锛屼互涓嬫墠闇€瑕佽缃獥鍙ｉ€忔槑
        //* 娉ㄦ剰锛氭湰绫讳腑鐨勬澶勪唬鐮佺殑鐩殑灏辨槸涓轰簡瀹炵幇鍗婇€忔槑杈规绐楀彛鐨?
        //* 姝ｅ父鏄剧ず锛岃€屼笖浠呴拡瀵规鐩殑銆傚鏋滆杈规涓嶄负閫忔槑锛屽垯姝ゅ涔熷氨涓嶉渶瑕佽缃?
        //* 绐楀彛閫忔槑浜嗭紝閭ｄ箞濡傛灉浣犵殑绋嬪簭鍏跺畠鍦版柟闇€瑕佺獥鍙ｉ€忔槑鐨勮瘽锛岃嚜琛?setWindowOpaque(..)
        //* 灏辫浜嗭紝鐢卞紑鍙戣€呰嚜鍏堝喅瀹氾紝姝ゅ灏变笉鎵胯浇杩囧鐨勮姹備簡
        if (!BeautyEyeLNFHelper.__isFrameBorderOpaque() 
                && window != null)
        {
            WindowTranslucencyHelper.setWindowOpaque(window, false);
            root.revalidate();
            root.repaint();
        }
    }

    /**
     * Uninstalls any state that <code>installClientDecorations</code> has
     * installed.
     * <p>
     * NOTE: This may be called if you haven't installed client decorations
     * yet (ie before <code>installClientDecorations</code> has been invoked).
     *
     * @param root the root
     */
    private void uninstallClientDecorations(JRootPane root) 
    {
        uninstallBorder(root);
        uninstallWindowListeners(root);
        setTitlePane(root, null);
        uninstallLayout(root);
        // We have to revalidate/repaint root if the style is JRootPane.NONE
        // only. When we needs to call revalidate/repaint with other styles
        // the installClientDecorations is always called after this method
        // imediatly and it will cause the revalidate/repaint at the proper
        // time.
        int style = root.getWindowDecorationStyle();
        if (style == JRootPane.NONE) 
        {
            root.repaint();
            root.revalidate();
        }
        // Reset the cursor, as we may have changed it to a resize cursor
        if (window != null) 
        {
            window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        window = null;
    }

    /**
     * Returns the <code>JComponent</code> to render the window decoration
     * style.
     *
     * @param root the root
     * @return the j component
     */
    private JComponent createTitlePane(JRootPane root) 
    {
        return new BETitlePane(root, this);
    }

    /**
     * Returns a <code>MouseListener</code> that will be added to the
     * <code>Window</code> containing the <code>JRootPane</code>.
     *
     * @param root the root
     * @return the mouse input listener
     */
    private MouseInputListener createWindowMouseInputListener(JRootPane root) 
    {
        return new MouseInputHandler();
    }

    /**
     * Returns a <code>LayoutManager</code> that will be set on the
     * <code>JRootPane</code>.
     *
     * @return the layout manager
     */
    private LayoutManager createLayoutManager() 
    {
        return new XMetalRootLayout();
    }

    /**
     * Sets the window title pane -- the JComponent used to provide a plaf a
     * way to override the native operating system's window title pane with
     * one whose look and feel are controlled by the plaf.  The plaf creates
     * and sets this value; the default is null, implying a native operating
     * system window title pane.
     *
     * @param root the root
     * @param titlePane the title pane
     */
    private void setTitlePane(JRootPane root, JComponent titlePane) 
    {
        JLayeredPane layeredPane = root.getLayeredPane();
        JComponent oldTitlePane = getTitlePane();

        if (oldTitlePane != null)
        {
            oldTitlePane.setVisible(false);
            layeredPane.remove(oldTitlePane);
        }
        if (titlePane != null) 
        {
            layeredPane.add(titlePane, JLayeredPane.FRAME_CONTENT_LAYER);
            titlePane.setVisible(true);
        }
        this.titlePane = titlePane;
    }

    /**
     * Returns the <code>JComponent</code> rendering the title pane. If this
     * returns null, it implies there is no need to render window decorations.
     *
     * @return the current window title pane, or null
     * @see #setTitlePane
     */
    private JComponent getTitlePane() 
    {
        return titlePane;
    }

    /**
     * Returns the <code>JRootPane</code> we're providing the look and
     * feel for.
     *
     * @return the root pane
     */
    private JRootPane getRootPane() 
    {
        return root;
    }

    /**
     * Invoked when a property changes. <code>MetalRootPaneUI</code> is
     * primarily interested in events originating from the
     * <code>JRootPane</code> it has been installed on identifying the
     * property <code>windowDecorationStyle</code>. If the 
     * <code>windowDecorationStyle</code> has changed to a value other
     * than <code>JRootPane.NONE</code>, this will add a <code>Component</code>
     * to the <code>JRootPane</code> to render the window decorations, as well
     * as installing a <code>Border</code> on the <code>JRootPane</code>.
     * On the other hand, if the <code>windowDecorationStyle</code> has
     * changed to <code>JRootPane.NONE</code>, this will remove the
     * <code>Component</code> that has been added to the <code>JRootPane</code>
     * as well resetting the Border to what it was before
     * <code>installUI</code> was invoked.
     *
     * @param e A PropertyChangeEvent object describing the event source 
     *          and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent e) 
    {
        super.propertyChange(e);

        String propertyName = e.getPropertyName();
        if(propertyName == null) 
        {
            return;
        }

        if(propertyName.equals("windowDecorationStyle")) 
        {
            JRootPane root = (JRootPane) e.getSource();
            int style = root.getWindowDecorationStyle();

            // This is potentially more than needs to be done,
            // but it rarely happens and makes the install/uninstall process
            // simpler. MetalTitlePane also assumes it will be recreated if
            // the decoration style changes.
            
            uninstallClientDecorations(root);
            if (style != JRootPane.NONE) 
            {
                installClientDecorations(root);
            }
        }
        else if (propertyName.equals("contentPane")) 
        {
            if (e.getNewValue() instanceof JComponent && Boolean.TRUE.equals(root.getClientProperty("BeautyEye.frameRound"))) {
                ((JComponent) e.getNewValue()).setOpaque(false);
            }
        }
        else if (propertyName.equals("BeautyEye.frameRound") || propertyName.equals("frameRound"))
        {
            boolean rounded = BEUtils.isFrameRound(root);
            root.setOpaque(!rounded);
            
            if (root.getLayeredPane() != null) root.getLayeredPane().setOpaque(!rounded);
            if (root.getContentPane() instanceof JComponent) {
                ((JComponent) root.getContentPane()).setOpaque(!rounded);
            }
            
            Window win = SwingUtilities.getWindowAncestor(root);
            if (win != null) {
                WindowTranslucencyHelper.setWindowOpaque(win, !rounded);
            }
            root.revalidate();
            root.repaint();
        }
        else if (propertyName.equals("ancestor")) 
        {
            uninstallWindowListeners(root);
            if (((JRootPane)e.getSource()).getWindowDecorationStyle() !=
                JRootPane.NONE) 
            {
                installWindowListeners(root, root.getParent());
            }
        }
        return;
    } 

    /** 
     * A custom layout manager that is responsible for the layout of 
     * layeredPane, glassPane, menuBar and titlePane, if one has been
     * installed.
     */
    // NOTE: Ideally this would extends JRootPane.RootLayout, but that
    //       would force this to be non-static.
    private static class XMetalRootLayout implements LayoutManager2 
    {
        
        /**
         * Returns the amount of space the layout would like to have.
         *
         * @param parent the parent
         * @return a Dimension object containing the layout's preferred size
         */ 
        public Dimension preferredLayoutSize(Container parent) 
        {
            Dimension cpd, mbd, tpd;
            int cpWidth = 0;
            int cpHeight = 0;
            int mbWidth = 0;
            int mbHeight = 0;
            int tpWidth = 0;
            int tpHeight = 0;
            Insets i = parent.getInsets();
            JRootPane root = (JRootPane) parent;

            if(root.getContentPane() != null) 
            {
                cpd = root.getContentPane().getPreferredSize();
            } 
            else 
            {
                cpd = root.getSize();
            }
            if (cpd != null) 
            {
                cpWidth = cpd.width;
                cpHeight = cpd.height;
            }

            if(root.getMenuBar() != null) 
            {
                mbd = root.getMenuBar().getPreferredSize();
                if (mbd != null)
                {
                    mbWidth = mbd.width;
                    mbHeight = mbd.height;
                }
            } 

            if (root.getWindowDecorationStyle() != JRootPane.NONE &&
                    (root.getUI() instanceof BERootPaneUI)) 
            {
                JComponent titlePane = ((BERootPaneUI)root.getUI()).
                getTitlePane();
                if (titlePane != null) 
                {
                    tpd = titlePane.getPreferredSize();
                    if (tpd != null) 
                    {
                        tpWidth = tpd.width;
                        tpHeight = tpd.height;
                    }
                }
            }

            return new Dimension(Math.max(Math.max(cpWidth, mbWidth), tpWidth) + i.left + i.right, 
                    cpHeight + mbHeight + tpWidth + i.top + i.bottom);
        }

        /**
         * Returns the minimum amount of space the layout needs.
         *
         * @param parent the parent
         * @return a Dimension object containing the layout's minimum size
         */ 
        public Dimension minimumLayoutSize(Container parent) 
        {
            Dimension cpd, mbd, tpd;
            int cpWidth = 0;
            int cpHeight = 0;
            int mbWidth = 0;
            int mbHeight = 0;
            int tpWidth = 0;
            int tpHeight = 0;
            Insets i = parent.getInsets();
            JRootPane root = (JRootPane) parent;

            if(root.getContentPane() != null) 
            {
                cpd = root.getContentPane().getMinimumSize();
            } 
            else 
            {
                cpd = root.getSize();
            }
            if (cpd != null) 
            {
                cpWidth = cpd.width;
                cpHeight = cpd.height;
            }

            if(root.getMenuBar() != null) 
            {
                mbd = root.getMenuBar().getMinimumSize();
                if (mbd != null) {
                    mbWidth = mbd.width;
                    mbHeight = mbd.height;
                }
            }            
            if (root.getWindowDecorationStyle() != JRootPane.NONE &&
                    (root.getUI() instanceof BERootPaneUI)) {
                JComponent titlePane = ((BERootPaneUI)root.getUI()).
                getTitlePane();
                if (titlePane != null) 
                {
                    tpd = titlePane.getMinimumSize();
                    if (tpd != null) 
                    {
                        tpWidth = tpd.width;
                        tpHeight = tpd.height;
                    }
                }
            }

            return new Dimension(Math.max(Math.max(cpWidth, mbWidth), tpWidth) + i.left + i.right, 
                    cpHeight + mbHeight + tpWidth + i.top + i.bottom);
        }

        /**
         * Returns the maximum amount of space the layout can use.
         *
         * @param target the target
         * @return a Dimension object containing the layout's maximum size
         */ 
        public Dimension maximumLayoutSize(Container target)
        {
            Dimension cpd, mbd, tpd;
            int cpWidth = Integer.MAX_VALUE;
            int cpHeight = Integer.MAX_VALUE;
            int mbWidth = Integer.MAX_VALUE;
            int mbHeight = Integer.MAX_VALUE;
            int tpWidth = Integer.MAX_VALUE;
            int tpHeight = Integer.MAX_VALUE;
            Insets i = target.getInsets();
            JRootPane root = (JRootPane) target;

            if(root.getContentPane() != null)
            {
                cpd = root.getContentPane().getMaximumSize();
                if (cpd != null) 
                {
                    cpWidth = cpd.width;
                    cpHeight = cpd.height;
                }
            }

            if(root.getMenuBar() != null) 
            {
                mbd = root.getMenuBar().getMaximumSize();
                if (mbd != null) 
                {
                    mbWidth = mbd.width;
                    mbHeight = mbd.height;
                }
            }

            if (root.getWindowDecorationStyle() != JRootPane.NONE &&
                    (root.getUI() instanceof BERootPaneUI))
            {
                JComponent titlePane = ((BERootPaneUI)root.getUI()).
                getTitlePane();
                if (titlePane != null)
                {
                    tpd = titlePane.getMaximumSize();
                    if (tpd != null) 
                    {
                        tpWidth = tpd.width;
                        tpHeight = tpd.height;
                    }
                }
            }

            int maxHeight = Math.max(Math.max(cpHeight, mbHeight), tpHeight);
            // Only overflows if 3 real non-MAX_VALUE heights, sum to > MAX_VALUE
            // Only will happen if sums to more than 2 billion units.  Not likely.
            if (maxHeight != Integer.MAX_VALUE) 
            {
                maxHeight = cpHeight + mbHeight + tpHeight + i.top + i.bottom;
            }

            int maxWidth = Math.max(Math.max(cpWidth, mbWidth), tpWidth);
            // Similar overflow comment as above
            if (maxWidth != Integer.MAX_VALUE)
            {
                maxWidth += i.left + i.right;
            }

            return new Dimension(maxWidth, maxHeight);
        }

        /**
         * Instructs the layout manager to perform the layout for the specified
         * container.
         *
         * @param parent the parent
         */ 
        @SuppressWarnings("deprecation")
        public void layoutContainer(Container parent) 
        {
            JRootPane root = (JRootPane) parent;
            Rectangle b = root.getBounds();
            Insets i = root.getInsets();
            int nextY = 0;
            int w = b.width - i.right - i.left;
            int h = b.height - i.top - i.bottom;

            if(root.getLayeredPane() != null)
            {
                root.getLayeredPane().setBounds(i.left, i.top, w, h);
            }
            if(root.getGlassPane() != null) 
            {
                root.getGlassPane().setBounds(i.left, i.top, w, h);
            }
            // Note: This is laying out the children in the layeredPane,
            // technically, these are not our children.
            if (root.getWindowDecorationStyle() != JRootPane.NONE &&
                    (root.getUI() instanceof BERootPaneUI)) 
            {
                JComponent titlePane = ((BERootPaneUI)root.getUI()).
                getTitlePane();
                if (titlePane != null) 
                {
                    Dimension tpd = titlePane.getPreferredSize();
                    if (tpd != null) 
                    {
                        int tpHeight = tpd.height;
                        titlePane.setBounds(0, 0, w, tpHeight);
                        nextY += tpHeight;
                    }                    
                }
            }
            if(root.getMenuBar() != null
                    //* 璇?琛屼唬鐮佺敱lornwolf浜?012-10-20澧炲姞锛氱洰鐨勬槸涓鸿В鍐冲綋
                    //* MebuBar琚缃笉鍙鏃朵换鐒惰閿欒鍦板綋浣滃彲瑙嗙粍浠跺崰鎹竷灞€绌洪棿锛岃繖
                    //* 鍦˙E LNF涓殑琛ㄧ幇灏辨槸褰搈enuBar涓嶅彲瑙侊紝瀹冨崰鎹殑閭ｅ潡绌洪棿灏嗕細鏄叏閫忔槑
                    //* 鐨勭┖鐧藉尯銆傝繖涓棶棰樺湪Metal涓婚涓粛鐒跺瓨鍦?灏辨槸璁剧疆JFrame.setDefaultLookAndFeelDecorated(true);
                    //* JDialog.setDefaultLookAndFeelDecorated(true);鍚庣殑Metal涓婚鐘舵€?锛?
                    //* 鍙兘瀹樻柟涓嶈涓鸿繖鏄釜bug鍚с€?
                    //* 涓轰粈涔堟棤璁轰粈涔堝瑙傚綋鍦ㄤ娇鐢ㄧ郴缁熺獥鍙ｈ竟妗嗙被鍨嬫椂涓嶄細鍑虹幇杩欐牱鐨勬儏鍐靛憿锛熷畠
                    //* 鍙兘鏄敱浜庣獥鍙ｅ瑙傜殑瀹炵幇鍘熺悊鍐冲畾鐨勫惂锛堟寜鐞嗚鏄悓涓€鍘熺悊锛夛紝鏈夊緟娣辩┒锛侊紒锛?
                    && root.getMenuBar().isVisible()
                    ) 
            {
                Dimension mbd = root.getMenuBar().getPreferredSize();
                root.getMenuBar().setBounds(0, nextY, w, mbd.height);
                nextY += mbd.height;
            }
            if(root.getContentPane() != null
                    && root.getContentPane().isVisible()
                    ) 
            {
                int contentH = h < nextY ? 0 : h - nextY;
                root.getContentPane().setBounds(0, nextY, w, contentH);
            }
        }

        public void addLayoutComponent(String name, Component comp) {}
        
        public void removeLayoutComponent(Component comp) {}
        
        public void addLayoutComponent(Component comp, Object constraints) {}
        
        public float getLayoutAlignmentX(Container target) { return 0.0f; }
        
        public float getLayoutAlignmentY(Container target) { return 0.0f; }
        
        public void invalidateLayout(Container target) {}
    }


    /**
     * Maps from positions to cursor type. Refer to calculateCorner and
     * calculatePosition for details of this.
     */
    private static final int[] cursorMapping = new int[]
      { Cursor.NW_RESIZE_CURSOR,
        Cursor.NW_RESIZE_CURSOR,
        Cursor.N_RESIZE_CURSOR,
        Cursor.NE_RESIZE_CURSOR,
        Cursor.NE_RESIZE_CURSOR,
        Cursor.NW_RESIZE_CURSOR,
        0,
        0,
        0,
        Cursor.NE_RESIZE_CURSOR,
        Cursor.W_RESIZE_CURSOR,
        0,
        0,
        0, 
        Cursor.E_RESIZE_CURSOR,
        Cursor.SW_RESIZE_CURSOR,
        0,
        0,
        0,
        Cursor.SE_RESIZE_CURSOR,
        Cursor.SW_RESIZE_CURSOR, 
        Cursor.SW_RESIZE_CURSOR,
        Cursor.S_RESIZE_CURSOR,
        Cursor.SE_RESIZE_CURSOR,
        Cursor.SE_RESIZE_CURSOR
      };

    /**
     * MouseInputHandler is responsible for handling resize/moving of
     * the Window. It sets the cursor directly on the Window when then
     * mouse moves over a hot spot.
     */
    private class MouseInputHandler implements MouseInputListener
    {
        /**
         * Set to true if the drag operation is moving the window.
         */
        private boolean isMovingWindow;

        /**
         * Used to determine the corner the resize is occuring from.
         */
        private int dragCursor;

        /**
         * X location the mouse went down on for a drag operation.
         */
        private int dragOffsetX;

        /**
         * Y location the mouse went down on for a drag operation.
         */
        private int dragOffsetY;

        /**
         * Width of the window when the drag started.
         */
        private int dragWidth;

        /**
         * Height of the window when the drag started.
         */
        private int dragHeight;

        /*
         * PrivilegedExceptionAction needed by mouseDragged method to
         * obtain new location of window on screen during the drag.
         */
        // Previously PrivilegedExceptionAction; SecurityManager removed in Java 9+. Kept as no-op.
        private final Runnable getLocationAction = new Runnable()
        {
            public void run()
            {
                // no-op; replaced by direct call below
            }
        };

        public void mousePressed(MouseEvent ev) {
            JRootPane rootPane = getRootPane();

            if (rootPane.getWindowDecorationStyle() == JRootPane.NONE)
            {
                return;
            }
            Point dragWindowOffset = ev.getPoint();
            Window w = (Window) ev.getSource();
            if (w != null)
            {
                w.toFront();
            }
            Point convertedDragWindowOffset = SwingUtilities.convertPoint(w,
                    dragWindowOffset, getTitlePane());

            Frame f = null;
            Dialog d = null;

            if (w instanceof Frame)
            {
                f = (Frame) w;
            }
            else if (w instanceof Dialog)
            {
                d = (Dialog) w;
            }

            int frameState = (f != null) ? f.getExtendedState() : 0;

            if (getTitlePane() != null
                    && getTitlePane().contains(convertedDragWindowOffset))
            {
                Insets insets = w.getInsets();
                if ((f != null && ((frameState & Frame.MAXIMIZED_BOTH) == 0) || (d != null))
                        && dragWindowOffset.y >= BORDER_DRAG_THICKNESS
                        && dragWindowOffset.x >= BORDER_DRAG_THICKNESS 
                        && dragWindowOffset.x < w.getWidth()
                                - BORDER_DRAG_THICKNESS)
                {
                    isMovingWindow = true;
                    dragOffsetX = dragWindowOffset.x;
                    dragOffsetY = dragWindowOffset.y;
                }
            }
            else if (f != null && f.isResizable()
                    && ((frameState & Frame.MAXIMIZED_BOTH) == 0)
                    || (d != null && d.isResizable()))
            {
                dragOffsetX = dragWindowOffset.x;
                dragOffsetY = dragWindowOffset.y;
                dragWidth = w.getWidth();
                dragHeight = w.getHeight();
                dragCursor = 
                        getCursor_new(w, dragWindowOffset.x,dragWindowOffset.y);
            }
        }

        public void mouseReleased(MouseEvent ev) {
            if (dragCursor != 0 && window != null && !window.isValid())
            {
                // Some Window systems validate as you resize, others won't,
                // thus the check for validity before repainting.
                window.validate();
                getRootPane().repaint();
            }
            isMovingWindow = false;
            dragCursor = 0;
        }

        public void mouseMoved(MouseEvent ev) {
            JRootPane root = getRootPane();

            if (root.getWindowDecorationStyle() == JRootPane.NONE)
            {
                return;
            }

            Window w = (Window) ev.getSource();

            Frame f = null;
            Dialog d = null;

            if (w instanceof Frame)
            {
                f = (Frame) w;
            }
            else if (w instanceof Dialog)
            {
                d = (Dialog) w;
            }

            // Update the cursor
            int cursor =
                getCursor_new(w, ev.getX(), ev.getY());

            if (cursor != 0
                    && ((f != null && (f.isResizable() && (f.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0)) || (d != null && d
                            .isResizable())))
            {
                w.setCursor(Cursor.getPredefinedCursor(cursor));
            }
            else
            {
                w.setCursor(lastCursor);
            }
        }

        /**
         * Adjust.
         *
         * @param bounds the bounds
         * @param min the min
         * @param deltaX the delta x
         * @param deltaY the delta y
         * @param deltaWidth the delta width
         * @param deltaHeight the delta height
         */
        private void adjust(Rectangle bounds, Dimension min, int deltaX,
                int deltaY, int deltaWidth, int deltaHeight)
        {
            bounds.x += deltaX;
            bounds.y += deltaY;
            bounds.width += deltaWidth;
            bounds.height += deltaHeight;
            if (min != null)
            {
                if (bounds.width < min.width)
                {
                    int correction = min.width - bounds.width;
                    if (deltaX != 0)
                    {
                        bounds.x -= correction;
                    }
                    bounds.width = min.width;
                }
                if (bounds.height < min.height)
                {
                    int correction = min.height - bounds.height;
                    if (deltaY != 0)
                    {
                        bounds.y -= correction;
                    }
                    bounds.height = min.height;
                }
            }
        }

        public void mouseDragged(MouseEvent ev) {
            Window w = (Window) ev.getSource();
            Point pt = ev.getPoint();

            if (isMovingWindow)
            {
                // Java 9+: SecurityManager/AccessController removed; call directly
                Point windowPt = MouseInfo.getPointerInfo().getLocation();
                windowPt.x = windowPt.x - dragOffsetX;
                windowPt.y = windowPt.y - dragOffsetY;
                w.setLocation(windowPt);
            }
            else if (dragCursor != 0)
            {
                Rectangle r = w.getBounds();
                Rectangle startBounds = new Rectangle(r);
                Dimension min = w.getMinimumSize();

                switch (dragCursor)
                {
                    case Cursor.E_RESIZE_CURSOR:
                        adjust(r, min, 0, 0, pt.x + (dragWidth - dragOffsetX)
                                - r.width, 0);
                        break;
                    case Cursor.S_RESIZE_CURSOR:
                        adjust(r, min, 0, 0, 0, pt.y
                                + (dragHeight - dragOffsetY) - r.height);
                        break;
                    case Cursor.N_RESIZE_CURSOR:
                        adjust(r, min, 0, pt.y - dragOffsetY, 0,
                                -(pt.y - dragOffsetY));
                        break;
                    case Cursor.W_RESIZE_CURSOR:
                        adjust(r, min, pt.x - dragOffsetX, 0,
                                -(pt.x - dragOffsetX), 0);
                        break;
                    case Cursor.NE_RESIZE_CURSOR:
                        adjust(r, min, 0, pt.y - dragOffsetY, pt.x
                                + (dragWidth - dragOffsetX) - r.width,
                                -(pt.y - dragOffsetY));
                        break;
                    case Cursor.SE_RESIZE_CURSOR:
                        adjust(r, min, 0, 0, pt.x + (dragWidth - dragOffsetX)
                                - r.width, pt.y + (dragHeight - dragOffsetY)
                                - r.height);
                        break;
                    case Cursor.NW_RESIZE_CURSOR:
                        adjust(r, min, pt.x - dragOffsetX, pt.y - dragOffsetY,
                                -(pt.x - dragOffsetX), -(pt.y - dragOffsetY));
                        break;
                    case Cursor.SW_RESIZE_CURSOR:
                        adjust(r, min, pt.x - dragOffsetX, 0,
                                -(pt.x - dragOffsetX), pt.y
                                        + (dragHeight - dragOffsetY) - r.height);
                        break;
                    default:
                        break;
                }
                if (!r.equals(startBounds))
                {
                    w.setBounds(r);
                    // Defer repaint/validate on mouseReleased unless dynamic
                    // layout is active.
                    if (Toolkit.getDefaultToolkit().isDynamicLayoutActive())
                    {
                        w.validate();
                        getRootPane().repaint();
                    }
                }
            }
        }

        public void mouseEntered(MouseEvent ev) {
            Window w = (Window) ev.getSource();
            lastCursor = w.getCursor();
            mouseMoved(ev);
        }

        public void mouseExited(MouseEvent ev) {
            Window w = (Window) ev.getSource();
            // Hack锛氬洜Swing榧犳爣浜嬩欢闂锛屾嫋鍔ㄨ繃蹇殑璇濆緢澶氭椂鍊欐病娉曟甯稿湴淇濈暀鍜岃缃甽astCursor
            //       浠庤€屽鑷寸粡甯告€х殑閫€鍑烘嫋鍔ㄥ悗锛屾嫋鍔ㄦ椂鐨勯紶鏍囨牱寮忚繕鍦紝杩欐牱寰堜笉鐖斤紝杩欏簲璇ユ槸swing
            //       鐨勯紶鏍囦簨浠朵笉绮剧‘瀵艰嚧鐨勬垨鍏跺畠闂銆傜洰鍓嶄笉濡傚共鑴冨湪閫€鍑烘嫋鍔ㄦ椂寮哄埗杩樺師鍒伴粯璁ら紶鏍囷紝
            //       铏界劧鍦ㄦ瀬灏戞儏鍐典笅鍙兘鍥炰笉鍒扮敤鎴风湡姝ｇ殑lastCursor锛屼絾璧风爜鑳借В鍐崇洰鍓嶅湪BueatyEye涓?
            //       鍥犲ぇborder鑰岄绻佸嚭鐜扮殑杩欎釜闂浜嗭紝鍏堣繖涔堟淮鍚э紒
            w.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        public void mouseClicked(MouseEvent ev) {
            Window w = (Window) ev.getSource();
            Frame f = null;

            if (w instanceof Frame)
            {
                f = (Frame) w;
            }
            else
            {
                return;
            }

            Point convertedPoint = SwingUtilities.convertPoint(w,
                    ev.getPoint(), getTitlePane());

            int state = f.getExtendedState();
            if (getTitlePane() != null
                    && getTitlePane().contains(convertedPoint))
            {
                if ((ev.getClickCount() % 2) == 0
                        && ((ev.getModifiers() & InputEvent.BUTTON1_MASK) != 0))
                {
                    if (f.isResizable())
                    {
                        if ((state & Frame.MAXIMIZED_BOTH) != 0)
                        {
                            f.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
                        }
                        else
                        {
                            f.setExtendedState(state | Frame.MAXIMIZED_BOTH);
                        }
                        return;
                    }
                }
            }
        }

        //*************************************************************** v3.2鍓嶅弬鑰冭嚜MetalRootPaneUI涓殑鑰佽竟妗嗘嫋鏀炬牳蹇冪畻娉?START
        //** 鑰佺畻娉曡鏄庯細Metal涓殑绠楁硶鏄亣璁剧獥鍙ｈ竟妗嗙殑border鏄鍒掔殑锛屽嵆涓婁笅宸﹀彸鐨刬nset閮芥槸涓€鏍风殑锛屽畠鍋囧畾鍙嫋鍔ㄨ寖鍥存槸鏁翠釜
        //**             绐椾綋澶у皬锛堝寘鎷琤order鍦ㄥ唴鐨勫ぇ灏忥級鐨凚ORDER_DRAG_THICKNESS甯搁噺鑼冨洿鍐呯殑涓婁笅宸﹀彸鍖哄煙锛屾墍浠ュ畠鐨?绠楁硶鍦?
        //**             姝ゅ墠棰樹笅閫氳繃杈冨阀濡欑殑鏂规硶绠€鍗曞疄鐜版病鏈夐棶棰樸€?
        //** 鑰佺畻娉曠己闄凤細褰撶獥鍙ｇ殑杈规涓嶈鍒掞紝濡侳rameBorderStyle.translucencyAppleLik杩欑鏃讹紙涓?17,宸?27,鍙?27,涓?37锛夛紝
        //**             姝ゆ儏鍐典笅鍙兘鍋囧畾涓€涓渶灏忓€间簡锛屼互鍓嶆槸鍙栫殑17浣滀负缁熶竴杈规鑼冨洿璺濈锛岄偅涔堝儚涓嬮儴鍘熸湰鏄?7鐨刬nset锛岀幇鍦ㄦ嫋鍔?
        //**             鑼冨洿鏄?7锛屼綑涓嬬殑鍘熸湰鏄痓order閲宨nsets鐨?0涓儚绱犱篃琚畻杩涚獥鍙ｅ唴瀹归潰鏉夸簡锛岃繖鏍峰鑷寸Щ鍔ㄥ埌涓嬫柟鏃讹紝鏄庢槑
        //**            鏄湪杈圭紭浣嶇疆锛屽嵈涓嶆槸澶勪簬鎷栧姩鑼冨洿鍐咃紙瑕佸啀寰€涓嬬Щ10鍍忕礌鍒拌揪inset鐨勭10~27鍍忕礌鑼冨洿鍐呮墠琛岋級锛岃繖鏍峰氨涓ラ噸
        //**             褰卞搷浜嗙敤鎴蜂綋楠屻€?
        //********************************************************************* v3.2鐗堝惎鐢ㄧ殑鏂拌竟妗嗘嫋鏀炬牳蹇冪畻娉?SART
        //** 鏂扮畻娉曡鏄庯細v3.2涓惎鐢ㄧ殑鏂扮畻娉曠殑鍘熺悊鏄妸鍙嫋鍔ㄨ寖鍥撮檺瀹氬湪鍐呭鍖猴紙鍗虫暣涓獥浣撳ぇ灏忓噺鍘籅order鍚庣殑鐪熸宸ヤ綔鍖猴級
        //**            寰€澶栫殑涓€涓浐瀹氱殑BORDER_DRAG_THICKNESS鍖哄煙鍐咃紝鍗充笉绠＄悊浣犳妸绐楀彛鐨刡order璁剧疆澶氫箞涓嶈鍒掞紝鎴戠殑鐢ㄦ埛鎷?
        //**            鍔ㄥ尯姘歌繙鏄繖涓€涓寖鍥村唴锛岃繖灏变繚璇佺敤鎴蜂綋楠岋紝杈冨ソ鐨勮В鍐充簡鑰佺畻娉曠殑缂洪櫡銆?
        /**
         * Gets the cursor_new.
         *
         * @param w the w
         * @param x the x
         * @param y the y
         * @return the cursor_new
         */
        public int getCursor_new(Window w, int x, int y)
        {
            Insets insets = w.getInsets();
            return getCursor_new(x - insets.left,y - insets.top
                    , w.getWidth() - insets.left - insets.right
                    , w.getHeight() - insets.top - insets.bottom);
        }
        
        /**
         * 鏂扮殑绐楀彛杈规鎷栧姩绠楁硶鐨勫疄鐜版槸鎶婂彲鎷栧姩鍖哄垎鎴?涓窛褰㈠尯锛屽綋榧犳爣鍔ㄥ埌瀵瑰簲
         * 鐨勫尯閲屽氨璁＄畻鍑烘槸鏄悜鍝釜鏂瑰悜鎷栧姩锛屾瘮MetalRootPaneUI涓殑绠€鏄撴柟娉曡鏄庣‘鍜岀簿纭€?
         * <p>
         * 鍙嫋鍔ㄥ垽鏂尯绀烘剰鍥撅細<br>
         * <u>绾㈣壊鍒拌摑鑹茬殑鏁翠釜鍖哄煙鏄獥鍙ｇ殑border鑼冨洿锛岀孩鑹插埌鐏拌壊鐨勫尯鍩熸槸鍥哄畾鐨勫彲鎷栧姩鍖猴紝绾㈣壊鍒扮伆鑹茬殑鍖哄煙鏄浐瀹氱殑锛?
         * 绾㈣壊鍒拌摑鑹茬殑鍖哄煙鍥燽order涓嶅悓鑰屼笉涓€鏍枫€?/u><br>
         * <b>娉ㄦ剰锛?/b>绠楁硶涓娉ㄦ剰涓€绉嶆瀬绔儏鍐碉紝灏辨槸Border鐨勪竴閮ㄥ垎鎴栧叏閮ㄩ兘灏忎簬鍙嫋鍔ㄥ尯鐨勬儏鍐碉紝浠ヤ笅绠楁硶搴旇涔?
         * 鏄病鏈夐棶棰樼殑锛屾棤闈炵畻鍑虹殑8鍙嫋鍔ㄨ窛褰㈠尯鍧愭爣鏈夎礋鐨勬儏鍐碉紝鍒濇娴嬭瘯杩囨病褰卞搷锛屼互鍚庤繕鏄敞鎰忎竴涓嬶紒
         * <table border="1" width="28%" cellpadding="10" height="185" bordercolor="#000080">
         * <tr>
         * <td align="center">
         * <table border="1" width="88%" id="table1" height="148" bordercolor="#808080">
         * <tr>
         * <td width="27" height="25" align="center">R1</td>
         * <td height="25" align="center">R2</td>
         * <td width="25" height="25" align="center">R3</td>
         * </tr>
         * <tr>
         * <td width="27" align="center">R8</td>
         * <td align="center" bordercolor="#FF0000">鍙宸ヤ綔鍖?/td>
         * <td width="25" align="center">R4</td>
         * </tr>
         * <tr>
         * <td width="27" height="25" align="center">R7</td>
         * <td height="25" align="center">R6</td>
         * <td width="25" height="25" align="center">R5</td>
         * </tr>
         * </table>
         * </td>
         * </tr>
         * </table>.
         *
         * @param x the x
         * @param y the y
         * @param w the w
         * @param h the h
         * @return the cursor_new
         */
        public int getCursor_new(int x, int y , int w, int h)
        {
            int B = BORDER_DRAG_THICKNESS;
            
            Insets iss = getRootPane().getInsets();
            int topI = iss.top, bottomI = iss.bottom, leftI = iss.left, rightI = iss.right; 
            
            //8涓嫋鍔ㄦ娴嬭窛褰㈠尯
            Rectangle r1 = new Rectangle(leftI-B,topI-B,B,B);
            Rectangle r2 = new Rectangle(leftI,topI-B,w-leftI-rightI,B);
            Rectangle r3 = new Rectangle(w-rightI,topI-B,B,B);
            Rectangle r4 = new Rectangle(w-rightI,topI,B,h-topI-bottomI);
            Rectangle r5 = new Rectangle(w-rightI,h-bottomI,B,B);
            Rectangle r6 = new Rectangle(leftI,h-bottomI,w-leftI-rightI,B);
            Rectangle r7 = new Rectangle(leftI-B,h-bottomI,B,B);
            Rectangle r8 = new Rectangle(leftI-B,topI,B,h-topI-bottomI);
            
            Point p = new Point(x,y);
            int cc = 0;
            
            if (r1.contains(p)) {
                cc = Cursor.NW_RESIZE_CURSOR; 
            } else if (r3.contains(p)) {
                cc = Cursor.NE_RESIZE_CURSOR; 
            } else if (r5.contains(p)) {
                cc = Cursor.SE_RESIZE_CURSOR; 
            } else if (r7.contains(p)) {
                cc = Cursor.SW_RESIZE_CURSOR; 
            } else if (r2.contains(p)) {
                cc = Cursor.N_RESIZE_CURSOR; 
            } else if (r4.contains(p)) {
                cc = Cursor.E_RESIZE_CURSOR; 
            } else if (r6.contains(p)) {
                cc = Cursor.S_RESIZE_CURSOR; 
            } else if (r8.contains(p)) {
                cc = Cursor.W_RESIZE_CURSOR; 
            }
            
            return cc;
        }
    }//********************************************************************* v3.2版本启用的新边框拖放核心算法 END
}
