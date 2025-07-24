[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[English](README.md) | [简体中文](README.zh.md)

# eGPS 2.1

eGPS 2.1 源代码 - 先进的生物信息学分析平台

## 目录

- [关于项目](#关于项目)
- [功能特性](#功能特性)
- [快速开始](#快速开始)
- [环境要求](#环境要求)
- [安装指南](#安装指南)
- [使用说明](#使用说明)
- [模块介绍](#模块介绍)
- [贡献指南](#贡献指南)
- [在IntelliJ IDEA中运行](#在intellij-idea中运行)
- [Maven配置](#maven配置)
- [许可证](#许可证)
- [致谢](#致谢)

## 关于项目

eGPS（增强型GPS）2.1 是一个先进的生物信息学分析平台，提供各种进化分析、序列处理、系统发育树构建和可视化的工具。与传统的GPS系统不同，eGPS 2.1 专注于生物数据分析，为进化生物学和基因组学领域的研究人员提供了一整套分析模块。

本仓库包含了eGPS 2.1系统的源代码，该系统使用Java构建，需要Java 21或更高版本才能运行。

## 功能特性

- 进化分析工具
- 序列处理和操作
- 系统发育树构建和可视化
- 数据可视化模块（热图、弦图、维恩图等）
- 模块化设计，拥有60多个专业分析模块
- 跨平台兼容性
- 可扩展架构

## 快速开始

以下说明将帮助您在本地机器上获取并运行该项目副本。

### 环境要求

使用本项目前需要安装的软件、库或工具：
- Java开发工具包 (JDK) 21或更高版本
- Apache Maven 3.6或更高版本
- Git

### 安装指南

1. 克隆仓库：
   ```bash
   git clone https://github.com/your-username/eGPS2.1.git
   ```
2. 进入项目目录：
   ```bash
   cd eGPS2.1
   ```
3. 构建项目：
   ```bash
   mvn clean install
   ```

## 使用说明

构建项目后，可以通过以下命令运行主应用程序：
```bash
java -jar target/eGPS2.1.jar
```

各个模块的详细使用说明可以在其各自的文档中找到。

## 模块介绍

eGPS 2.1由60多个专门用于各种生物信息学分析任务的模块组成：

- **进化分析**: evoldist, evolknow, evoltre, evoltrepipline
- **序列处理**: fastadumper, fastatools, multiseq, webmsaoperator
- **系统发育树工具**: treebuilder, evolview, treenodecoll, treetanglegram
- **数据可视化**: heatmap, chorddiagram, vennplot, skeletonscatter, sankeyplot
- **基因组分析**: genome, gff3opr, bedmerger
- **数据处理**: tablecuration, tableleftjoin, stringsetoperator

## 贡献指南

我们欢迎对eGPS 2.1项目的贡献。如果您有兴趣参与贡献，请遵循以下步骤：

1. Fork本仓库
2. 创建新分支 (`git checkout -b feature/YourFeature`)
3. 提交您的更改 (`git commit -am 'Add some feature'`)
4. 推送到分支 (`git push origin feature/YourFeature`)
5. 创建新的Pull Request

更多信息请阅读我们的[贡献指南](CONTRIBUTING.md)。

## 在IntelliJ IDEA中运行

要在IntelliJ IDEA中运行eGPS 2.1项目，请按照以下步骤操作：

1. 打开IntelliJ IDEA
2. 从欢迎界面选择"Open"或通过"File" -> "Open"菜单
3. 导航到eGPS2.1项目目录并选择根文件夹
4. IntelliJ IDEA应自动检测Maven项目结构
5. 等待IntelliJ IDEA导入Maven依赖项
6. 运行项目：
   - 导航到您想要运行的主类或模块
   - 右键单击类并选择"Run"或使用装订线中的绿色播放按钮
   - 或者，您可以通过"Run" -> "Edit Configurations"创建运行配置，并添加新的Application配置

注意：请确保您已在IntelliJ IDEA中配置了JDK 21或更高版本，路径为"File" -> "Project Structure" -> "Project" -> "Project SDK"。

## Maven配置

eGPS 2.1项目使用Maven进行依赖管理和构建自动化。主要的Maven配置详情如下：

- **Java版本**：项目需要Java 21或更高版本（在父级[pom.xml](pom.xml)中配置）
- **多模块结构**：项目遵循多模块结构，包含以下模块：
  - `parent`：根模块，包含共享配置
  - `mainframe`：主应用程序框架：注意此项目闭源
  - `allmodules`：包含所有分析模块
  - `ydl.lab.utils`：工具类和共享功能
  - `application.example`：专门用于eGPS 2.1 来进行分析的模块

- **依赖项**：
  - 依赖项在父级[pom.xml](pom.xml)文件中管理
  - 一些依赖项使用`system`作用域来引用[lib/](lib/)目录中的本地JAR文件
  - 主要依赖项包括：Apache Commons、BioJava组件、FlatLaf外观和XChart可视化库

- **资源管理**：
  - 配置了`src/main/java`中的资源包含在构建中（除了`.java`文件）
  - 这允许源代码目录中的XML和其他资源文件被正确打包

从命令行构建项目：
```bash
mvn clean install
```

这将按正确顺序构建所有模块并创建必要的JAR文件。

## 许可证

该项目采用Apache License 2.0授权 - 详情请见[LICENSE](LICENSE)文件。

## 致谢

- 感谢所有为该项目开发做出贡献的人员
- 特别感谢eGPS技术背后的研究团队