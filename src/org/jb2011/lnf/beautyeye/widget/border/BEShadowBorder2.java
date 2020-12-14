/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * BEShadowBorder2.java at 2015-2-1 20:25:37, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye.widget.border;

import java.awt.Insets;

// TODO: Auto-generated Javadoc
/**
 * 一个用9格图实现的边框阴影效果，目前主要用于窗口的边框（阴影效果是半透明的）。.
 *
 * @author lornwolf
 * @deprecated 此border因美感效果有些瑕疵，目前已停用。
 */
public class BEShadowBorder2 extends NinePatchBorder//AbstractBorder
{
    
    /** The Constant BOTTOM. */
    private final static int TOP = 14,LEFT = 15,RIGHT = 15,BOTTOM = 16;
    
    /**
     * Instantiates a new bE shadow border2.
     */
    public BEShadowBorder2()
    {
        super(new Insets(TOP, LEFT, BOTTOM, RIGHT)
        , org.jb2011.lnf.beautyeye.widget.__Icon9Factory__.getInstance().getBorderIcon_Shadow2()); 
    }
    
    //* 2012-09-19 在BeautyEye v3.2中的BERootPaneUI，lornwolf启用了相比
    //* 原MetalRootPaneUI中更精确更好的边框拖放算法，以下方法暂时弃用，以后可以删除了！
//    //当用本border作边框时，窗口可拖动敏感触点区大小值
//    public static int BORDER_DRAG_THICKNESS()
//    {
//        return Math.min(Math.min(Math.min(TOP, LEFT),RIGHT),BOTTOM);
//    }
//    
//    //当用本border作边框时，窗口边角可拖动敏感触点区大小值
//    public static int CORNER_DRAG_WIDTH()
//    {
//        return Math.max(Math.max(Math.max(TOP, LEFT),RIGHT),BOTTOM);
//    }
}
