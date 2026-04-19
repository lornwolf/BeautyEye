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
import java.lang.reflect.Method;

import javax.swing.JComponent;

/**
 * 本类中的方法一一对应于SUN未公开的类SwingUtilities2中的方法。
 * <p>
 * 本类中的各方法都是自动据JVM版本不同对SwingUtilities2进行反射调用
 * 实现的，以便解决兼容性问题。.
 *
 */
public class MySwingUtilities2 {

    /**
     * True if sun.swing.SwingUtilities2 is accessible via reflection (Java 8),
     * false on Java 9+ without --add-opens java.desktop/sun.swing=ALL-UNNAMED.
     */
    private static final boolean USE_SUN_UTILITIES2;
    static {
        boolean ok = false;
        try {
            Class<?> cls = Class.forName("sun.swing.SwingUtilities2");
            // Test accessibility by trying to invoke a simple method with null arg
            Method m = cls.getMethod("isPrinting", Graphics.class);
            m.invoke(null, (Object[]) null);
            ok = true;
        } catch (java.lang.reflect.InvocationTargetException e) {
            ok = true; // method was accessible; threw NPE internally
        } catch (Exception e) {
            ok = false; // IllegalAccessException etc. — module not opened
        }
        USE_SUN_UTILITIES2 = ok;
    }

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
          if (USE_SUN_UTILITIES2) {
              FontMetrics fm = (FontMetrics)invokeSwingUtilities2StaticMethod("getFontMetrics"
                              , new Class[]{JComponent.class, Graphics.class}
                              , new Object[]{c, g});
              if (fm != null) return fm;
          }
          return c != null ? c.getFontMetrics(g.getFont()) : g.getFontMetrics(g.getFont());
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
          if (USE_SUN_UTILITIES2) {
              FontMetrics fm = (FontMetrics)invokeSwingUtilities2StaticMethod("getFontMetrics"
                              , new Class[]{JComponent.class, Graphics.class, Font.class}
                              , new Object[]{c, g, font});
              if (fm != null) return fm;
          }
          return c != null ? c.getFontMetrics(font) : g.getFontMetrics(font);
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
          if (USE_SUN_UTILITIES2) {
              Object result = invokeSwingUtilities2StaticMethod("stringWidth"
                          , new Class[]{JComponent.class, FontMetrics.class, String.class}
                          , new Object[]{c, fm, string});
              if (result != null) return (Integer) result;
          }
          return fm.stringWidth(string);
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
          if (USE_SUN_UTILITIES2) {
              invokeSwingUtilities2StaticMethod("drawString"
                         , new Class[]{JComponent.class, Graphics.class, String.class, int.class, int.class}
                         , new Object[]{c, g, text, x, y});
              return;
          }
          g.drawString(text, x, y);
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
     static boolean isPrinting(Graphics g) {
          if (USE_SUN_UTILITIES2) {
              Object result = invokeSwingUtilities2StaticMethod("isPrinting"
                              , new Class[]{Graphics.class}
                              , new Object[]{g});
              if (result != null) return (Boolean) result;
          }
          return false;
     }

     /**
      * Returns whether or not text should be drawn antialiased.
      *
      * @param c JComponent to test.
      * @return Whether or not text should be drawn antialiased for the
      *            specified component.
      */
     private static boolean drawTextAntialiased(JComponent c) {
          if (USE_SUN_UTILITIES2) {
              Object result = invokeSwingUtilities2StaticMethod("drawTextAntialiased"
                              , new Class[]{JComponent.class}
                              , new Object[]{c});
              if (result != null) return (Boolean) result;
          }
          return true;
     }

     /**
      * Returns whether or not text should be drawn antialiased.
      *
      * @param aaText Whether or not aa text has been turned on for the
      *          component.
      * @return Whether or not text should be drawn antialiased.
      */
     public static boolean drawTextAntialiased(boolean aaText) {
          if (USE_SUN_UTILITIES2) {
              Object result = invokeSwingUtilities2StaticMethod("drawTextAntialiased"
                          , new Class[]{boolean.class}
                          , new Object[]{aaText});
              if (result != null) return (Boolean) result;
          }
          return aaText;
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
          if (USE_SUN_UTILITIES2) {
              Graphics2D result = (Graphics2D)invokeSwingUtilities2StaticMethod("getGraphics2D"
                         , new Class[]{Graphics.class}
                         , new Object[]{g});
              if (result != null) return result;
          }
          return (g instanceof Graphics2D) ? (Graphics2D) g : null;
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
     public static void drawStringUnderlineCharAt(JComponent c, Graphics g,
                                    String text, int underlinedIndex, int x, int y) {
          if (USE_SUN_UTILITIES2) {
              invokeSwingUtilities2StaticMethod("drawStringUnderlineCharAt"
                         , new Class[]{JComponent.class, Graphics.class, String.class, int.class, int.class, int.class}
                         , new Object[]{c, g, text, underlinedIndex, x, y});
              return;
          }
          // Fallback: draw string, then underline the mnemonic character
          g.drawString(text, x, y);
          if (underlinedIndex >= 0 && underlinedIndex < text.length()) {
              FontMetrics fm = g.getFontMetrics();
              int ulX = x + fm.stringWidth(text.substring(0, underlinedIndex));
              int ulY = y + fm.getDescent() - 1;
              int ulW = fm.charWidth(text.charAt(underlinedIndex));
              g.drawLine(ulX, ulY, ulX + ulW - 1, ulY);
          }
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
         if (USE_SUN_UTILITIES2) {
             String result = (String)invokeSwingUtilities2StaticMethod("clipStringIfNecessary"
                        , new Class[]{JComponent.class, FontMetrics.class, String.class, int.class}
                        , new Object[]{c, fm, string, availTextWidth});
             if (result != null) return result;
         }
         if (fm.stringWidth(string) <= availTextWidth) return string;
         return clipString(c, fm, string, availTextWidth);
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
         if (USE_SUN_UTILITIES2) {
             String result = (String)invokeSwingUtilities2StaticMethod("clipString"
                        , new Class[]{JComponent.class, FontMetrics.class, String.class, int.class}
                        , new Object[]{c, fm, string, availTextWidth});
             if (result != null) return result;
         }
         // Fallback: truncate with ellipsis
         final String clip = "...";
         int clipWidth = fm.stringWidth(clip);
         if (clipWidth >= availTextWidth) return clip.substring(0, 1);
         int total = clipWidth;
         StringBuilder sb = new StringBuilder();
         for (int i = 0; i < string.length(); i++) {
             int cw = fm.charWidth(string.charAt(i));
             if (total + cw > availTextWidth) break;
             sb.append(string.charAt(i));
             total += cw;
         }
         return sb.toString() + clip;
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