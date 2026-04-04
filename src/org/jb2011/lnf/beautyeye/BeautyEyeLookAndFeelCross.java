/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * BeautyEyeLookAndFeelCross.java at 2015-2-1 20:25:40, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 *<p>
 * BeautyEye Swing外观实现方案 - 跨平台通用外观实现主类.<br>
 * <p>
 * 本主题主类仅供跨平台时使用，它可用于Java支持的所有操作系统.
 * 
 * @author lornwolf
 * @version 1.0
 */
public class BeautyEyeLookAndFeelCross extends MetalLookAndFeel {
    static {
        BeautyEyeLookAndFeelWin.initLookAndFeelDecorated();
    }

    /**
     * Instantiates a new beauty eye look and feel cross.
     */
    public BeautyEyeLookAndFeelCross() {
        super();

        //取消Metal LNF中默认的粗体字
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //此项如是true，则将会为TabbedPane的内容面板填充天蓝色背景
        UIManager.put("TabbedPane.contentOpaque", Boolean.FALSE);
        //此项如是true，则将会为TabbedPane的标签填充天蓝色背景
        UIManager.put("TabbedPane.tabsOpaque", Boolean.FALSE);
        BeautyEyeLNFHelper.implLNF();

        //自定义JFileChooser的L&F实现（为了解决JFileChooser中的文件查看列表的行高问题）
        org.jb2011.lnf.beautyeye.ch20_filechooser.__UI__.uiImpl_cross();
    }
    
    @Override
    public String getName() {
        return "BeautyEyeCross";
    }

    @Override
    public String getID() {
        return "BeautyEyeCross";
    }

    @Override
    public String getDescription() {
        return "BeautyEye cross-platform L&F developed by lornwolf.";
    }
    
    @Override
    public boolean getSupportsWindowDecorations() {
        return true;
    }

    @Override
    public boolean isNativeLookAndFeel() {
        return false;
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    protected void initComponentDefaults(UIDefaults table) {
        super.initComponentDefaults(table);
        initOtherResourceBundle(table);
    }

    /**
     * Initialize the defaults table with the name of the other ResourceBundle
     * used for getting localized defaults.
     */
    protected void initOtherResourceBundle(UIDefaults table) {
        table.addResourceBundle("org.jb2011.lnf.beautyeye.resources.beautyeye");
    }
}
