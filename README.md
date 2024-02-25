# jPipe - Justified Pipelines

![tool logo](./logo.png)

The jPipe environment is a experimental framework used to create pipelines that are justified by design. 
It's an extension of so-called Justification Diagrams to support the notion of pipelines.

## General Information
- Version: 24.02
- Contributors:
  - Sébastien Mosser, McMaster University (Hamilton, Canada)
  - Nirmal Chaudhari, McMaster University (Hamilton, Canada)
  - Corinne Pulgar, École de Technologie Supérieure (Montréal, Canada)
  - Deesha Patel, McMaster University (Hamilton, Canada)
  - Aaron Loh, McMaster University (Hamilton, Canada)
  - Jean-Michel Bruel, Université de Toulouse (Toulouse, France)
  - Mireille Blay Fornarino, Université Côte d'Azur (Sophia Antipolis, France)
  
## How to use?

### Compiling from sources

Maven is used as dependency manager and build environment. Simply clone the repository and ask Maven to `install` 
the software

```
~ $ git clone git@github.com:ace-design/jpipe.git
~ $ cd jpipe
~/jpipe $ mvn clean install
```

After installation, you can find the `jpipe.jar` turn-key artefact at the root of the repository

### Downloading a pre-compiled version

Go to the [Releases page](https://github.com/ace-design/jpipe/releases), and download the latest precompiled 
version of the jPipe compiler.

### Using the command-line interface

You'll find a complete description of the compiler's options by invoking `java -jar jpipe.jar --help`. 

Here is a list of the main options for end-users:

- `-i`, `--input`: Specify the filename to be used as input (default is `stdin`)
- `-d`, `--diagram`: Specify the name of the diagram to compile.
    - If there is only one diagram in the compilation unit, it will default to this one.
- `-o`, `--output`: Specify the output filename to use as output (default is `stdout`)
- `-f`, `--format`: Specify the output format (PNG or SVG). Default is PNG.
- `--all`: Flush all diagrams in the directory provided as argument.

## How to contribute?

Found a bug, or want to add a cool feature? Feel free to fork this repository and send a pull request. 

If you're interested in contributing to the research effort related to jPipe, feel free to contact the PI: [Dr. Sébastien Mosser](mossers@mcmaster.ca). We do have undergrad summer internships available to contribute to the compiler, as well as MASc and PhD positions in Software Engineering at Mac.

