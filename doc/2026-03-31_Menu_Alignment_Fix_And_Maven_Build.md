# BeautyEye 3.6 — 菜单项左侧对齐修复 & Maven 构建支持

**日期**: 2026-03-31
**前提**: Java 17 兼容性迁移及 JMenu 字体渲染修复完成后
**问题**: 弹出菜单中子菜单目录（JMenu）与普通菜单项（JMenuItem）左侧未对齐
**附加**: 新增 pom.xml，支持 Maven 编译打包

---

## 一、菜单项左侧对齐修复

### 问题现象

弹出菜单中，JMenu（带子菜单箭头的菜单目录）与 JMenuItem（普通菜单项）的文本及图标左侧存在水平偏移，未能对齐。

### 根因分析

Swing 的 `BasicMenuItemUI` 在布局菜单项时，通过 `MenuItemLayoutHelper` 计算同一弹出菜单中所有项的最大 checkIcon 宽度（`maxCheckWidth`），以此统一左侧缩进。菜单项的水平布局结构为：

```
[checkIcon] [icon] [text] [accelerator] [arrowIcon]
```

问题在于 `Menu.checkIcon` 和 `MenuItem.checkIcon` 未被显式设置，继承自 MetalLookAndFeel 的默认值尺寸不一致，导致 `MenuItemLayoutHelper` 计算出的 checkIcon 列宽度在不同类型的菜单项之间产生差异，进而使 icon 列和 text 列的起始位置不对齐。

### 修改内容

#### 1. `src/org/jb2011/lnf/beautyeye/ch9_menu/__UI__.java`

在杂项设置区域新增统一的空 checkIcon，与 CheckBoxMenuItem / RadioButtonMenuItem 的 checkIcon 保持相同尺寸（16×16）：

```java
// 为Menu和MenuItem设置统一尺寸的checkIcon，确保子菜单目录与普通菜单项左侧文本对齐
javax.swing.Icon emptyCheckIcon = new javax.swing.Icon() {
    public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {}
    public int getIconWidth() { return 16; }
    public int getIconHeight() { return 16; }
};
UIManager.put("Menu.checkIcon", emptyCheckIcon);
UIManager.put("MenuItem.checkIcon", emptyCheckIcon);
```

#### 2. `src/org/jb2011/lnf/beautyeye/BeautyEyeLookAndFeelWin.java`

**Vista 模式 border 统一**: `initForVista()` 中四种菜单项的 border 左边距统一为 3，修复原先 Menu(left=3) 与 MenuItem(left=0)、CheckBoxMenuItem(left=2) 与 RadioButtonMenuItem(left=0) 不一致的问题：

```diff
- UIManager.put("MenuItem.border",       ...createEmptyBorder(1,0,2,0));
- UIManager.put("CheckBoxMenuItem.border",...createEmptyBorder(4,2,4,2));
- UIManager.put("RadioButtonMenuItem.border",...createEmptyBorder(4,0,4,0));
+ UIManager.put("MenuItem.border",       ...createEmptyBorder(1,3,2,3));
+ UIManager.put("CheckBoxMenuItem.border",...createEmptyBorder(4,3,4,3));
+ UIManager.put("RadioButtonMenuItem.border",...createEmptyBorder(4,3,4,3));
```

**Vista 模式 checkIcon 统一**: 同样为 Menu 和 MenuItem 设置 16×16 的空 checkIcon，与 `__UI__.java` 中的处理保持一致。

### 影响范围

- 所有弹出菜单中的 JMenu、JMenuItem、JCheckBoxMenuItem、JRadioButtonMenuItem 的左侧对齐
- 无论菜单项是否设置了自定义 icon，左侧均能正确对齐
- 跨平台版（BeautyEyeLookAndFeelCross）和 Windows 平台版（BeautyEyeLookAndFeelWin）均已覆盖

---

## 二、新增 Maven 构建支持

### 新增文件

`pom.xml` — Maven 项目描述文件

### 配置要点

- **GAV 坐标**: `org.jb2011:beautyeye:3.6-java17`
- **编译级别**: Java 17（source 和 target 均为 17）
- **源码目录**: `src`（非 Maven 默认的 `src/main/java`）
- **资源处理**: 从 `src` 目录中包含所有非 `.java` 文件（PNG、9.png、properties 等）
- **本地依赖**: `library/ninepatch4j.jar` 以 system scope 引入，无需安装到本地仓库
- **输出**: `target/beautyeye-3.6-java17.jar`

### 使用方法

```bash
mvn clean package
```

输出文件位于 `target/beautyeye-3.6-java17.jar`。

---

## 三、修改文件清单

| 文件 | 变更类型 | 说明 |
|------|----------|------|
| `ch9_menu/__UI__.java` | 修改 | 新增 Menu.checkIcon 和 MenuItem.checkIcon 统一设置 |
| `BeautyEyeLookAndFeelWin.java` | 修改 | Vista 模式下统一 border 左边距及 checkIcon |
| `pom.xml` | 新增 | Maven 构建配置 |
