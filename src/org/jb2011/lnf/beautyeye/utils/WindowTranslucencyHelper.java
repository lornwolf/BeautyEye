/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * WindowTranslucencyHelper.java at 2015-2-1 20:25:40, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */
package org.jb2011.lnf.beautyeye.utils;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

//* 关于java支持窗口透明的详细信息请见：http://docs.oracle.com/javase/tutorial/uiswing/misc/trans_shaped_windows.html#uniform

//* 关于java1.6.0_10里的窗口透明存在一个BUG：
//* BUG出的错误：Exception in thread "AWT-EventQueue-0" java.lang.IllegalArgumentException: Width (0) and height (0) cannot be <= 0
//* 官方BUG ID ：6750920，地址：http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6750920
//* 该BUG被解决于:java1.6.0_12，realease note地址：http://www.oracle.com/technetwork/java/javase/6u12-137788.html
/**
 * The Class WindowTranslucencyHelper.
 */
public class WindowTranslucencyHelper
{
    private final static String UN_WINDOWS_SORRY = "I'm sorry, the Linux platform does not support transparency" +
            ", please pay attention to the next version of BeautyEye.";
//    public static boolean isWindowTranslucencySupported(Class cTranslucency, Object param)
//    {
//        try
//        {
//            if(JVM.current().isOneDotSixUpdate12OrAfter())
//            {
//                if(JVM.current().isOrLater(JVM.JDK1_7))
//                {
//                    Method m = GraphicsDevice.class.getMethod("isWindowTranslucencySupported", cTranslucency);
//                    boolean is = (Boolean)(m.invoke(null, param));
//                    return is;
//                }
//                else
//                {
//                    Class c = Class.forName("com.sun.awt.AWTUtilities");
//                    Method m = c.getMethod("isTranslucencySupported", cTranslucency);
//                    boolean is = (Boolean)(m.invoke(null, param));
//                    return is;
//                }
//            }
//            else
//            {
//                return false;
//            }
//        }
//        catch (Exception e)
//        {
//            System.err.println("Exception at method of WindowT" +
//                    "ranslucencyHelper.isWindowTranslucencySupported, "+e.getMessage());
//            return false;
//        }
//    }
    
    /**
     * Checks if is translucency supported.
     *
     * @return true, if is translucency supported
     * @see <code>GraphicsDevice.isWindowTranslucencySupported(TRANSLUCENT)</code> at JDK1.7 or later
     * @see <code>com.sun.awt.AWTUtilities.isTranslucencySupported(AWTUtilities.Translucency.TRANSLUCENT)</code> at JDK1.6_u10 or later
     * @author lornwolf at 2013-03-20 19:00
     * @since 3.5
     */
    public static boolean isTranslucencySupported()
    {
        try
        {
            // Java 7+ standard API (com.sun.awt.AWTUtilities removed in Java 9)
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            return gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);
        }
        catch (Exception e)
        {
            if(BeautyEyeLNFHelper.debug)
                e.printStackTrace();
            LogHelper.debug("Exception at WindowTranslucencyHelper.isTranslucencySupported(),"+e.getMessage());
        }
        return false;
    }
    
    /**
     * Sets the opacity.
     *
     * @param w the w
     * @param opacity the opacity
     */
    public static void setOpacity(Window w, float opacity)
    {
//        //## Fix for: Issue BELNF-5, 目前因lornwolf手头没有Linux等测试环境，目前就暂时先让这
//        //## 些平台不支持窗口透明吧，起码先把BE LNF跑起来再说，此问题以后再彻底解决
//        if(!Platform.isWindows())
//        {
//            System.out.println(UN_WINDOWS_SORRY);
//            return;
//        }
        
        try
        {
            if(!isTranslucencySupported())
            {
                LogHelper.debug("Your OS can't supported translucency.");
                return;
            }
            // Java 7+ standard API: Window.setOpacity(float)
            w.setOpacity(opacity);
        }
        catch (Exception e)
        {
            if(BeautyEyeLNFHelper.debug)
                e.printStackTrace();
            LogHelper.debug("您的JRE版本不支持每像素半透明，BeautyEye外观将不能达到最佳视觉效果哦."+e.getMessage());
        }
    }
    
    /**
     * Sets the window opaque.
     *
     * @param w the w
     * @param opaque the opaque
     */
    public static void setWindowOpaque(Window w, boolean opaque)
    {
//        //## Fix for: Issue BELNF-5, 目前因lornwolf手头没有Linux等测试环境，目前就暂时先让这
//        //## 些平台不支持窗口透明吧，起码先把BE LNF跑起来再说，此问题以后再彻底解决
//        if(!Platform.isWindows())
//        {
//            System.out.println(UN_WINDOWS_SORRY);
//            return;
//        }
        
        try
        {
            if(!isTranslucencySupported())
            {
                LogHelper.debug("Your OS can't supported translucency.");
                return;
            }
            // Java 7+ standard API: set background with alpha to control opacity
            Color bgc = w.getBackground();
            if(bgc == null)
                bgc = Color.black;
            Color newBgn = new Color(bgc.getRed(), bgc.getGreen(), bgc.getBlue(), opaque ? 255 : 0);
            w.setBackground(newBgn);
        }
        catch (Exception e)
        {
            if(BeautyEyeLNFHelper.debug)
                e.printStackTrace();
            LogHelper.debug("您的JRE版本不支持窗口透明，BeautyEye外观将不能达到最佳视觉效果哦."+e.getMessage());
        }
    }
    
//    public static void main(String[] args)
//    {
//        System.out.println(WindowTranslucencyHelper.isTranslucencySupported());
//    }
}
