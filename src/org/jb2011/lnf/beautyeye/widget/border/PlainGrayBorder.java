/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * PlainGrayBorder.java at 2015-2-1 20:25:38, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye.widget.border;

import java.awt.Insets;

import org.jb2011.lnf.beautyeye.widget.__Icon9Factory__;

/**
 * 一个NinePatch图实现的不透明边框border.
 * <p>
 * 目前主要用于jdk1.5及以下版本的窗口边框（因为该版本下java不支持窗口透明）.
 * 
 * @author lornwolf, 2012-09-04
 * @version 1.0
 * @see org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.FrameBorderStyle#generalNoTranslucencyShadow
 */
public class PlainGrayBorder extends NinePatchBorder {

    /** The Constant IS. */
    private final static int IS = 5;

    /**
     * Instantiates a new plain gray border.
     */
    public PlainGrayBorder() {
        super(new Insets(IS, IS, IS, IS)
                , __Icon9Factory__.getInstance().getBorderIcon_plainGray());
    }
}
