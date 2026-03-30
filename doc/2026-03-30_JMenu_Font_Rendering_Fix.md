# BeautyEye 3.6 — JMenu 字体渲染修复记录

**日期**: 2026-03-30
**前提**: Java 17 兼容性迁移完成后（详见 `2026-03-00_Java17_Migration_Changes.md`）
**问题**: JMenu（菜单目录）与 JMenuItem（菜单项）的文字渲染外观不一致
**产出**: beautyeye-3.6-java17.jar（更新）

---

## 一、问题现象

使用 BeautyEye 皮肤的应用程序中，弹出菜单内的 JMenu 项（带子菜单箭头的菜单目录）与 JMenuItem 项文字外观明显不同：

- **JMenuItem**: 文字正常，使用用户设定的字体（如 Yahei Mono）且渲染清晰
- **JMenu**: 文字偏细、偏窄，视觉上像使用了不同的字体

通过 `component.getFont()` 和 `Graphics.getFont()` 的调试输出确认，两类组件的字体数据模型完全一致（均为用户设定的字体），**问题出在文本渲染路径而非字体设置**。

---

## 二、根因分析

### 文本渲染路径对比

| 组件 | UI 类 | paintText 实现 | 文本渲染方法 |
|------|--------|---------------|-------------|
| JMenuItem | BEMenuItemUI (extends BasicMenuItemUI) | 继承 BasicMenuItemUI.paintText() | `sun.swing.SwingUtilities2.drawStringUnderlineCharAt()` — **JDK 内部直接调用** |
| JMenu | BEMenuUI (extends BasicMenuUI) | **重写** paintText() | `WinUtils.paintText()` → `MySwingUtilities2.drawStringUnderlineCharAt()` → **反射调用失败后回退到 `g.drawString()`** |

### 详细调用链

**JMenuItem 的正常路径**:
```
BasicMenuItemUI.paintMenuItem()
  → g.setFont(menuItem.getFont())     // 设置正确字体
  → BasicMenuItemUI.paintText()
    → SwingUtilities2.drawStringUnderlineCharAt()  // 同模块内直接调用
      → 使用系统文本渲染提示（ClearType 亚像素渲染等）
```

**JMenu 的问题路径**:
```
BasicMenuItemUI.paintMenuItem()
  → g.setFont(menuItem.getFont())     // 设置正确字体（数据正确！）
  → BEMenuUI.paintText()              // 重写方法
    → WinUtils.paintText()
      → MySwingUtilities2.drawStringUnderlineCharAt()
        → 反射调用 sun.swing.SwingUtilities2 → Java 9+ 模块限制，IllegalAccessException
        → 回退到 g.drawString(text, x, y)  // 丢失文本渲染提示！
```

### 核心原因

`BEMenuUI.paintText()` 原本是从 `WindowsMenuUI` 复制而来，通过 `WinUtils` → `MySwingUtilities2` 间接调用 `sun.swing.SwingUtilities2`。在 Java 8 时代，反射调用可以正常工作，两条路径的渲染效果一致。

Java 9 引入模块系统（JPMS）后，`sun.swing.SwingUtilities2` 被封装在 `java.desktop` 模块内部。`MySwingUtilities2` 通过反射调用时会抛出 `IllegalAccessException`，于是回退到普通的 `g.drawString()`。而 `BasicMenuItemUI.paintText()` 与 `SwingUtilities2` 在同一个模块内，可以直接调用，不受此限制。

`SwingUtilities2` 在绘制文本前会设置系统级的文本渲染提示（如 Windows 上的 ClearType LCD 亚像素渲染），而 `g.drawString()` 不会主动设置这些提示，导致相同字体在两条路径下渲染出截然不同的视觉效果。

---

## 三、修改内容

### 修改文件清单

| 文件 | 变更类型 | 说明 |
|------|----------|------|
| `ch9_menu/BEMenuUI.java` | 重写 | `paintText()` 改为调用 `super.paintText()` |
| `BeautyEyeLookAndFeelWin.java` | 优化 | `overrideMetalFontsWithSystemFont()` 移至 `getDefaults()` |

### 1. `src/org/jb2011/lnf/beautyeye/ch9_menu/BEMenuUI.java`

**变更**: 重写 `paintText()` 方法，用 `super.paintText()` 替代 `WinUtils.paintText()`

```diff
  protected void paintText(Graphics g, JMenuItem menuItem,
          Rectangle textRect, String text)
  {
      JMenu menu = (JMenu)menuItem;
-     ButtonModel model = menuItem.getModel();
      Color oldColor = g.getColor();

-     // Only paint rollover if no other menu on menubar is selected
-     boolean paintRollover = model.isRollover();
-     if (paintRollover && menu.isTopLevelMenu()) {
-         MenuElement[] menus = ((JMenuBar)menu.getParent()).getSubElements();
-         for (int i = 0; i < menus.length; i++) {
-             if (((JMenuItem)menus[i]).isSelected()) {
-                 paintRollover = false;
-                 break;
-             }
-         }
-     }
-
-     if ((model.isSelected() && !menu.isTopLevelMenu())
-             || (paintRollover || model.isArmed() || model.isSelected()))
-     {
-         g.setColor(selectionForeground);
-     }
-
+     //特殊处理顶级菜单项（就是直接放在JMenuBar上的那一层），使之保持黑色
      if(menu.isTopLevelMenu())
          g.setColor(new Color(35,35,35));

-     WinUtils.paintText(g, menuItem, textRect, text, 0);
+     // 使用父类（BasicMenuItemUI）的paintText，内部直接调用
+     // sun.swing.SwingUtilities2（同模块，无反射问题），
+     // 保证文本渲染与JMenuItem完全一致。
+     super.paintText(g, menuItem, textRect, text);

      g.setColor(oldColor);
  }
```

**说明**:
- 删除了从 `WindowsMenuUI` 复制来的选中/Rollover 状态前景色处理逻辑 — `BasicMenuItemUI.paintText()` 内部已包含完整的启用/禁用状态文本绘制逻辑
- 保留了顶级菜单项（直接放在 JMenuBar 上的那一层）的颜色特殊处理
- 移除了对 `WinUtils` 的 import（该类在此文件中不再使用）

### 2. `src/org/jb2011/lnf/beautyeye/BeautyEyeLookAndFeelWin.java`

**变更**: `overrideMetalFontsWithSystemFont()` 的调用位置从 `initComponentDefaults()` 移至新增的 `getDefaults()` 重写方法

```diff
+ @Override
+ public UIDefaults getDefaults()
+ {
+     UIDefaults table = super.getDefaults();
+     overrideMetalFontsWithSystemFont(table);
+     return table;
+ }

  protected void initComponentDefaults(UIDefaults table)
  {
      super.initComponentDefaults(table);
      initOtherResourceBundle(table);
-     overrideMetalFontsWithSystemFont(table);
  }
```

**说明**: `MetalLookAndFeel.getDefaults()` 在调用 `initComponentDefaults()` 之后还会执行 `currentTheme.addCustomEntriesToTable(table)`，可能重新用 `FontActiveValue` 覆盖字体。将字体替换移至 `getDefaults()` 返回前，确保在所有 Metal 主题处理完成后执行。

---

## 四、验证方法

通过在 `BEMenuUI.paintText()` 中添加临时调试输出，确认了问题的性质：

```java
System.out.println("[BEMenuUI.paintText] text='" + text
    + "' component.getFont=" + menuItem.getFont()
    + " g.getFont=" + g.getFont());
```

调试输出证实所有 JMenu 的 `component.getFont()` 和 `g.getFont()` 均为用户设定的字体（Yahei Mono 14pt），排除了字体数据模型的问题，确认是渲染路径导致的视觉差异。

修复后，JMenu 和 JMenuItem 的文字渲染完全一致。

---

## 五、影响范围

- **JMenu 文本渲染**: 所有弹出菜单中的 JMenu 项（菜单目录）及 JMenuBar 中的顶级菜单项
- **兼容性**: 无负面影响。`super.paintText()` 即 `BasicMenuItemUI.paintText()`，是 JDK 标准实现，在所有 Java 版本上均可正常工作
- **移除的依赖**: `BEMenuUI` 不再依赖 `WinUtils` / `MySwingUtilities2` 进行文本渲染
