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

/**
 * 一个用9格图实现的边框阴影效果，目前主要用于窗口的边框（阴影效果是半透明的）。.
 *
 * @author lornwolf
 * @deprecated 此border因美感效果有些瑕疵，目前已停用。
 */
@Deprecated
public class BEShadowBorder2 extends NinePatchBorder {

    /** The Constant BOTTOM. */
    private final static int TOP = 14, LEFT = 15, RIGHT = 15, BOTTOM = 16;

    /**
     * Instantiates a new bE shadow border2.
     */
    public BEShadowBorder2() {
        super(new Insets(TOP, LEFT, BOTTOM, RIGHT)
                , org.jb2011.lnf.beautyeye.widget.__Icon9Factory__.getInstance().getBorderIcon_Shadow2());
    }
}
