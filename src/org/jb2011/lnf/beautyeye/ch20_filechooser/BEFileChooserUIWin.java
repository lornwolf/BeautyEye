/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project.
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 *
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * BEFileChooserUIWin.java at 2015-2-1 20:25:37, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */

package org.jb2011.lnf.beautyeye.ch20_filechooser;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalFileChooserUI;

/**
 * BeautyEye L&F implementation of a FileChooser for Windows platform.
 * <p>
 * Java 9+: formerly extended WindowsFileChooserUI (com.sun.java.swing.plaf.windows),
 * now extends MetalFileChooserUI to avoid dependency on unexported internal package.
 *
 * @author lornwolf, 2012-09-09
 * @version 1.1
 */
public class BEFileChooserUIWin extends MetalFileChooserUI
{

    /**
     * Instantiates a new BEFileChooserUIWin.
     *
     * @param filechooser the filechooser
     */
    public BEFileChooserUIWin(JFileChooser filechooser)
    {
        super(filechooser);
    }

    //
    // ComponentUI Interface Implementation methods
    //
    /**
     * Creates the ui.
     *
     * @param c the c
     * @return the component ui
     */
    public static ComponentUI createUI(JComponent c)
    {
        return new BEFileChooserUIWin((JFileChooser) c);
    }

    //* 注：本方法由lornwolf实现，没有以下默认背景绘制则在BE LNF中因透明窗口而使得
    //* 文件选择框内容面板的空白处出现全透明现的丑陋现象
    @Override
    public void paint( Graphics g, JComponent c )
    {
        g.setColor(c.getBackground());
        g.fillRect(0, 0, c.getWidth(), c.getHeight());
    }

    //* modified by lornwolf 2012-09-17
    /**
     * 重写父类方法，以实现对文件查看列表的额外设置.
     * <p>
     * 为什么要重写此方法：因父类的封装结构不佳，filePane是private私有，子类中无法直接引用，
     * 要想对filePane中的文列表额外设置，目前重写本方法是个没有办法的方法.
     *
     * @param fc the fc
     * @return the j panel
     */
    protected JPanel createList(JFileChooser fc)
    {
        JPanel p = super.createList(fc);

        if(p.getComponentCount() > 0)
        {
            Component scollPane = p.getComponent(0);
            if(scollPane != null && scollPane instanceof JScrollPane)
            {
                JViewport vp = ((JScrollPane)scollPane).getViewport();
                if(vp != null)
                {
                    Component fileListView = vp.getView();
                    if(fileListView != null && fileListView instanceof JList)
                    {
                        //把列表的行高改成-1（即自动计算列表每个单元的行高而不指定固定值）
                        ((JList)fileListView).setFixedCellHeight(-1);
                    }
                }
            }
        }

        return p;
    }
}
