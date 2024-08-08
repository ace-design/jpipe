import { URI } from "langium";

//Class to manage file paths (making file paths relative/absolute)
export class FilePath{
    private components: Array<string>;
    private separator: "\\" | "/";

    public constructor(path: string);
    public constructor(path: URI);
    public constructor(path: string | URI){
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
        if(!(value && typeof value === 'object' && !Array.isArray(value))) return false;
        if(!Object.keys(value).includes("components")) return false;
        if(!Object.keys(value).includes("separator")) return false;

        if(!Object.keys(value).includes("getRelativePathTo")) return false;
        if(!Object.keys(value).includes("reduceComponents")) return false;
        if(!Object.keys(value).includes("assembleRelativePath")) return false;
        if(!Object.keys(value).includes("shift")) return false;
        if(!Object.keys(value).includes("unshift")) return false;
        if(!Object.keys(value).includes("toString")) return false;

        return true;
    }
    
    //Function to find the relative path to a file, using the current file path as home
    public getRelativePathTo(dest: string): FilePath
    public getRelativePathTo(dest: URI): FilePath;
    public getRelativePathTo(dest: FilePath): FilePath;
    public getRelativePathTo(dest: string | URI | FilePath): FilePath{
        if(FilePath.isFilePath(dest)){    
            let reduced_path = this.reduceComponents(new FilePath(this.toString()), dest);

            return this.assembleRelativePath(reduced_path.home, reduced_path.dest);

        }else if(typeof dest === "string"){
            return this.getRelativePathTo(new FilePath(dest));
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

    //helper function to assemble a relative path given 2 reduced paths
    private assembleRelativePath(home: FilePath, dest: FilePath): FilePath{
        if(this.separator === "/"){
            if(home.components.length === 1){
                dest.unshift(".");
            }else{
                for(let i = 0 ; i < home.components.length ; i++){
                    dest.unshift("..");
                }
            }
        }
        
        return dest;
    }

    // helper function for accesing purposes
    private shift(): string | undefined{
        return this.components.shift();
    }

    //helper function for accesing purposes
    private unshift(s: string): void{
        this.components.unshift(s);
    }
    
    //function to convert a file path to a string
    public toString(): string{
        let path = "";

        for(let i = 0 ; i < (this.components.length - 1) ; i++){
            path = path + this.components.at(i) + this.separator;
        }
        
        path = path + this.components.at(this.components.length - 1);

        if(!(this.components.at(0) === "." || this.components.at(0) === "..")){
            if(this.separator === "/"){
                path = "/" + path;
            }
        }

        return path;
    }
}