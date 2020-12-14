/*
 * Copyright (C) 2015 lornwolf The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * lornwolf PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * MySwingUtilities2.java at 2015-2-1 20:25:40, original version by lornwolf.
 * You can contact author with jb2011@163.com.
 */

package org.jb2011.lnf.beautyeye.utils;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

/**
 * 本类中的方法一一对应于SUN未公开的类SwingUtilities2中的方法。
 * <p>
 * 本类中的各方法都是自动据JVM版本不同对SwingUtilities2进行反射调用
 * 实现的，以便解决兼容性问题。.
 *
 */
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 一些说明 Start
//此类中方法一一对应于官方非公开的SwingUtilities2类中的方法,
//只因该类是非公开类，在不同版本里会被移动，甚至未来有被取消的可能哦，
//所以这了最大化兼容1.5版jdk，所以做个类通过反射来调用该jre里的SwingUtilities2实现
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 一些说明 END
public class MySwingUtilities2 {
     
     /**
      * Returns the FontMetrics for the current Font of the passed
      * in Graphics.  This method is used when a Graphics
      * is available, typically when painting.  If a Graphics is not
      * available the JComponent method of the same name should be used.
      * <p>
      * Callers should pass in a non-null JComponent, the exception
      * to this is if a JComponent is not readily available at the time of
      * painting.
      * <p>
      * This does not necessarily return the FontMetrics from the
      * Graphics.
      *
      * @param c JComponent requesting FontMetrics, may be null
      * @param g Graphics Graphics
      * @return the font metrics
      */
     public static FontMetrics getFontMetrics(JComponent c, Graphics g) {
          // return getFontMetrics(c, g, g.getFont());
          return (FontMetrics)invokeSwingUtilities2StaticMethod("getFontMetrics"
                         , new Class[]{JComponent.class, Graphics.class}
                         , new Object[]{c, g});
     }


     /**
      * Returns the FontMetrics for the specified Font.
      * This method is used when a Graphics is available, typically when
      * painting.  If a Graphics is not available the JComponent method of
      * the same name should be used.
      * <p>
      * Callers should pass in a non-null JComonent, the exception
      * to this is if a JComponent is not readily available at the time of
      * painting.
      * <p>
      * This does not necessarily return the FontMetrics from the
      * Graphics.
      *
      * @param c Graphics Graphics
      * @param g the g
      * @param font Font to get FontMetrics for
      * @return the font metrics
      */
     public static FontMetrics getFontMetrics(JComponent c, Graphics g, Font font) {
          return (FontMetrics)invokeSwingUtilities2StaticMethod("getFontMetrics"
                        , new Class[]{JComponent.class, Graphics.class, Font.class}
                        , new Object[]{c, g, font});
     }

     /**
      * Returns the width of the passed in String.
      *
      * @param c JComponent that will display the string, may be null
      * @param fm FontMetrics used to measure the String width
      * @param string String to get the width of
      * @return the int
      */
     public static int stringWidth(JComponent c, FontMetrics fm, String string){
          return (Integer)invokeSwingUtilities2StaticMethod("stringWidth"
                      , new Class[]{JComponent.class, FontMetrics.class, String.class}
                      , new Object[]{c, fm, string});
     }

     /**
      * Draws the string at the specified location.
      *
      * @param c JComponent that will display the string, may be null
      * @param g Graphics to draw the text to
      * @param text String to display
      * @param x X coordinate to draw the text at
      * @param y Y coordinate to draw the text at
      */
     public static void drawString(JComponent c, Graphics g, String text,
                                             int x, int y) {
          invokeSwingUtilities2StaticMethod("drawString"
                     , new Class[]{JComponent.class, Graphics.class, String.class, int.class, int.class}
                     , new Object[]{c, g, text, x, y});
     }

     /*
      * returns true if the Graphics is print Graphics
      * false otherwise
      */
     /**
      * Checks if is printing.
      *
      * @param g the g
      * @return true, if is printing
      */
     static boolean isPrinting(Graphics g) 
     {
          // return (g instanceof PrinterGraphics || g instanceof PrintGraphics);
          return (Boolean)invokeSwingUtilities2StaticMethod("isPrinting"
                         , new Class[]{Graphics.class}
                         , new Object[]{g});
     }

     /**
      * Returns whether or not text should be drawn antialiased.
      *
      * @param c JComponent to test.
      * @return Whether or not text should be drawn antialiased for the
      *            specified component.
      */
     private static boolean drawTextAntialiased(JComponent c) {
          return (Boolean)invokeSwingUtilities2StaticMethod("drawTextAntialiased"
                        , new Class[]{JComponent.class}
                        , new Object[]{c});
     }

     /**
      * Returns whether or not text should be drawn antialiased.
      *
      * @param aaText Whether or not aa text has been turned on for the
      *          component.
      * @return Whether or not text should be drawn antialiased.
      */
     public static boolean drawTextAntialiased(boolean aaText) {
          return (Boolean)invokeSwingUtilities2StaticMethod("drawTextAntialiased"
                      , new Class[]{boolean.class}
                      , new Object[]{aaText});
     }

     /* 
      * Tries it best to get Graphics2D out of the given Graphics
      * returns null if can not derive it.
      */
     /**
      * Gets the graphics2 d.
      *
      * @param g the g
      * @return the graphics2 d
      */
     public static Graphics2D getGraphics2D(Graphics g) {
          return (Graphics2D)invokeSwingUtilities2StaticMethod("getGraphics2D"
                     , new Class[]{Graphics.class}
                     , new Object[]{g});
     }
     
     /**
      * Draws the string at the specified location underlining the specified
      * character.
      *
      * @param c JComponent that will display the string, may be null
      * @param g Graphics to draw the text to
      * @param text String to display
      * @param underlinedIndex Index of a character in the string to underline
      * @param x X coordinate to draw the text at
      * @param y Y coordinate to draw the text at
      */
     public static void drawStringUnderlineCharAt(JComponent c,Graphics g,
                                    String text, int underlinedIndex, int x,int y) {
          invokeSwingUtilities2StaticMethod("drawStringUnderlineCharAt"
                     , new Class[]{JComponent.class, Graphics.class, String.class, int.class, int.class, int.class}
                     , new Object[]{c, g, text, underlinedIndex, x, y});
     }

    /**
     * Clips the passed in String to the space provided.
     *
     * @param c JComponent that will display the string, may be null
     * @param fm FontMetrics used to measure the String width
     * @param string String to display
     * @param availTextWidth Amount of space that the string can be drawn in
     * @return Clipped string that can fit in the provided space.
     */
    public static String clipStringIfNecessary(JComponent c, FontMetrics fm,
                                                             String string,
                                                             int availTextWidth) {
         string = (String)invokeSwingUtilities2StaticMethod("clipStringIfNecessary"
                    , new Class[]{JComponent.class, FontMetrics.class, String.class, int.class}
                    , new Object[]{c, fm, string, availTextWidth});
         return string;
    }

    /**
     * Clips the passed in String to the space provided.  NOTE: this assumes
     * the string does not fit in the available space.
     *
     * @param c JComponent that will display the string, may be null
     * @param fm FontMetrics used to measure the String width
     * @param string String to display
     * @param availTextWidth Amount of space that the string can be drawn in
     * @return Clipped string that can fit in the provided space.
     */
    public static String clipString(JComponent c, FontMetrics fm,
                                              String string, int availTextWidth) {
         string = (String)invokeSwingUtilities2StaticMethod("clipString"
                    , new Class[]{JComponent.class, FontMetrics.class, String.class, int.class}
                    , new Object[]{c, fm, string, availTextWidth});
         
         return string;
    }

    /**
     * Invoke swing utilities2 static method.
     *
     * @param methodName the method name
     * @param paramsType the params type
     * @param paramsValue the params value
     * @return the object
     */
    public static Object invokeSwingUtilities2StaticMethod(String methodName
              , Class[] paramsType, Object[] paramsValue) {
         return ReflectHelper.invokeStaticMethod(ReflectHelper.getClass(getSwingUtilities2ClassName())
                    , methodName, paramsType, paramsValue);
    }

    //* SwingUtilities2是SUN的非公开api，它在不同的版本里位于不同的包内，甚至某天有消失的可能哦
    //* 但是不用它又不行，好几个地方都用到了它，用反射的好处是不需要把代码拷过来，它将自动适应各
    //* 版本里的实现，比如在新版本里这些方法里可能已经优化了优化等等
    /**
     * Gets the swing utilities2 class name.
     *
     * @return the swing utilities2 class name
     */
    public static String getSwingUtilities2ClassName() {
         return "sun.swing.SwingUtilities2";
    }
}