//Wrapper class for file system in case functionality is later depreciated
export class JpipeFileSystemManager{
    private fs: any;

    constructor(fs: any){
        this.fs = fs;
    }

    //function to verify file path existence
    public pathExists(file_path: string): boolean{
        return this.fs.existsSync(file_path);
    }
}