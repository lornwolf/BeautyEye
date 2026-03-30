/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project.
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 *
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * BeautyEyeLookAndFeelWin.java at 2015-2-1 20:25:39, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.FrameBorderStyle;
import org.jb2011.lnf.beautyeye.winlnfutils.WinUtils;

/**
 * <p>
 * BeautyEye Swing外观实现方案 - Windows平台专用外观实现主类.<br>
 * <p>
 * 本主题主类仅供Windows下使用，继承自MetalLookAndFeel（Java 9+中WindowsLookAndFeel
 * 所在包不再对外导出，故改用Metal作为基础L&F）.
 *
 * @author lornwolf
 * @version 1.1
 */
public class BeautyEyeLookAndFeelWin extends MetalLookAndFeel
{
    static{
        initLookAndFeelDecorated();
    }

    /**
     * Instantiates a new beauty eye look and feel win.
     *
     * @see BeautyEyeLNFHelper#implLNF()
     * @see org.jb2011.lnf.beautyeye.ch20_filechooser.__UI__#uiImpl_win()
     * @see #initForVista()
     */
    public BeautyEyeLookAndFeelWin()
    {
        super();

        //取消Metal LNF中默认的粗体字
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //此项如是true，则将会为TabbedPane的内容面板填充天蓝色背景
        UIManager.put("TabbedPane.contentOpaque", Boolean.FALSE);
        //此项如是true，则将会为TabbedPane的标签填充天蓝色背景
        UIManager.put("TabbedPane.tabsOpaque", Boolean.FALSE);

        BeautyEyeLNFHelper.implLNF();

        //自定义JFileChooser的L&F实现（为了解决windows LNF下文件选择框UI未实现背景填充问题）
        org.jb2011.lnf.beautyeye.ch20_filechooser.__UI__.uiImpl_win();

        //针对Vista及更新的windows平台进行特殊设置
        initForVista();
    }

    /**
     * 针对Vista及更新的windows平台进行补救性重新设置以便与BeautyEye LNF的审美进行适配.
     */
    protected void initForVista()
    {
        if(WinUtils.isOnVista())
        {
            UIManager.put("CheckBoxMenuItem.margin",new InsetsUIResource(0,0,0,0));
            UIManager.put("RadioButtonMenuItem.margin",new InsetsUIResource(0,0,0,0));
            UIManager.put("Menu.margin",new InsetsUIResource(0,0,0,0));
            UIManager.put("MenuItem.margin",new InsetsUIResource(0,0,0,0));

            UIManager.put("Menu.border",new BorderUIResource(BorderFactory.createEmptyBorder(1,3,2,3)));
            UIManager.put("MenuItem.border",new BorderUIResource(BorderFactory.createEmptyBorder(1,3,2,3)));
            UIManager.put("CheckBoxMenuItem.border",new BorderUIResource(BorderFactory.createEmptyBorder(4,3,4,3)));
            UIManager.put("RadioButtonMenuItem.border",new BorderUIResource(BorderFactory.createEmptyBorder(4,3,4,3)));

            UIManager.put("CheckBoxMenuItem.checkIcon"
                    ,new org.jb2011.lnf.beautyeye.ch9_menu.BECheckBoxMenuItemUI.CheckBoxMenuItemIcon().setUsedForVista(true));
            UIManager.put("RadioButtonMenuItem.checkIcon"
                    ,new org.jb2011.lnf.beautyeye.ch9_menu.BERadioButtonMenuItemUI.RadioButtonMenuItemIcon().setUsedForVista(true));

            //为Menu和MenuItem设置统一尺寸的checkIcon，确保Vista下子菜单目录与普通菜单项左侧对齐
            javax.swing.Icon emptyCheckIcon = new javax.swing.Icon() {
                public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {}
                public int getIconWidth() { return 16; }
                public int getIconHeight() { return 16; }
            };
            UIManager.put("Menu.checkIcon", emptyCheckIcon);
            UIManager.put("MenuItem.checkIcon", emptyCheckIcon);
        }
    }

    @Override
    public String getName()
    {
        return "BeautyEyeWin";
    }

    @Override
    public String getID()
    {
        return "BeautyEyeWin";
    }

    @Override
    public String getDescription()
    {
        return "BeautyEye windows-platform L&F developed by lornwolf.";
    }

    @Override
    public boolean getSupportsWindowDecorations()
    {
        return true;
    }

    @Override
    public boolean isNativeLookAndFeel()
    {
        return false;
    }

    @Override
    public boolean isSupportedLookAndFeel()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIDefaults getDefaults()
    {
        UIDefaults table = super.getDefaults();
        // MetalLookAndFeel.getDefaults()在super.initComponentDefaults()之后
        // 还会调用currentTheme.addCustomEntriesToTable(table)，重新用ActiveValue
        // 覆盖字体设置。因此必须在getDefaults()返回前再次替换字体，
        // 确保所有Metal主题的字体覆盖都被清除。
        overrideMetalFontsWithSystemFont(table);
        return table;
    }

    /**
     * {@inheritDoc}
     */
    protected void initComponentDefaults(UIDefaults table)
    {
        super.initComponentDefaults(table);
        initOtherResourceBundle(table);
    }

    /**
     * MetalLookAndFeel会将所有组件字体强制设为Metal主题字体（Dialog），
     * 这导致用户通过setFont()设置的字体被覆盖。此方法用Windows系统字体替换
     * Metal的默认字体，恢复原WindowsLookAndFeel的字体行为。
     */
    private void overrideMetalFontsWithSystemFont(UIDefaults table)
    {
        Font sysFont = (Font) Toolkit.getDefaultToolkit().getDesktopProperty("win.defaultGUI.font");
        if (sysFont == null)
            return;

        FontUIResource fontResource = new FontUIResource(sysFont);

        String[] fontKeys = {
            "Button.font", "CheckBox.font", "CheckBoxMenuItem.font",
            "ColorChooser.font", "ComboBox.font", "EditorPane.font",
            "FormattedTextField.font", "Label.font", "List.font",
            "Menu.font", "MenuBar.font", "MenuItem.font",
            "OptionPane.font", "Panel.font", "PasswordField.font",
            "PopupMenu.font", "ProgressBar.font", "RadioButton.font",
            "RadioButtonMenuItem.font", "ScrollPane.font", "Spinner.font",
            "TabbedPane.font", "Table.font", "TableHeader.font",
            "TextArea.font", "TextField.font", "TextPane.font",
            "TitledBorder.font", "ToggleButton.font", "ToolBar.font",
            "ToolTip.font", "Tree.font", "Viewport.font",
            "InternalFrame.titleFont"
        };

        for (String key : fontKeys)
        {
            table.put(key, fontResource);
        }
    }

    /**
     * Initialize the defaults table with the name of the other ResourceBundle
     * used for getting localized defaults.
     */
    protected void initOtherResourceBundle(UIDefaults table)
    {
        table.addResourceBundle( "org.jb2011.lnf.beautyeye.resources.beautyeye" );
    }

    /**
     * 据BeautyEyeLNFHelper.frameBorderStyle指明的窗口边框类型来
     * 决定是否使用操作系统相关的窗口装饰样式.
     */
    static void initLookAndFeelDecorated()
    {
        if(BeautyEyeLNFHelper.frameBorderStyle == FrameBorderStyle.osLookAndFeelDecorated)
        {
            JFrame.setDefaultLookAndFeelDecorated(false);
            JDialog.setDefaultLookAndFeelDecorated(false);
        }
        else
        {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }
    }
}
