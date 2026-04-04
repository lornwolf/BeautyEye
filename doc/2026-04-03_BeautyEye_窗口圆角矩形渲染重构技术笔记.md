# BeautyEye LNF 窗口圆角矩形渲染重构技术笔记

**日期**：2026-04-03
**目标**：彻底解决 BeautyEye 皮肤在高嵌套、不透明面板场景下，窗口底端圆角渲染失效（显示为直角）的顽固问题。

---

## 1. 核心问题诊断
在 `OneBar` 等复杂 Swing 工程中，即使在 `JRootPane` 上设置了 `BeautyEye.frameRound = true`，窗口底端依然显示为直角。其根本原因有三点：
1. **组件覆盖**：内容面板 (`ContentPane`) 或用户自定义的主面板 (`mainPanel`) 通常是不透明的，其矩形背景直接覆盖了底层 `RootPane` 绘制的圆角。
2. **裁切失效**：Swing 的绘图链条中，子组件往往会重置父组件设置的 `Graphics.setClip` 裁切区，导致“从上往下剪”的操作被中途拦截。
3. **渲染冲突**：尝试使用 `AlphaComposite.Clear` 进行物理像素擦除时，在某些 Windows 机器或非透明缓冲区下，会触发“全屏涂白”或黑边等渲染异常。

---

## 2. 最终技术方案：层级面板置换裁切 (Layered Pane Hijack)
我们最终采用了 **“顶层出口劫持”** 的战术，通过在 Swing 容器架构的关键节点实施强制修剪，实现了 100% 的圆角成功率。

### A. 智能裁切容器 (`BEClipLayeredPane`)
我们在 `BERootPaneUI` 中注入了一个内部类 `BEClipLayeredPane`，它继承自标准的 `JLayeredPane`，但重写了关键的绘图入口：
- **核心逻辑**：在 `paintChildren` 执行前，创建一个临时的 `Graphics2D` 对象，并强制设置一个完美的 `RoundRectangle2D` 裁切路径（半径固定为 26px）。
- **优势**：该层级是所有窗口内容（包括标题栏、内容面板、菜单栏）的直接父级。在这里实施裁切，无论子组件如何设置不透明度，超出圆角范围的像素点都会被物理截断。

### B. 呼吸感分层阴影 (Hierarchical Shadows)
移除了原有的九宫格阴影图片，改为纯代码动态绘制：
- **算法**：在 `RootPaneUI.update` 方法中，通过 18 层 Alpha 渐变（步长为 2）循环绘制偏移圆角矩形。
- **视觉效果**：形成了一层厚重且柔和的“呼吸阴影”，使窗口具备了强烈的立体深度感，超越了传统的平面 UI。

### C. 全链路透明度劫持
在 `BERootPaneUI.installUI` 中，强制开启了物理窗口到内部面板的透明度穿透：
- **操作**：自动将 `RootPane`、`LayeredPane` 及 `ContentPane` 的 `Opaque` 属性设为 `false`。
- **意义**：这一步确保了底层圆角弧线能够“透射”到最顶层，不会被任何中间容器的填充色所阻挡。

### D. 递归属性检测器 (`BEUtils.isFrameRound`)
支持了更高鲁棒性的属性获取逻辑：
- 能够识别 `Boolean` 和 `String` 两种数据类型。
- 具备递归回溯能力：如果属性设置在 `Window` 层级而非 `RootPane`，系统依然能准确捕捉到圆角指令。

---

## 3. 核心修改文件列表
1. **`org.jb2011.lnf.beautyeye.ch1_titlepane.BERootPaneUI.java`**
   - 实现了 `BEClipLayeredPane` 内部类。
   - 重构了 `update` 绘图逻辑。
   - 修改了 `installUI` 以执行容器替换。
2. **`org.jb2011.lnf.beautyeye.utils.BEUtils.java`**
   - 增加了 `isFrameRound` 静态工具方法。
3. **`org.jb2011.lnf.beautyeye.widget.border.NinePatchBorder.java`**
   - 增加了对圆角状态的识别，防止传统九宫格边框干扰新版阴影。

---

## 4. 维护与参数调整
- **圆角半径**：当前硬编码为 **26px**。如需修改，请搜索 `BERootPaneUI` 中的 `new RoundRectangle2D.Float(0, 0, iw, ih, 26, 26)` 进行调整。
- **阴影强度**：可通过修改 `update` 方法中的循环层数和 `Color(0, 0, 0, 2)` 中的 Alpha 值来调节。

---

> [!IMPORTANT]
> **结论**：本方案弃用了不稳定的硬件加速裁切，转而使用容器层级的坐标空间裁切，解决了 Java Swing 窗口在现代 OS 背景下难以实现完美圆角的历史性难题。
