package org.jb2011.lnf.beautyeye.utils;

import java.awt.Point;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

/**
 * 自定义 RepaintManager，用于修复圆角窗口在动态添加组件后下方两角变为直角的问题。
 *
 * <p><b>问题根因：</b><br>
 * Swing 的 {@code JComponent._paintImmediately()} 在上溯查找绘制起点时，
 * 会停在第一个 opaque（不透明）的祖先组件并从该处开始绘制，
 * 从而绕过 {@code BEClipLayeredPane.paintChildren()} 中的圆角裁切逻辑。
 * 例如，用户代码中常见的 {@code new JPanel()}（默认 opaque=true）
 * 被添加到内容面板后，便成为增量重绘的起点，导致圆角丢失。
 *
 * <p><b>解决方案：</b><br>
 * 当圆角窗口（{@code BeautyEye.frameRound=true}）中的子组件请求重绘时，
 * 将脏区域重定向到 {@code JLayeredPane}（即 BEClipLayeredPane），
 * 确保 Swing 绘制路径始终经过圆角裁切逻辑。
 *
 * <p>由 {@link org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper#implLNF()} 安装。
 *
 * @author lornwolf
 */
public class BERepaintManager extends RepaintManager {

    /**
     * 缓存每个 JRootPane 是否为圆角窗口的判断结果。
     * 使用 WeakHashMap 避免内存泄漏（JRootPane 销毁后自动清除）。
     * 注意：isFrameRound 的结果由 clientProperty 决定，在窗口初始化后不会改变，可以安全缓存。
     */
    private final WeakHashMap<JRootPane, Boolean> roundFrameCache = new WeakHashMap<>();

    private boolean isRoundFrame(JRootPane rp) {
        return roundFrameCache.computeIfAbsent(rp, BEUtils::isFrameRound);
    }

    @Override
    public synchronized void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
        // 快速路径：JLayeredPane 本身的重绘直接走父类，避免递归和无意义的树遍历
        if (c instanceof JLayeredPane) {
            super.addDirtyRegion(c, x, y, w, h);
            return;
        }

        JRootPane rp = SwingUtilities.getRootPane(c);
        if (rp != null && isRoundFrame(rp)) {
            JLayeredPane lp = rp.getLayeredPane();
            if (lp instanceof JComponent && SwingUtilities.isDescendingFrom(c, lp)) {
                // 将脏区域坐标从 c 的坐标系转换到 LayeredPane 的坐标系
                Point pt = SwingUtilities.convertPoint(c, x, y, lp);
                super.addDirtyRegion((JComponent) lp, pt.x, pt.y, w, h);
                return;
            }
        }
        super.addDirtyRegion(c, x, y, w, h);
    }
}
