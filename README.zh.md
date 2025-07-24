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

## 许可证

该项目采用Apache License 2.0授权 - 详情请见[LICENSE](LICENSE)文件。

## 致谢

- 感谢所有为该项目开发做出贡献的人员
- 特别感谢eGPS技术背后的研究团队