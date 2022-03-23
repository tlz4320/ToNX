package cn.treeh.ToNX.Exception;

public class FormatException extends Exception{
    String error, file, line;
    public FormatException(String file, String line, String error){
        this.error = error;
        this.line = line;
        this.file = file;
    }
    @Override
    public void printStackTrace() {
        System.err.println("file read error: " + error);
        System.err.println("file: " + file);
        System.err.println("line: " + line);
    }
}
