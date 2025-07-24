[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[English](README.md) | [简体中文](README.zh.md)

# eGPS 2.1

Source code of eGPS 2.1 - an advanced bioinformatics analysis platform

The official website of eGPS 2.1 is available at http://www.egps-software.org/ 

## Table of Contents

- [About](#about)
- [Features](#features)
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Modules](#modules)
- [Contributing](#contributing)
- [Running in IntelliJ IDEA](#running-in-intellij-idea)
- [Maven Configuration](#maven-configuration)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## About and history

eGPS (enhanced GPS) 2.1 is an advanced bioinformatics analysis platform that provides various tools for evolutionary analysis, sequence processing, phylogenetic tree construction and visualization. Unlike traditional GPS systems, eGPS 2.1 focuses on biological data analysis and offers a comprehensive set of modules for researchers in the field of evolutionary biology and genomics.

This repository contains the source code for the eGPS 2.1 system, which is built using Java and requires Java 21 to run.

This platform is designed to facilitate the analysis of biological data and to provide a comprehensive set of tools for researchers in the field of evolutionary biology and genomics.

This project is officially started on the 2017.09 and first published in 2019.06.

The repo. owner yudalang3 is the owner, starter and maintainer of this project.

## Features

- Evolutionary analysis tools
- Sequence processing and manipulation
- Phylogenetic tree construction and visualization
- Data visualization modules (heatmap, chord diagram, venn plot, etc.)
- Modular design with over 60 specialized analysis modules
- Cross-platform compatibility
- Extensible architecture

## Getting Started

These instructions will get you a copy of the project up and running on your local machine.

### Prerequisites

For users (GUI users or command line users):

For developers (module developers, mainframe developers and plug-ins developers): Please see [Contributing](#contributing)


### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/eGPS2.1.git
   ```
2. Navigate to the project directory:
   ```bash
   cd eGPS2.1
   ```
3. Build the project:
   ```bash
   mvn clean install
   ```

## Usage

After building the project, you can run the main application with:
```bash
java -jar target/eGPS2.1.jar
```

Detailed usage instructions for individual modules can be found in their respective documentation.

## Modules

eGPS 2.1 consists of over 60 specialized modules for various bioinformatics analysis tasks:

- **Evolutionary Analysis**: evoldist, evolknow, evoltre, evoltrepipline
- **Sequence Processing**: fastadumper, fastatools, multiseq, webmsaoperator
- **Phylogenetic Tree Tools**: treebuilder, evolview, treenodecoll, treetanglegram
- **Data Visualization**: heatmap, chorddiagram, vennplot, skeletonscatter, sankeyplot
- **Genome Analysis**: genome, gff3opr, bedmerger
- **Data Processing**: tablecuration, tableleftjoin, stringsetoperator

## Contributing

We welcome contributions to the eGPS 2.1 project. If you're interested in contributing, please follow these steps:

1. Fork the repository
2. Create a new branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Create a new Pull Request

List of software, libraries, or tools that need to be installed before using this project:
- Java Development Kit (JDK) 21 or higher
- Apache Maven 3.6 or higher
- Git

Please read our [Contributing Guidelines](CONTRIBUTING.md) for more information.

## Running in IntelliJ IDEA

To run the eGPS 2.1 project in IntelliJ IDEA:

1. Open IntelliJ IDEA
2. Select "Open" from the welcome screen or go to "File" -> "Open"
3. Navigate to the eGPS2.1 project directory and select the root folder
4. IntelliJ IDEA should automatically detect the Maven project structure
5. Wait for IntelliJ IDEA to import the Maven dependencies
6. To run the project:
   - Navigate to the main class or module you want to run
   - Right-click on the class and select "Run" or use the green play button in the gutter
   - Alternatively, you can create a run configuration by going to "Run" -> "Edit Configurations" and adding a new Application configuration

Note: Make sure you have configured the JDK 21 or higher in IntelliJ IDEA under "File" -> "Project Structure" -> "Project" -> "Project SDK".

## Maven Configuration

The eGPS 2.1 project uses Maven for dependency management and build automation. Key Maven configuration details:

- **Java Version**: The project requires Java 21 or higher (configured in the parent [pom.xml](pom.xml))
- **Multi-module Structure**: The project follows a multi-module structure with the following modules:
  - `parent`: The root module containing shared configuration
  - `mainframe`: The main application framework: Note this is closed-source software
  - `allmodules`: Contains all analysis modules
  - `ydl.lab.utils`: Utility classes and shared functionality
  - `application.example`: Specialized modules for Application of eGPS 2.1

- **Dependencies**: 
  - Dependencies are managed in the parent [pom.xml](pom.xml) file
  - Some dependencies use `system` scope to reference local JAR files in the [lib/](lib/) directory
  - Key dependencies include: Apache Commons, BioJava components, FlatLaf look and feel, and XChart for visualization

- **Resource Management**:
  - Resources in `src/main/java` are configured to be included in the build (except for `.java` files)
  - This allows XML and other resource files in the source directories to be properly packaged

To build the project from the command line:
```bash
mvn clean install
```

This will build all modules in the correct order and create the necessary JAR files.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Thanks to all contributors who have helped with the development of this project
- Special recognition to the research team behind the eGPS technology