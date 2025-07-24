[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[English](README.md) | [简体中文](README.zh.md)

# eGPS 2.1

Source code of eGPS 2.1 - an advanced bioinformatics analysis platform

## Table of Contents

- [About](#about)
- [Features](#features)
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Modules](#modules)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## About

eGPS (enhanced GPS) 2.1 is an advanced bioinformatics analysis platform that provides various tools for evolutionary analysis, sequence processing, phylogenetic tree construction and visualization. Unlike traditional GPS systems, eGPS 2.1 focuses on biological data analysis and offers a comprehensive set of modules for researchers in the field of evolutionary biology and genomics.

This repository contains the source code for the eGPS 2.1 system, which is built using Java and requires Java 21 to run.

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

List of software, libraries, or tools that need to be installed before using this project:
- Java Development Kit (JDK) 21 or higher
- Apache Maven 3.6 or higher
- Git

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

Please read our [Contributing Guidelines](CONTRIBUTING.md) for more information.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Thanks to all contributors who have helped with the development of this project
- Special recognition to the research team behind the eGPS technology