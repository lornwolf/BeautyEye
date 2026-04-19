# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在操作本仓库代码时提供指引。

## 项目概述

BeautyEye 是一个自定义 Java Swing 外观与感觉（L&F）库，继承自 MetalLookAndFeel，为 Swing 应用提供现代、美观的 UI 皮肤。项目最初面向 Java 5/6，现已迁移至 Java 17。

## 构建

基于 Maven 构建，使用本地 system 范围依赖（`library/ninepatch4j.jar`）：

```bash
mvn clean package          # 构建 JAR（输出：target/beautyeye-3.6-java17.jar）
mvn compile                # 仅编译
```

源码目录为 `src/`（非 Maven 默认的 `src/main/java/`）。非 Java 资源文件（图片、properties）也在 `src/` 下，会一并打包进 JAR。

## 架构

### 入口点

两个 L&F 主类（使用方调用 `UIManager.setLookAndFeel()`）：
- **`BeautyEyeLookAndFeelCross`** — 跨平台版（继承 MetalLookAndFeel）
- **`BeautyEyeLookAndFeelWin`** — Windows 专用版（继承 WindowsLookAndFeel）

两者均委托给 **`BeautyEyeLNFHelper`** 执行，由其统一编排所有组件 UI 注册及全局设置。

### 包结构

各 Swing 组件类别分别位于 `org.jb2011.lnf.beautyeye` 下以 `ch*_` 为前缀的包中：

| 包名 | 组件 |
|---------|-----------|
| `ch1_titlepane` | 窗口标题栏、根面板（`BERootPaneUI`） |
| `ch2_tab` | TabbedPane（选项卡面板） |
| `ch3_button` | Button、ToggleButton |
| `ch4_scroll` | ScrollBar、ScrollPane |
| `ch5_table` | Table、TableHeader |
| `ch6_textcoms` | TextField、TextArea、PasswordField 等 |
| `ch7_popup` | Popup/tooltip（弹出框/提示） |
| `ch8_toolbar` | Toolbar（工具栏） |
| `ch9_menu` | Menu、MenuBar、MenuItem、PopupMenu |
| `ch10_internalframe` | InternalFrame（内部窗口） |
| `ch12_progress` | ProgressBar（进度条） |
| `ch13_radio$cb_btn` | RadioButton、CheckBox |
| `ch14_combox` | ComboBox（下拉框） |
| `ch15_slider` | Slider（滑块） |
| `ch16_tree` | Tree（树形控件） |
| `ch17_split` | SplitPane（分隔面板） |
| `ch18_spinner` | Spinner |
| `ch19_list` | List（列表） |
| `ch20_filechooser` | FileChooser（文件选择器） |
| `ch_x` | 分隔线及其他杂项 |

### 包内类的约定

每个 `ch*_` 包遵循统一规范：
- **`__UI__`** — 包含静态 `uiImpl()` 方法，通过 `UIManager.put()` 注册 UIDefaults，由 `BeautyEyeLNFHelper.implLNF()` 调用。
- **`__IconFactory__`** / **`__Icon9Factory__`** — 该组件视觉资源的图标/NinePatch 图片工厂（单例模式）。
- **`BE*UI`** — 实际的 `ComponentUI` 子类（如 `BEButtonUI`、`BEScrollBarUI`）。

### 辅助包

- **`utils`** — JVM 检测、平台判断、NinePatch 工具、反射工具
- **`widget`** — 可复用 UI 控件及自定义边框（`BEShadowBorder`、`BEDashedBorder` 等）
- **`resources`** — i18n 资源包

### 核心设计理念

- **NinePatch 渲染**：组件背景通过 `ninepatch4j.jar` 使用 Android 风格的九宫格图片，由 `NinePatchHelper` 和 `N9ComponentFactory` 工具类管理。
- **窗口透明**：`BERootPaneUI` 负责窗口装饰、圆角及半透明效果，由 `BeautyEyeLNFHelper.frameBorderStyle` 控制。
- **全局配置**：`BeautyEyeLNFHelper` 暴露静态字段（`debug`、`translucencyAtFrameInactive`、`frameBorderStyle`、`activeCaptionTextColor` 等），使用方在调用 `UIManager.setLookAndFeel()` 前设置。

## 语言规范

源码注释和提交信息使用简体中文，请遵循此约定。
