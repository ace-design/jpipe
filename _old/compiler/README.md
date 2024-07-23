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