/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * ScrollPaneBorder.java at 2015-2-1 20:25:41, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye.ch4_scroll;

import java.awt.Insets;

import org.jb2011.lnf.beautyeye.widget.border.NinePatchBorder;

// TODO: Auto-generated Javadoc
/**
 * 滚动面板默认Border的实现类。.
 *
 * @author lornwolf
 */
public class ScrollPaneBorder extends NinePatchBorder
{
    
    /**
     * Instantiates a new scroll pane border.
     */
    public ScrollPaneBorder()
    {
        super(new Insets(6,6,8,6)//5,4,6,4
                , __Icon9Factory__.getInstance().getScrollPaneBorderBg());
    }
}