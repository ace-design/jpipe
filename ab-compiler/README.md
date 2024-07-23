# jPipe Action-Based Compiler

- Author: Sébastien Mosser
- Version: 2024.2

## How to use

TBD

## How to contribute

### Compiling the compiler

```
$ mvn clean package
```

### Configuring checkstyle

The project adheres to Google's recommendations, automatically enforced by CheckStyle. We relaxed two practices, related to code indentation and Javadoc formatting. These are automatically enforced by Maven when building the project. 

To configure your IDE to reflect these practices, use the provided local configuration file (`./src/main/resources/checkstyle/google_check.xml`)

