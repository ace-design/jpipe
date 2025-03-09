# jPipe Action-Based Compiler

- Author: Sébastien Mosser
- Version: 2024.2

### How to run the compiler?

```
$ java -jar abc-jpipe.jar --help
```

One should [read the documentation](https://github.com/ace-design/jpipe/wiki), or consider following one of our tutorials.

To run the compiler in developer mode

```
 $ mvn -q exec:java -Dexec.mainClass="ca.mcscert.jpipe.Main" -Dexec.args="-h"
```

### How to contribute?

Before contributing, you might want to read some [_Architectural Decisions_](adr.md) recorded as Y-statements, providing some explanations on the global architecture.

#### Compiling the compiler

It is highly recommended to use the _released_ versions (e.g., through the VS Code marketplace or HomeBrew) rather than compiling from source. Use at your own risk.

```
$ mvn clean install
```

#### Configuring checkstyle

The project adheres to Google's recommendations, automatically enforced by CheckStyle. These 
are automatically enforced by Maven when building the project. 
Violating them is ok at _package_ time (e.g., while developing), but is considered an error at _install_ time (e.g., when releasing).

To configure your IDE to reflect these practices, use the provided local configuration file (`./src/main/resources/checkstyle/google_checks_intelliJ.xml`)

