/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * BEInternalFrameTitlePane.java at 2015-2-1 20:25:36, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye.ch10_internalframe;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jb2011.lnf.beautyeye.ch1_titlepane.BETitlePane;
import org.jb2011.lnf.beautyeye.utils.MySwingUtilities2;
import org.jb2011.lnf.beautyeye.winlnfutils.WinUtils;

/**
 * 内部窗体的标题栏UI实现.
 * 
 * @author lornwolf
 */
//BeautyEye外观实现中取消了isPalette的所有特殊处理，isPalette及相关属性在
//该外观中将失去意义，请注意！
//虽然beautyEye是参考自MetalLookAndFeel，但因beautyEye使用了Insets很大的立体边框，
//则如果还要像MetalLookAndFeel实现Palette类型的JInternalFrame则效果会很难看，干脆就就像
//WindowsLookAndFeel一样，不去理会什么Palette，在当前的L&F下没有任何减分。
public class BEInternalFrameTitlePane extends BasicInternalFrameTitlePane {

    /** The Constant handyEmptyBorder. */
    private static final Border handyEmptyBorder = new EmptyBorder(0, 0, 0, 0);

    /**
     * Key used to lookup Color from UIManager. If this is null,
     * <code>getWindowTitleBackground</code> is used.
     */
    private String selectedBackgroundKey;
    /**
     * Key used to lookup Color from UIManager. If this is null,
     * <code>getWindowTitleForeground</code> is used.
     */
    private String selectedForegroundKey;
    /**
     * Key used to lookup shadow color from UIManager. If this is null,
     * <code>getPrimaryControlDarkShadow</code> is used.
     */
    private String selectedShadowKey;
    /**
     * Boolean indicating the state of the <code>JInternalFrame</code>s
     * closable property at <code>updateUI</code> time.
     */
    private boolean wasClosable;

    /** The buttons width. */
    int buttonsWidth = 0;

    /**
     * Instantiates a new bE internal frame title pane.
     *
     * @param f the f
     */
    public BEInternalFrameTitlePane(JInternalFrame f) {
        super(f);
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#addNotify()
     */
    public void addNotify() {
        super.addNotify();
        // This is done here instead of in installDefaults as I was worried
        // that the BasicInternalFrameUI might not be fully initialized, and
        // that if this resets the closable state the BasicInternalFrameUI
        // Listeners that get notified might be in an odd/uninitialized state.
        updateOptionPaneState();
    }

    /* (non-Javadoc)
     * @see javax.swing.plaf.basic.BasicInternalFrameTitlePane#installDefaults()
     */
    protected void installDefaults() {
        super.installDefaults();
        setFont(UIManager.getFont("InternalFrame.titleFont"));
        wasClosable = frame.isClosable();
        selectedForegroundKey = selectedBackgroundKey = null;
        if (true) {
            setOpaque(false);
        }
    }


    /* (non-Javadoc)
     * @see javax.swing.plaf.basic.BasicInternalFrameTitlePane#uninstallDefaults()
     */
    protected void uninstallDefaults() {
        super.uninstallDefaults();
        if (wasClosable != frame.isClosable()) {
            frame.setClosable(wasClosable);
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.plaf.basic.BasicInternalFrameTitlePane#createButtons()
     */
    protected void createButtons() {
        super.createButtons();

        Boolean paintActive = frame.isSelected() ? Boolean.TRUE : Boolean.FALSE;
        iconButton.putClientProperty("paintActive", paintActive);
        iconButton.setBorder(handyEmptyBorder);

        maxButton.putClientProperty("paintActive", paintActive);
        maxButton.setBorder(handyEmptyBorder);

        closeButton.putClientProperty("paintActive", paintActive);
        closeButton.setBorder(handyEmptyBorder);
        
        // The palette close icon isn't opaque while the regular close icon is.
        // This makes sure palette close buttons have the right background.
        closeButton.setBackground(MetalLookAndFeel.getPrimaryControlShadow());

        if (true) {
            iconButton.setContentAreaFilled(false);
            maxButton.setContentAreaFilled(false);
            closeButton.setContentAreaFilled(false);
        }
    }
    
    /**
     * Override the parent's method to do nothing. Metal frames do not 
     * have system menus.
     */
    protected void assembleSystemMenu() {
    }

    /**
     * Override the parent's method to do nothing. Metal frames do not
     * have system menus.
     *
     * @param systemMenu the system menu
     */
    protected void addSystemMenuItems(JMenu systemMenu) {
    }


    /**
     * Override the parent's method to do nothing. Metal frames do not 
     * have system menus.
     */
    protected void showSystemMenu() {
    }

    /**
     * Override the parent's method avoid creating a menu bar. Metal frames
     * do not have system menus.
     */
    protected void addSubComponents() {
        add(iconButton);
        add(maxButton);
        add(closeButton);
    }

    /* (non-Javadoc)
     * @see javax.swing.plaf.basic.BasicInternalFrameTitlePane#createPropertyChangeListener()
     */
    protected PropertyChangeListener createPropertyChangeListener() {
        return new MetalPropertyChangeHandler();
    }

    /* (non-Javadoc)
     * @see javax.swing.plaf.basic.BasicInternalFrameTitlePane#createLayout()
     */
    protected LayoutManager createLayout() {
        return new XMetalTitlePaneLayout();
    }

    /**
     * The Class MetalPropertyChangeHandler.
     */
    class MetalPropertyChangeHandler extends
            BasicInternalFrameTitlePane.PropertyChangeHandler {

        /* (non-Javadoc)
         * @see javax.swing.plaf.basic.BasicInternalFrameTitlePane.PropertyChangeHandler#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent evt) {
            String prop = (String) evt.getPropertyName();
            if (prop.equals(JInternalFrame.IS_SELECTED_PROPERTY)) {
                Boolean b = (Boolean) evt.getNewValue();
                iconButton.putClientProperty("paintActive", b);
                closeButton.putClientProperty("paintActive", b);
                maxButton.putClientProperty("paintActive", b);
            } else if ("JInternalFrame.messageType".equals(prop)) {
                updateOptionPaneState();
                frame.repaint();
            }
            super.propertyChange(evt);    
        }
    }


    /**
     * The Class XZCMetalTitlePaneLayout.
     */
    class XMetalTitlePaneLayout extends TitlePaneLayout {

        /* (non-Javadoc)
         * @see javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout#addLayoutComponent(java.lang.String, java.awt.Component)
         */
        public void addLayoutComponent(String name, Component c) {
        }

        /* (non-Javadoc)
         * @see javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout#removeLayoutComponent(java.awt.Component)
         */
        public void removeLayoutComponent(Component c) {
        }

        /* (non-Javadoc)
         * @see javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout#preferredLayoutSize(java.awt.Container)
         */
        public Dimension preferredLayoutSize(Container c) {
            return minimumLayoutSize(c);
        }

        /* (non-Javadoc)
         * @see javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout#minimumLayoutSize(java.awt.Container)
         */
        public Dimension minimumLayoutSize(Container c) {
            // Compute width.
            int width = 30;
            if (frame.isClosable()) {
                width += 21;
            }
            if (frame.isMaximizable()) {
                width += 16 + (frame.isClosable() ? 10 : 4);
            }
            if (frame.isIconifiable()) {
                width += 16 + (frame.isMaximizable() ? 2
                        : (frame.isClosable() ? 10 : 4));
            }
            FontMetrics fm = frame.getFontMetrics(getFont());
            String frameTitle = frame.getTitle();
            int title_w = frameTitle != null ? MySwingUtilities2.stringWidth(
                    frame, fm, frameTitle) : 0;
            int title_length = frameTitle != null ? frameTitle.length() : 0;

            if (title_length > 2) {
                int subtitle_w = MySwingUtilities2.stringWidth(frame, fm, frame
                        .getTitle().substring(0, 2)
                        + "...");
                width += (title_w < subtitle_w) ? title_w : subtitle_w;
            } else {
                width += title_w;
            }


            // Compute height.
            int height = 0;
            {
                int fontHeight = fm.getHeight();
                fontHeight += 4;//默认是 +=7 
                Icon icon = frame.getFrameIcon();
                int iconHeight = 0;
                if (icon != null) {
                    // SystemMenuBar forces the icon to be 16x16 or less.
                    iconHeight = Math.min(icon.getIconHeight(), 16);
                }
                iconHeight += 5;
                height = Math.max(fontHeight, iconHeight) + 5;//默认是 +0，modified by jb2011 2012-06-18
            }

            return new Dimension(width, height);
        }

        /* (non-Javadoc)
         * @see javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout#layoutContainer(java.awt.Container)
         */
        public void layoutContainer(Container c) {
            boolean leftToRight = WinUtils.isLeftToRight(frame);

            int w = getWidth();
            int x = leftToRight ? w : 0;
            int y = 5;//默认是0，modified by jb2011
            int spacing;

            // assumes all buttons have the same dimensions
            // these dimensions include the borders
            int buttonHeight = closeButton.getIcon().getIconHeight();
            int buttonWidth = closeButton.getIcon().getIconWidth();

            if (frame.isClosable()) {
                {
                    spacing = 4;
                    x += leftToRight ? -spacing - buttonWidth : spacing;
                    closeButton.setBounds(x, y, buttonWidth, buttonHeight);
                    if (!leftToRight)
                        x += buttonWidth;
                }
            }

            if (frame.isMaximizable()) {
                spacing = frame.isClosable() ? 2 : 4;
                x += leftToRight ? -spacing - buttonWidth : spacing;
                maxButton.setBounds(x, y, buttonWidth, buttonHeight);
                if (!leftToRight)
                    x += buttonWidth;
            }


            if (frame.isIconifiable()) {
                spacing = frame.isMaximizable() ? 2 : (frame.isClosable() ? 10
                        : 4);
                x += leftToRight ? -spacing - buttonWidth : spacing;
                iconButton.setBounds(x, y, buttonWidth, buttonHeight);
                if (!leftToRight)
                    x += buttonWidth;
            }

            buttonsWidth = leftToRight ? w - x : x;
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.plaf.basic.BasicInternalFrameTitlePane#paintComponent(java.awt.Graphics)
     */
    public void paintComponent(Graphics g) {
        boolean leftToRight = WinUtils.isLeftToRight(frame);
        boolean isSelected = frame.isSelected();//! 选中与否的判断方式，参见border部分的注释

        int width = getWidth();
        int height = getHeight();
        
        Color background = null;
        Color foreground = null;
        Color shadow = null;
        
        if (isSelected) {
            if (selectedBackgroundKey != null) {
                background = UIManager.getColor(selectedBackgroundKey);
            }
            if (background == null) {
                background = UIManager.getColor("activeCaption");
            }
            if (selectedForegroundKey != null) {
                foreground = UIManager.getColor(selectedForegroundKey);
            }
            if (selectedShadowKey != null) {
                shadow = UIManager.getColor(selectedShadowKey);
            }
            if (shadow == null) {
                shadow = UIManager.getColor("activeCaptionBorder");
            }
            if (foreground == null) {
                foreground = UIManager.getColor("activeCaptionText");
            }
        } else {
            if (!true) {
                closeButton.setContentAreaFilled(false);
                maxButton.setContentAreaFilled(false);
                iconButton.setContentAreaFilled(false);
            }
            background = UIManager.getColor("inactiveCaption");
            foreground = UIManager.getColor("inactiveCaptionText");
            shadow = UIManager.getColor("inactiveCaptionBorder");
        }


        //---------------------------------------------------绘制标题栏背景
        /*>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 绘制标题栏背景START */
        /*
         * ** bug修正 by js,2009-09-30（这因是JDK1.5的Swing底层BUG）
         * 
         * BUG描述：当操作系统的主题切换时，JInternalFrame有时相同的方法this.getBounds()获得的结果是不一样的
         *             ，这种差异主要在于有时this.getBounds()获得的结果并未包括它的border，主要表现就是结果
         *             的(x,y)的坐标是未包含border的坐标，因而这种情况下它的(x=0,y=0),而正确应该
         *             (x=border.insets.left,y=border.insets.top)，如果无视此bug，则titlePane
         *             在填充时会变的丑陋。
         * 
         * 解决方法：当确知这个bug发生后，只能以人工方式弥补这种异相，如 强制修正其(x,y)的坐标，并相应地调整
         *             titlePane的填充区域。
         */
        {
            Insets frameInsets = frame.getInsets();
            //** Swing BUG：按理说，此处是不需要传入frameInstes的，因父类BasicInternalFrameTitlePane的布局
            //是存在BUG（布计算时没有把BorderInstes算入到x、y的偏移中，导致UI中paint时只能自行
            //进行增益，否则填充的图璩形肯定就因没有算上borderInstes而错位，详见父类中的
            //BasicInternalFrameTitlePane中内部类Handler的layoutContainer方法里
            //getNorthPane().setBounds(..)这一段
            paintTitlePaneImpl(frameInsets, g, width,height, isSelected);
        }
        /*>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 绘制标题栏背景END */

        //----------------------------------------------------绘制标题及图标
        int titleLength = 0;
        int xOffset = leftToRight ? 5 : width - 5;
        String frameTitle = frame.getTitle();

        Icon icon = frame.getFrameIcon();
        if (icon != null) {
            if (!leftToRight)
                xOffset -= icon.getIconWidth();
            int iconY = ((height / 2) - (icon.getIconHeight() / 2));
            icon.paintIcon(frame, g, xOffset +2, iconY+1);//默认是xOffset +0, iconY+0
            xOffset += leftToRight ? icon.getIconWidth() + 5 : -5;
        }

        if (frameTitle != null) {
            Font f = getFont();
            g.setFont(f);
            FontMetrics fm = MySwingUtilities2.getFontMetrics(frame, g, f);
            int fHeight = fm.getHeight();

            int yOffset = ((height - fm.getHeight()) / 2) + fm.getAscent();

            Rectangle rect = new Rectangle(0, 0, 0, 0);
            if (frame.isIconifiable()) {
                rect = iconButton.getBounds();
            } else if (frame.isMaximizable()) {
                rect = maxButton.getBounds();
            } else if (frame.isClosable()) {
                rect = closeButton.getBounds();
            }
            int titleW;

            if (leftToRight) {
                if (rect.x == 0) {
                    rect.x = frame.getWidth() - frame.getInsets().right - 2;
                }
                titleW = rect.x - xOffset - 4;
                frameTitle = getTitle(frameTitle, fm, titleW);
            } else {
                titleW = xOffset - rect.x - rect.width - 4;
                frameTitle = getTitle(frameTitle, fm, titleW);
                xOffset -= MySwingUtilities2.stringWidth(frame, fm, frameTitle);
            }

            titleLength = MySwingUtilities2.stringWidth(frame, fm, frameTitle);
            
            g.setColor(foreground);
            MySwingUtilities2.drawString(frame, g, frameTitle, xOffset, yOffset);
            xOffset += leftToRight ? titleLength + 5 : -5;
        }
    }
    
    /**
     * Paint title pane impl.
     *
     * @param frameInsets the frame insets
     * @param g the g
     * @param width the width
     * @param height the height
     * @param isSelected the is selected
     */
    protected void paintTitlePaneImpl(Insets frameInsets,Graphics g
            , int width,int height, boolean isSelected) {
        BETitlePane.paintTitlePane(g
                , frameInsets.left//0
                , frameInsets.top//0
                , width-frameInsets.left-frameInsets.right//-0
                , height, isSelected
                );
    }


    /**
     * Updates any state dependant upon the JInternalFrame being shown in
     * a <code>JOptionPane</code>.
     */
    private void updateOptionPaneState() {
        int type = -2;
        boolean closable = wasClosable;
        Object obj = frame.getClientProperty("JInternalFrame.messageType");

        if (obj == null) {
            // Don't change the closable state unless in an JOptionPane.
            return;
        }
        if (obj instanceof Integer) {
            type = ((Integer) obj).intValue();
        }
        switch (type) {
            case JOptionPane.ERROR_MESSAGE:
                selectedBackgroundKey = "OptionPane.errorDialog.titlePane.background";
                selectedForegroundKey = "OptionPane.errorDialog.titlePane.foreground";
                selectedShadowKey = "OptionPane.errorDialog.titlePane.shadow";
                closable = false;
                break;
            case JOptionPane.QUESTION_MESSAGE:
                selectedBackgroundKey = "OptionPane.questionDialog.titlePane.background";
                selectedForegroundKey = "OptionPane.questionDialog.titlePane.foreground";
                selectedShadowKey = "OptionPane.questionDialog.titlePane.shadow";
                closable = false;
                break;
            case JOptionPane.WARNING_MESSAGE:
                selectedBackgroundKey = "OptionPane.warningDialog.titlePane.background";
                selectedForegroundKey = "OptionPane.warningDialog.titlePane.foreground";
                selectedShadowKey = "OptionPane.warningDialog.titlePane.shadow";
                closable = false;
                break;
            case JOptionPane.INFORMATION_MESSAGE:
            case JOptionPane.PLAIN_MESSAGE:
                selectedBackgroundKey = selectedForegroundKey = selectedShadowKey = null;
                closable = false;
                break;
            default:
                selectedBackgroundKey = selectedForegroundKey = selectedShadowKey = null;
                break;
        }
        if (closable != frame.isClosable()) {
            frame.setClosable(closable);
        }
    }
    
    //改变父类的方法的可见性为public，方便外界调用，仅此而已，
    //modified by jb2011
    //@see com.sun.java.swing.plaf.windows.uninstallListeners()
    /* (non-Javadoc)
     * @see javax.swing.plaf.basic.BasicInternalFrameTitlePane#uninstallListeners()
     */
    public void uninstallListeners() {
        // Get around protected method in superclass
        super.uninstallListeners();
    }
}
