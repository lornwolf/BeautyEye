/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * BETreeUI.java at 2015-2-1 20:25:36, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye.ch16_tree;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 * JTree的UI实现类。.
 *
 * @author lornwolf
 * @version 1.0
 */
//本类的实现参考了WindowsTreeUI
//目前，本类中暂未对UI方面代码进行修改，对Tree的UI修改主要是基于UIManager对应Tree
//属性的配置，目前已经足够达到预期效果，如有必要可开放本类中的代码进行深入修改。
public class BETreeUI extends BasicTreeUI {

    /**
     * Creates the ui.
     *
     * @param c the c
     * @return the component ui
     */
    public static ComponentUI createUI(JComponent c) {
        return new BETreeUI();
    }

    //copy from WindowsTreeUI
    /**
     * Returns the default cell renderer that is used to do the
     * stamping of each node.
     *
     * @return the tree cell renderer
     */
    protected TreeCellRenderer createDefaultCellRenderer() {
        return new WindowsTreeCellRenderer();
    }

    //copy from WindowsTreeUI
    /**
     * The Class WindowsTreeCellRenderer.
     */
    public class WindowsTreeCellRenderer extends DefaultTreeCellRenderer {
        //目前没有定制内容，本来想让render绘制成圆角，但尝试后发现DefaultTreeCellRenderer类里
        //的代码设计欠佳，很难继承，要改的代码非常多，干脆作罢
    }
}
