# nand2tetris using Groovy

## Overview
This project is an implementation of the **Nand2Tetris** course concepts using **Groovy**. It provides tools and scripts for simulating logic gates, building a simple computer, and compiling assembly code into machine language. By implementing each stage of the computing stack from the ground up, this project demonstrates a deep understanding of computer architecture and software development.

## Authors
- **Ariel Moreshet**
- **Adir Magid**

## Why This Project is Awesome
This project is an exceptional demonstration of how a modern computer is built from first principles. By implementing a computer from NAND gates up to a working compiler, it showcases:
- **Hardware Simulation:** The creation of fundamental logic gates and arithmetic units.
- **Assembler:** Converts human-readable assembly language into binary machine code for execution.
- **Virtual Machine (VM) Translator:** Translates VM commands into assembly code, mimicking the behavior of a high-level language runtime.
- **Compiler:** Converts a high-level language into VM instructions, demonstrating real-world compiler design principles.
- **Deep System Understanding:** Mastery of both low-level and high-level programming paradigms.

This project is an incredible way to prove mastery over computer science fundamentals, and it reflects strong problem-solving skills, attention to detail, and the ability to build complex systems from scratch. It is particularly valuable for software engineering roles requiring knowledge of compilers, interpreters, and low-level programming.

## Features
- Implementation of basic logic gates in Groovy
- Assembler for the Hack computer
- Virtual machine (VM) translator
- High-level language compiler
- Supports running and testing components within an IntelliJ-based Groovy environment

## Requirements
To use this project, you need:
- **Java Development Kit (JDK) 8+**
- **Groovy 3.0+**
- **IntelliJ IDEA (Optional but recommended)**

## Installation
1. Clone this repository:
   ```sh
   git clone https://github.com/your-repo/nand2tetris-using-groovy.git
   ```
2. Navigate to the project directory:
   ```sh
   cd nand2tetris-using-groovy
   ```
3. Ensure you have **Groovy** installed. You can check by running:
   ```sh
   groovy --version
   ```
   If Groovy is not installed, download it from [Groovy's official website](https://groovy-lang.org/).

## Usage
To run the assembler, use:
```sh
groovy Assembler.groovy path/to/input.asm
```

To execute the VM translator:
```sh
groovy VMTranslator.groovy path/to/input.vm
```

For testing and debugging, you can use IntelliJ IDEA with Groovy support.

## Project Structure
```
/nand2tetris-using-groovy
├── src/                  # Main source code
│   ├── Assembler.groovy  # Translates Hack assembly to binary
│   ├── VMTranslator.groovy  # Translates VM code to assembly
│   ├── Compiler.groovy   # High-level language to VM translation
│   └── tests/            # Test cases for components
├── .idea/                # IntelliJ project settings
├── README.md             # Project documentation
└── LICENSE               # License information
```

## Contributing
Contributions are welcome! To contribute:
1. Fork the repository
2. Create a new branch (`git checkout -b feature-branch`)
3. Commit your changes (`git commit -m 'Add new feature'`)
4. Push to the branch (`git push origin feature-branch`)
5. Open a Pull Request

## License
This project is licensed under the MIT License - see the LICENSE file for details.

