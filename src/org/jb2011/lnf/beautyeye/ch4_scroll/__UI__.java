/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * __UI__.java at 2015-2-1 20:25:36, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye.ch4_scroll;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class __UI__.
 */
public class __UI__
{
    
    /**
     * Ui impl.
     */
    public static void uiImpl()
    {
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 视口的相关ui值设定
        UIManager.put("Viewport.background",new ColorUIResource(BeautyEyeLNFHelper.commonBackgroundColor));
        UIManager.put("Viewport.foreground",new ColorUIResource(BeautyEyeLNFHelper.commonForegroundColor));

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> JScrollPane的相关ui值设定
        UIManager.put("ScrollPane.border",new BorderUIResource(new org.jb2011.lnf.beautyeye.ch4_scroll.ScrollPaneBorder()));//BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        // 不能设置alpha通道小于255的透明颜色，否则会出现无法重paint的问题
        UIManager.put("ScrollPane.background",new ColorUIResource(Color.white));//cc));
        UIManager.put("ScrollPane.foreground",new ColorUIResource(BeautyEyeLNFHelper.commonForegroundColor));
        UIManager.put("ScrollPaneUI",org.jb2011.lnf.beautyeye.ch4_scroll.BEScrollPaneUI.class.getName());

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> JScrollPane的滚动条相关ui值设定
        UIManager.put("ScrollBar.thumb",new ColorUIResource(BeautyEyeLNFHelper.commonBackgroundColor));
        UIManager.put("ScrollBar.foreground",new ColorUIResource(BeautyEyeLNFHelper.commonForegroundColor));
        UIManager.put("ScrollBar.background",new ColorUIResource(new Color(250,250,250)));
        UIManager.put("ScrollBar.trackForeground",new ColorUIResource(new Color(250,250,250)));
        UIManager.put("scrollbar",new ColorUIResource(new Color(250,250,250)));
        UIManager.put("ScrollBarUI",org.jb2011.lnf.beautyeye.ch4_scroll.BEScrollBarUI.class.getName());
    }
}
