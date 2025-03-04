//interface which defines an abstract configuration
export interface AbstractConfiguration<T> {
    readonly configuration: Configuration<T>;

    //function to change the configuration when needed
    update(): T;

    //function to return the configuration when necessary
    getConfiguration(): T;
}

export enum ConfigKey{
    LOGLEVEL = "jpipe.logLevel",
    JAVAVERSION = "jpipe.setJavaVersion",
    JARFILE = "jpipe.jarFile",
    DEVMODE = "jpipe.developerMode",
    CHECKGRAPHVIZ = "jpipe.checkGraphviz",
    CHECKJAVA = "jpipe.checkJava"
}

export type Configuration<T> = {
    key: ConfigKey,
    default_value: T
}