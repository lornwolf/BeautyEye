/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * BEShadowBorder.java at 2015-2-1 20:25:39, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye.widget.border;

import java.awt.Insets;

/**
 * 一个用9格图实现的边框阴影效果，目前用于内部窗口的边框（阴影效果是半透明的）。.
 *
 * @author lornwolf
 * @see org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.FrameBorderStyle#translucencySmallShadow
 */
public class BEShadowBorder extends NinePatchBorder {

    /** The Constant BOTTOM. */
    private final static int TOP = 5, LEFT = 5, RIGHT = 5, BOTTOM = 6;

    /**
     * Instantiates a new bE shadow border.
     */
    public BEShadowBorder() {
        super(new Insets(TOP, LEFT, BOTTOM, RIGHT)
                , org.jb2011.lnf.beautyeye.widget.__Icon9Factory__.getInstance().getBorderIcon_Shadow1());
    }
}
