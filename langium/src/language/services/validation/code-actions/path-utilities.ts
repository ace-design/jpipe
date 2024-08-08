import { URI } from "langium";

abstract class Path{
    abstract components: Array<string>;
    abstract separator: "\\" | "/";
    abstract type: "Relative" | "Absolute";

    //Determines if an object is a FilePath
    public static isPath(value: any): value is Path{
        if(!(value && typeof value === 'object' && !Array.isArray(value))) return false;
        if(!Object.keys(value).includes("components")) return false;
        if(!Object.keys(value).includes("separator")) return false;
        if(!Object.keys(value).includes("type")) return false;
        
        if(!Array.isArray(value.components)) return false;
        if(!(value.type === "\\" || value.type === "/")) return false;
        if(!(value.type === "Relative" || value.type === "Absolute")) return false;

        return true;
    }

    public abstract toString(): string;

    // helper function for accesing purposes
    shift(): string | undefined{
        return this.components.shift();
    }

    //helper function for accesing purposes
    unshift(s: string): void{
        this.components.unshift(s);
    }
    
    // helper function for accesing purposes
    pop(): string | undefined{
        return this.components.pop();
    }

    //helper function for accesing purposes
    push(s: string): void{
        this.components.push(s);
    }

}

export class RelativePath extends Path{
    override components: string[];
    override separator: "\\" | "/";
    override type: "Relative" | "Absolute";
    
    constructor(home: FilePath, dest: FilePath){
        super();
        this.type = "Relative";

        if(home.separator === "/"){
            if(home.components.length === 1){
                dest.unshift(".");
            }else{
                for(let i = 0 ; i < (home.components.length - 1) ; i++){
                    dest.unshift("..");
                }
            }
        }
        
        this.components = dest.components;
        this.separator = dest.separator;
    }
    //Determines if an object is a FilePath
    public static isRelativePath(value: any): value is RelativePath{
        if(Path.isPath(value) && value.type === "Relative"){
            return true;
        }else{
            return false;
        }
    }

    public override toString(): string {
        let path = "";

        for(let i = 0 ; i < (this.components.length - 1) ; i++){
            path = path + this.components.at(i) + this.separator;
        }
        
        path = path + this.components.at(this.components.length - 1);

        return path;
    }
}

//Class to manage file paths (making file paths relative/absolute)
export class FilePath extends Path{
    override type: "Relative" | "Absolute";
    override components: Array<string>;
    override separator: "\\" | "/";

    public constructor(path: string);
    public constructor(path: URI);
    public constructor(path: string | URI){
        super();

        this.type = "Absolute"

        if(typeof path === "string"){
            if (path.includes("\\")){
                this.separator = "\\";
                this.components = path.split("\\");
            }else{
                this.separator = "/";
                this.components = path.split("/");
                this.components.shift(); //necessary because linux paths start with /
            }
        }else{
            let file_path = new FilePath(path.path);

            this.components = file_path.components;
            this.separator = file_path.separator;
        }
    }

    
    //Determines if an object is a FilePath
    public static isFilePath(value: any): value is FilePath{
        if(Path.isPath(value) && value.type === "Absolute"){
            return true;
        }else{
            return false;
        }
    }
    
    //Function to find the relative path to a file, using the current file path as home
    public getRelativePathTo(dest: string): RelativePath;
    public getRelativePathTo(dest: FilePath): RelativePath;
    public getRelativePathTo(dest: string | FilePath): RelativePath{
        if(typeof dest !== "string"){
            let reduced_path = this.reduceComponents(new FilePath(this.toString()), dest);

            return new RelativePath(reduced_path.home, reduced_path.dest);

        }else{
            return this.getRelativePathTo(new FilePath(dest));
        }
    }

    //helper function to remove all same components at the start of 2 file paths and return their reduced forms
    private reduceComponents(home: FilePath, dest: FilePath): {home: FilePath, dest: FilePath}{
        let home_first_element = home.shift();
        let dest_first_element = dest.shift();

        while(home_first_element === dest_first_element && (home_first_element !== undefined && dest_first_element !== undefined)){
            home_first_element = home.shift();
            dest_first_element = dest.shift();
        }
     
        if(home_first_element && dest_first_element){
            home.unshift(home_first_element);
            dest.unshift(dest_first_element);
        }else{
            throw new Error("Element is within scope");
        }
    
        return {
            home: home,
            dest: dest
        };
    }

    //function to convert a file path to a string
    public toString(): string{
        let path = "";

        for(let i = 0 ; i < (this.components.length - 1) ; i++){
            path = path + this.components.at(i) + this.separator;
        }
        
        path = path + this.components.at(this.components.length - 1);

        if(this.separator === "/"){
            path = "/" + path;
        }


        return path;
    }
}