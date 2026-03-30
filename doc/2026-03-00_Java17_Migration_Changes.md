# BeautyEye 3.6 — Java 17 兼容性迁移变更记录

**日期**: 2026-03-30
**目标**: 将 BeautyEye 3.6 从 Java 8 迁移到 Java 17，解决所有内部 API 不兼容问题
**产出**: beautyeye-3.6-java17.jar

---

## 一、变更概述

Java 9 引入的模块系统 (JPMS) 将 `sun.*`、`com.sun.*` 等内部包标记为不可导出，导致 BeautyEye 中大量依赖这些内部 API 的代码在 Java 17 下无法编译或运行。本次迁移共修改 **8 个源文件**，全部替换为公开 API 或自实现的回退方案。

### 修改文件清单

| 文件 | 变更类型 | 说明 |
|------|----------|------|
| `utils/JVM.java` | 修复 | 版本检测支持 Java 9+ 新版本号格式 |
| `utils/MySwingUtilities2.java` | 重构 | 为所有 `sun.swing.SwingUtilities2` 方法添加回退实现 |
| `utils/WindowTranslucencyHelper.java` | 简化 | 移除 `com.sun.awt.AWTUtilities` 反射，改用 Java 7+ 标准 API |
| `BeautyEyeLookAndFeelWin.java` | 重写 | 基类从 `WindowsLookAndFeel` 改为 `MetalLookAndFeel`，并修复字体问题 |
| `ch20_filechooser/BEFileChooserUIWin.java` | 重写 | 基类从 `WindowsFileChooserUI` 改为 `MetalFileChooserUI` |
| `ch3_button/BEToggleButtonUI.java` | 修复 | 移除 `sun.awt.AppContext`，改用单例模式 |
| `ch5_table/BETableHeaderUI.java` | 修复 | `sun.swing.table.DefaultTableCellHeaderRenderer` → 公开 API |
| `ch17_split/BESplitPaneDivider.java` | 修复 | `sun.swing.DefaultLookup` → `UIManager` |
| `ch1_titlepane/BERootPaneUI.java` | 修复 | 移除 `AccessController.doPrivileged`，直接调用 |
| `ch1_titlepane/BETitlePane.java` | 补充 | 添加 `@Deprecated` 注解 |
| `widget/border/BEShadowBorder2.java` | 补充 | 添加 `@Deprecated` 注解 |

---

## 二、各文件详细差分

### 1. `src/org/jb2011/lnf/beautyeye/utils/JVM.java`

**问题**: Java 9+ 版本号格式从 `1.x.y` 变为 `x.y.z`（如 `17.0.3`），原代码将所有 Java 9+ 版本错误识别为 JDK1_3。

```diff
  public final static int JDK1_9 = 32;
+
+    /** Java 10 and above (Java 10=33, Java 11=34, ..., Java 17=40, ...) */
+    public final static int JDK10 = 33;
+    public final static int JDK11 = 34;
+    public final static int JDK17 = 40;
```

```diff
  public JVM(String p_JavaVersion) {
+     // Java 9+ uses new versioning: "9", "9.0.1", "10.0.2", "17.0.3", etc.
+     if (!p_JavaVersion.startsWith("1.")) {
+         try {
+             int major = Integer.parseInt(p_JavaVersion.split("[.\\-+]")[0]);
+             if (major >= 9) {
+                 // JDK1_9=32, Java 10=33, Java 11=34, ..., Java 17=40
+                 jdkVersion = JDK1_9 + (major - 9);
+             } else {
+                 jdkVersion = JDK1_3;
+             }
+         } catch (NumberFormatException e) {
+             jdkVersion = JDK1_3;
+         }
+     }
+     // Legacy 1.x.x format below
-     if (p_JavaVersion.startsWith("1.9.")) {
+     else if (p_JavaVersion.startsWith("1.9.")) {
```

```diff
      // 原 else 无条件赋值 → 改为条件赋值以避免覆盖上面的结果
-     } else {
+     } else if (jdkVersion == 0) {
          jdkVersion = JDK1_3;
      }
```

---

### 2. `src/org/jb2011/lnf/beautyeye/utils/MySwingUtilities2.java`

**问题**: `sun.swing.SwingUtilities2` 在 Java 17 中受模块系统保护，反射调用会抛出 `IllegalAccessException`。

新增静态可访问性检测和所有方法的回退实现：

```diff
+ private static final boolean USE_SUN_UTILITIES2;
+ static {
+     boolean ok = false;
+     try {
+         Class<?> cls = Class.forName("sun.swing.SwingUtilities2");
+         Method m = cls.getMethod("isPrinting", Graphics.class);
+         m.invoke(null, (Object[]) null);
+         ok = true;
+     } catch (java.lang.reflect.InvocationTargetException e) {
+         ok = true; // 方法可访问，但因 null 参数抛出 NPE
+     } catch (Exception e) {
+         ok = false; // IllegalAccessException — 模块未开放
+     }
+     USE_SUN_UTILITIES2 = ok;
+ }
```

各方法回退策略：

| 方法 | 回退实现 |
|------|----------|
| `getFontMetrics(c, g)` | `c.getFontMetrics(g.getFont())` |
| `getFontMetrics(c, g, font)` | `c.getFontMetrics(font)` |
| `stringWidth(c, fm, string)` | `fm.stringWidth(string)` |
| `drawString(c, g, text, x, y)` | `g.drawString(text, x, y)` |
| `isPrinting(g)` | 返回 `false` |
| `drawTextAntialiased(c)` | 返回 `true` |
| `drawTextAntialiased(aaText)` | 返回 `aaText` |
| `getGraphics2D(g)` | `(g instanceof Graphics2D) ? (Graphics2D) g : null` |
| `drawStringUnderlineCharAt(...)` | `g.drawString()` + 手动绘制下划线 |
| `clipStringIfNecessary(...)` / `clipString(...)` | 手动截断 + `"..."` |

---

### 3. `src/org/jb2011/lnf/beautyeye/utils/WindowTranslucencyHelper.java`

**问题**: `com.sun.awt.AWTUtilities` 在 Java 9 中被移除，反射路径失效。

```diff
- import java.lang.reflect.Method;

  // isTranslucencySupported() — 移除所有反射分支
- if(JVM.current().isOrLater(JVM.JDK1_7)) {
-     Method m = GraphicsDevice.class.getMethod("isWindowTranslucencySupported", cTranslucency);
-     ...
- } else {
-     Class c = Class.forName("com.sun.awt.AWTUtilities");
-     ...
- }
+ // Java 7+ standard API (com.sun.awt.AWTUtilities removed in Java 9)
+ GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
+ GraphicsDevice gd = ge.getDefaultScreenDevice();
+ return gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);

  // setOpacity() — 直接使用 Java 7+ API
- // 移除 com.sun.awt.AWTUtilities.setWindowOpacity() 反射分支
+ w.setOpacity(opacity);

  // setWindowOpaque() — 直接使用 Java 7+ API
- // 移除 com.sun.awt.AWTUtilities.setWindowOpaque() 反射分支
+ w.setBackground(new Color(bgc.getRed(), bgc.getGreen(), bgc.getBlue(), opaque ? 255 : 0));
```

---

### 4. `src/org/jb2011/lnf/beautyeye/BeautyEyeLookAndFeelWin.java`

**问题**: `com.sun.java.swing.plaf.windows.WindowsLookAndFeel` 在 Java 9+ 中不再对外导出，运行时抛出 `IllegalAccessError`。

#### 4a. 基类变更

```diff
- import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
+ import javax.swing.plaf.metal.MetalLookAndFeel;

- public class BeautyEyeLookAndFeelWin extends WindowsLookAndFeel
+ public class BeautyEyeLookAndFeelWin extends MetalLookAndFeel
```

#### 4b. 构造函数新增 Metal 适配

```diff
  public BeautyEyeLookAndFeelWin() {
      super();
+     // 取消 Metal LNF 中默认的粗体字
+     UIManager.put("swing.boldMetal", Boolean.FALSE);
+     // TabbedPane 透明背景
+     UIManager.put("TabbedPane.contentOpaque", Boolean.FALSE);
+     UIManager.put("TabbedPane.tabsOpaque", Boolean.FALSE);
      ...
  }
```

#### 4c. 新增覆盖方法

```diff
+ @Override
+ public boolean isNativeLookAndFeel() { return false; }
+
+ @Override
+ public boolean isSupportedLookAndFeel() { return true; }
```

#### 4d. 字体修复（解决菜单项 setFont() 不生效问题）

```diff
  protected void initComponentDefaults(UIDefaults table) {
      super.initComponentDefaults(table);
      initOtherResourceBundle(table);
+     overrideMetalFontsWithSystemFont(table);
  }

+ /**
+  * MetalLookAndFeel会将所有组件字体强制设为Metal主题字体（Dialog），
+  * 用Windows系统字体替换，恢复原WindowsLookAndFeel的字体行为。
+  */
+ private void overrideMetalFontsWithSystemFont(UIDefaults table) {
+     Font sysFont = (Font) Toolkit.getDefaultToolkit()
+             .getDesktopProperty("win.defaultGUI.font");
+     if (sysFont == null) return;
+
+     FontUIResource fontResource = new FontUIResource(sysFont);
+     String[] fontKeys = {
+         "Button.font", "CheckBox.font", "CheckBoxMenuItem.font",
+         "ColorChooser.font", "ComboBox.font", "EditorPane.font",
+         "FormattedTextField.font", "Label.font", "List.font",
+         "Menu.font", "MenuBar.font", "MenuItem.font",
+         "OptionPane.font", "Panel.font", "PasswordField.font",
+         "PopupMenu.font", "ProgressBar.font", "RadioButton.font",
+         "RadioButtonMenuItem.font", "ScrollPane.font", "Spinner.font",
+         "TabbedPane.font", "Table.font", "TableHeader.font",
+         "TextArea.font", "TextField.font", "TextPane.font",
+         "TitledBorder.font", "ToggleButton.font", "ToolBar.font",
+         "ToolTip.font", "Tree.font", "Viewport.font",
+         "InternalFrame.titleFont"
+     };
+     for (String key : fontKeys) {
+         table.put(key, fontResource);
+     }
+ }
```

**根因分析**: `MetalLookAndFeel.initComponentDefaults()` 使用 `FontActiveValue`（`ActiveValue` 接口）包装字体，每次 `UIDefaults.get()` 都会重新求值为 Metal 主题字体（Dialog），覆盖用户的 `setFont()` 设置。用普通的 `FontUIResource` 替换后，字体值固定，用户调用 `component.setFont()` 即可正常覆盖。

---

### 5. `src/org/jb2011/lnf/beautyeye/ch20_filechooser/BEFileChooserUIWin.java`

**问题**: `com.sun.java.swing.plaf.windows.WindowsFileChooserUI` 不可访问。

```diff
- import com.sun.java.swing.plaf.windows.WindowsFileChooserUI;
+ import javax.swing.plaf.metal.MetalFileChooserUI;

- public class BEFileChooserUIWin extends WindowsFileChooserUI
+ public class BEFileChooserUIWin extends MetalFileChooserUI
```

保留 `paint()` 背景填充和 `createList()` 列表行高修复逻辑不变。

---

### 6. `src/org/jb2011/lnf/beautyeye/ch3_button/BEToggleButtonUI.java`

**问题**: `sun.awt.AppContext` 在 Java 9+ 中已移除。

```diff
- import sun.awt.AppContext;
...
- // 原实现通过 AppContext 缓存 UI 实例
- private static final Object TOGGLE_BUTTON_UI_KEY = new Object();
- public static ComponentUI createUI(JComponent b) {
-     AppContext appContext = AppContext.getAppContext();
-     BEToggleButtonUI toggleButtonUI =
-         (BEToggleButtonUI) appContext.get(TOGGLE_BUTTON_UI_KEY);
-     if (toggleButtonUI == null) {
-         toggleButtonUI = new BEToggleButtonUI();
-         appContext.put(TOGGLE_BUTTON_UI_KEY, toggleButtonUI);
-     }
-     return toggleButtonUI;
- }

+ /** Shared instance — one per JVM (AppContext removed in Java 9+). */
+ private static volatile BEToggleButtonUI sharedInstance;
+
+ public static ComponentUI createUI(JComponent b) {
+     if (sharedInstance == null) {
+         synchronized (BEToggleButtonUI.class) {
+             if (sharedInstance == null) {
+                 sharedInstance = new BEToggleButtonUI();
+             }
+         }
+     }
+     return sharedInstance;
+ }
```

---

### 7. `src/org/jb2011/lnf/beautyeye/ch5_table/BETableHeaderUI.java`

**问题**: `sun.swing.table.DefaultTableCellHeaderRenderer` 在 Java 17 中不可访问。

```diff
- import sun.swing.table.DefaultTableCellHeaderRenderer;
+ import javax.swing.table.DefaultTableCellRenderer;

  // 类中所有引用替换
- class XPDefaultRenderer extends DefaultTableCellHeaderRenderer { ... }
+ class XPDefaultRenderer extends DefaultTableCellRenderer { ... }
```

---

### 8. `src/org/jb2011/lnf/beautyeye/ch17_split/BESplitPaneDivider.java`

**问题**: `sun.swing.DefaultLookup` 在 Java 17 中不可访问。

```diff
- import sun.swing.DefaultLookup;
...
- oneTouchSize = DefaultLookup.getInt(ui.getSplitPane(), ui,
-         "SplitPane.oneTouchButtonSize", ONE_TOUCH_SIZE);
+ Object uiVal = UIManager.get("SplitPane.oneTouchButtonSize");
+ oneTouchSize = (uiVal instanceof Integer) ? (Integer) uiVal : ONE_TOUCH_SIZE;
```

---

### 9. `src/org/jb2011/lnf/beautyeye/ch1_titlepane/BERootPaneUI.java`

**问题**: `java.security.AccessController` 在 Java 17 中标记为 deprecated for removal。

```diff
- import java.security.AccessController;
- import java.security.PrivilegedActionException;
- import java.security.PrivilegedExceptionAction;
...
- private final PrivilegedExceptionAction getLocationAction =
-         new PrivilegedExceptionAction() {
-     public Object run() throws HeadlessException {
-         return MouseInfo.getPointerInfo().getLocation();
-     }
- };
+ // Previously PrivilegedExceptionAction; SecurityManager removed in Java 9+.
+ private final Runnable getLocationAction = new Runnable() {
+     public void run() { }
+ };

  // 窗口拖动处理
- try {
-     pt = (Point) AccessController.doPrivileged(getLocationAction);
-     ...
- } catch (PrivilegedActionException e) { }
+ Point windowPt = MouseInfo.getPointerInfo().getLocation();
+ windowPt.x = windowPt.x - dragOffsetX;
+ windowPt.y = windowPt.y - dragOffsetY;
+ w.setLocation(windowPt);
```

---

### 10. 注解补充

**`ch1_titlepane/BETitlePane.java`** — `setButtonIcon()` 方法有 `@deprecated` Javadoc 但缺少注解：
```diff
+ @Deprecated
  public static void setButtonIcon(AbstractButton btn, Icon ico)
```

**`widget/border/BEShadowBorder2.java`** — 类级别 `@deprecated` Javadoc 缺少注解：
```diff
+ @Deprecated
  public class BEShadowBorder2 extends NinePatchBorder
```

---

## 三、编译与打包

```bash
# 编译 (Java 17, target Java 8)
find src -name "*.java" > sources.txt
javac -encoding UTF-8 --release 8 -d build/classes -cp "library/ninepatch4j.jar" @sources.txt

# 复制资源文件 (PNG, 9.png, GIF, properties)
# PowerShell 脚本递归复制 src/ 下所有资源文件到 build/classes/ 保持目录结构

# 打包 JAR
cd build/classes && jar cf ../../beautyeye-3.6-java17.jar .
```

---

## 四、已知限制

1. **MetalLookAndFeel 替代 WindowsLookAndFeel**: Windows 平台版现在基于 MetalLookAndFeel，文件选择器的外观和行为与原 Windows 风格略有差异。
2. **SwingUtilities2 回退**: 在 Java 17 下，文本绘制回退到标准 `Graphics.drawString()`，可能丢失部分 ClearType 亚像素渲染优化。如需恢复，可在启动时添加 JVM 参数 `--add-opens java.desktop/sun.swing=ALL-UNNAMED`。
3. **字体**: Windows 平台版通过 `Toolkit.getDesktopProperty("win.defaultGUI.font")` 获取系统字体。在非 Windows 平台下该属性返回 null，将保留 Metal 默认字体。
