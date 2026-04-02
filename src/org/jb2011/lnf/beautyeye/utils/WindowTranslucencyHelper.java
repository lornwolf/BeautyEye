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

/**
 * The Class WindowTranslucencyHelper.
 */
public class WindowTranslucencyHelper {
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
	public static boolean isTranslucencySupported() {
		try {
			// Java 7+ standard API (com.sun.awt.AWTUtilities removed in Java 9)
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gd = ge.getDefaultScreenDevice();
			return gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);
		} catch (Exception e) {
			if (BeautyEyeLNFHelper.debug)
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
	public static void setOpacity(Window w, float opacity) {
		try {
			if (!isTranslucencySupported()) {
				LogHelper.debug("Your OS can't supported translucency.");
				return;
			}
			// Java 7+ standard API: Window.setOpacity(float)
			w.setOpacity(opacity);
		} catch (Exception e) {
			if (BeautyEyeLNFHelper.debug)
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
	public static void setWindowOpaque(Window w, boolean opaque) {
		try {
			if (!isTranslucencySupported()) {
				LogHelper.debug("Your OS can't supported translucency.");
				return;
			}
			// Java 7+ standard API: set background with alpha to control opacity
			Color bgc = w.getBackground();
			if (bgc == null)
				bgc = Color.black;
			Color newBgn = new Color(bgc.getRed(), bgc.getGreen(), bgc.getBlue(), opaque ? 255 : 0);
			w.setBackground(newBgn);
		} catch (Exception e) {
			if (BeautyEyeLNFHelper.debug)
				e.printStackTrace();
			LogHelper.debug("您的JRE版本不支持窗口透明，BeautyEye外观将不能达到最佳视觉效果哦."+e.getMessage());
		}
	}
}
