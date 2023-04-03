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
import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JRootPane;

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
        boolean isTranslucencySupported = false;
        try
        {
            //1.7.0及以后版本
            if(JVM.current().isOrLater(JVM.JDK1_7))
            {
                GraphicsEnvironment ge = 
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gd = ge.getDefaultScreenDevice();
                //
                Class _WindowTranslucency = Class.forName("java.awt.GraphicsDevice$WindowTranslucency");
                isTranslucencySupported = ((Boolean)(ReflectHelper.invokeMethod(GraphicsDevice.class, gd, "isWindowTranslucencySupported"
                        , new Class[]{_WindowTranslucency}
                        , new Object[]{Enum.valueOf(_WindowTranslucency, "TRANSLUCENT")}))).booleanValue();
            }
            else if(JVM.current().isOrLater(JVM.JDK1_6))
            {
                //* Implemention at JDK1.6_u10 and after in reflect 
                Class _WindowTranslucency = Class.forName("com.sun.awt.AWTUtilities$Translucency");
                isTranslucencySupported = ((Boolean)(ReflectHelper.invokeMethod(Class.forName("com.sun.awt.AWTUtilities"), _WindowTranslucency, "isTranslucencySupported"
                        , new Class[]{_WindowTranslucency}
                        , new Object[]{Enum.valueOf(_WindowTranslucency, "TRANSLUCENT")}))).booleanValue();
            }
        }
        catch (Exception e)
        {
            if(BeautyEyeLNFHelper.debug)
                e.printStackTrace();
            LogHelper.debug("Exception at WindowTranslucencyHelper.isTranslucencySupported(),"+e.getMessage());
        }

        return isTranslucencySupported;
    }

    /**
     * Sets the opacity.
     *
     * @param w the w
     * @param opacity the opacity
     */
    public static void setOpacity(Window w, float opacity)
    {
        try
        {
            //1.6.0_u12及以后版本
            if(JVM.current().isOneDotSixUpdate12OrAfter())//JDK1_6_U10//为什么要到u12才支持？因u10里的窗口透明存在BUG 6750920
            {
                if(!isTranslucencySupported())
                {
                    LogHelper.debug("Your OS can't supported translucency.");
                    return;
                }
                //1.7.0及以后版本
                if(JVM.current().isOrLater(JVM.JDK1_7))
                    ReflectHelper.invokeMethod(Window.class, w, "setOpacity"
                            , new Class[]{float.class}, new Object[]{opacity});
                else
                    ReflectHelper.invokeStaticMethod("com.sun.awt.AWTUtilities", "setWindowOpacity"
                            , new Class[]{Window.class, float.class}, new Object[]{w, opacity});
            }
            else
                LogHelper.debug("您的JRE版本不支持每像素半透明(需jre1.6_u12及以上版本)，BeautyEye外观将不能达到最佳视觉效果哦。");
        }
        catch (Exception e)
        {
            if(BeautyEyeLNFHelper.debug)
                e.printStackTrace();
            LogHelper.debug("您的JRE版本不支持每像素半透明(需jre1.6_u12及以上版本)，BeautyEye外观将不能达到最佳视觉效果哦。" + e.getMessage());
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
        if (w.getClass().getName().equals("com.lornwolf.onebar.plugins.webapps.MiniBrowser")
            || w.getClass().getName().equals("com.lornwolf.onebar.plugins.webapps.MemoBrowser")) {
            return;
        }
        try
        {
            //1.6.0_u12及以后版本
            if(JVM.current().isOneDotSixUpdate12OrAfter())//JDK1_6_U10//为什么要到u12才支持？因u10里的窗口透明存在BUG 6750920
            {
                if(!isTranslucencySupported())
                {
                    LogHelper.debug("Your OS can't supported translucency.");
                    return;
                }
                //1.7.0及以后版本
                if(JVM.current().isOrLater(JVM.JDK1_7))
                {
                    Color bgc = w.getBackground();
                    //* 2012-09-22 由lornwolf注释：在群友机器上（win7+java1.7.0.1）的生产系统下
                    //* 下使用BeautyEye有时w.getBackground()返回值是null，但为什么返回是null，Jack 没
                    //* 有测出来（Jack测试都是正常的），暂且认为是其系统代码有问题吧，在此容错一下
                    if(bgc == null)
                        bgc = Color.black;//暂不知道用此黑色作为容错值合不合适
                    Color newBgn = new Color(bgc.getRed(),bgc.getGreen(), bgc.getBlue(), opaque?255:0);
                    w.setBackground(newBgn);
                }
                else
                    ReflectHelper.invokeStaticMethod("com.sun.awt.AWTUtilities", "setWindowOpaque"
                            , new Class[]{Window.class, boolean.class}, new Object[]{w, opaque});
            }
            else
                LogHelper.debug("您的JRE版本不支持窗口透明(需jre1.6_u12及以上版本)，BeautyEye外观将不能达到最佳视觉效果哦.");
        }
        catch (Exception e)
        {
            if(BeautyEyeLNFHelper.debug)
                e.printStackTrace();
            LogHelper.debug("您的JRE版本不支持窗口透明(需jre1.6_u12及以上版本)，BeautyEye外观将不能达到最佳视觉效果哦."+e.getMessage());
        }
    }
}
