import { URI } from "langium";

//Stores a Path to a file
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

        return true;
    }

    //Determines if an object is a RelativePath
    public static isRelativePath(value: any): value is RelativePath{
        if(Path.isPath(value) && value.type === "Relative"){
            return true;
        }else{
            return false;
        }
    }

    //Determines if an object is an AbsolutePath
    public static isAbsolutePath(value: any): value is AbsolutePath{
        if(Path.isPath(value) && value.type === "Absolute"){
            return true;
        }else{
            return false;
        }
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

//Stores a relative path to a file
export class RelativePath extends Path{
    override components!: string[];
    override separator!: "\\" | "/";
    override type: "Relative" | "Absolute";
    
    constructor(path: string);
    constructor(home: AbsolutePath, dest: AbsolutePath);
    constructor(path: string | AbsolutePath, dest?: AbsolutePath){
        super();
        this.type = "Relative";

        if(typeof path === "string"){
            if(path.includes("\\")){
                this.separator = "\\";
                this.components = path.split("\\");
            }else{
                this.separator = "/";
                this.components = path.split("/");
            }
        }else if(dest){
            if(path.separator === "/"){
                if(path.components.length === 1){
                    dest.unshift(".");
                }else{
                    for(let i = 0 ; i < (path.components.length - 1) ; i++){
                        dest.unshift("..");
                    }
                }
            }
            
            this.components = dest.components;
            this.separator = dest.separator;
        }

    }

    public toAbsolutePath(home: AbsolutePath): AbsolutePath{
        let absolute_path: AbsolutePath;
        let this_path = new AbsolutePath(this.toString());

        home.pop();
        
        let first_element = this_path.shift();

        if(first_element === "."){
            absolute_path = new AbsolutePath(home.toString());
            
            first_element = this_path.shift();
        }else{
            while(first_element === ".."){
                home.pop();
                first_element = this_path.shift();
            }

            absolute_path = new AbsolutePath(home.toString());
        }

        while(first_element !== undefined){
            absolute_path.push(first_element);
            first_element = this_path.shift();
        }
    
        return absolute_path;
    }

    //converts a relative path to a string
    public override toString(): string {
        let path = "";

        for(let i = 0 ; i < (this.components.length - 1) ; i++){
            path = path + this.components.at(i) + this.separator;
        }
        
        path = path + this.components.at(this.components.length - 1);

        return path;
    }
}

//Stores an absolute path to a file
export class AbsolutePath extends Path{
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
            let file_path = new AbsolutePath(path.path);

            this.components = file_path.components;
            this.separator = file_path.separator;
        }
    }
    

    //Function to find the relative path to a file, using the current file path as home
    public getRelativePathTo(dest: string): RelativePath;
    public getRelativePathTo(dest: AbsolutePath): RelativePath;
    public getRelativePathTo(dest: string | AbsolutePath): RelativePath{
        if(AbsolutePath.isAbsolutePath(dest)){
            let reduced_path = this.reduceComponents(new AbsolutePath(this.toString()), dest);
            return new RelativePath(reduced_path.home, reduced_path.dest);

        }else{
            return this.getRelativePathTo(new AbsolutePath(dest));
        }
    }

    //helper function to remove all same components at the start of 2 file paths and return their reduced forms
    private reduceComponents(home: AbsolutePath, dest: AbsolutePath): {home: AbsolutePath, dest: AbsolutePath}{
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