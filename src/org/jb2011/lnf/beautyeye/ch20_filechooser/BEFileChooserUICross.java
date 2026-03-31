/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * BEFileChooserUICross.java at 2015-2-1 20:25:39, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye.ch20_filechooser;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalFileChooserUI;

/**
 * BeautyEye L&F implementation of a FileChooser.
 * <p>
 * 目前属通用跨平台专用UI实现类.
 *
 * @author lornwolf, 2012-09-17
 * @version 1.0
 */
public class BEFileChooserUICross extends MetalFileChooserUI {

    /**
     * Instantiates a new bE file chooser ui cross.
     *
     * @param filechooser the filechooser
     */
    public BEFileChooserUICross(JFileChooser filechooser) {
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
    public static ComponentUI createUI(JComponent c) {
        return new BEFileChooserUICross((JFileChooser) c);
    }

    
    //* modified by lornwolf 2012-09-17
    /**
     * 重写父类方法，以实现对文件查看列表的额外设置.
     * <p>
     * 为什么要重写此方法，没有更好的方法吗？<br>
     * 答：因父类的封装结构不佳，filePane是private私有，子类中无法直接引用，
     * 要想对filePane中的文列表额外设置，目前重写本方法是个没有办法的方法.
     * <p>
     * sun.swing.FilePane源码可查看地址：<a href="http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/sun/swing/FilePane.java">Click here.</a>
     *
     * @param fc the fc
     * @return the j panel
     */
    protected JPanel createList(JFileChooser fc) {
        JPanel p = super.createList(fc);
        
        //* 以下代码的作用就是将文件列表JList对象引用给找回来（通过从它的父面板中层层向下搜索）
        //* ，因无法从父类中直接获得列表对象的直接引用，只能用此笨办法了
        if (p.getComponentCount() > 0) {
            Component scollPane = p.getComponent(0);
            if (scollPane != null && scollPane instanceof JScrollPane) {
                JViewport vp = ((JScrollPane)scollPane).getViewport();
                if (vp != null) {
                    Component fileListView = vp.getView();
                    //终于找到了文件列表的实例引用
                    if (fileListView != null && fileListView instanceof JList) {
                        //把列表的行高改成-1（即自动计算列表每个单元的行高而不指定固定值）
                        //* 说明：在BeautyEye LNF中，为了便JList的UI更好看，在没有其它方法有前
                        //* 提下就在JList的BEListUI中给它设置了默写行高32，而JFildChooser中的
                        //* 文件列表将会因此而使得单元行高很大——从而导致文件列表很难看，此处就是恢复
                        //* 文件列表单元行高的自动计算，而非指定固定行高。
                        //*
                        //* 说明2：为什么不能利用list.getClientProperty("List.isFileList")从而在JList
                        //* 的ui中进行判断并区别对待是否是文件列表呢？
                        //* 答：因为"List.isFileList"是在BasicFileChooserUI中设置的，也就是说当为个属性被
                        //* 设置的时候JFileChooser中的文件列表已经实例化完成（包括它的ui初始化），所以此时
                        //* 如果在JList的ui中想区分是不可能的，因它还没有被调置，这个设置主要是供BasicListUI
                        //* 在被实例化完成后，来异步处理这个属性的（通过监听属性改变事件来实现的）
                        ((JList)fileListView).setFixedCellHeight(-1);
                    }
                }
            }
        }
        
        return p;
    }
}
