# jPipe - Justified Pipelines

![tool logo](./logo.png)

The jPipe environment is a experimental framework used to create pipelines that are justified by design. 
It's an extension of so-called Justification Diagrams to support the notion of pipelines.

## General Information
- Version: 23.07.03
- Contributors:
  - Sébastien Mosser, McMaster University (Hamilton, Canada)
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

Soon, you'll find some help about using the compiler by invoking `java -jar jpipe.jar --help`. In the meanwhile... 
be patient :)

In the MVP version, running the compiler on a file will produce one PNG file per justification diagram, in the same 
directory. Assume the file `my_input.jd` contains two diagrams, names "J1" and "J2".

```
$ java -jar jpipe.jar my_input.jd
```

Will produce two png files: `my_input_J1.png` and `my_input_J2.png`


## How to contribute?

Feel free to fork this repository and send a pull request.

