//interface which defines an abstract configuration
export interface AbstractConfiguration<T> {

    //key corresponds to the schema of the configuration property in package.json
    readonly key: ConfigKey;
    //value at startup of configuration
    readonly default_value: T;

    //function to change the configuration when needed
    update(): void;

    //function to return the configuration when necessary
    getConfiguration(): T;
}

export enum ConfigKey{
    LOGLEVEL = "jpipe.logLevel",
    JARFILE = "jpipe.jarFile",
    DEVMODE = "jpipe.developerMode",
    CHECKGRAPHVIZ = "jpipe.checkGraphviz",
    CHECKJAVA = "jpipe.checkJava"
}