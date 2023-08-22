package cn.treeh.ToNX.Output;

public class OutputCreator {
    public static OutputFile openOutput(String file, boolean binary){
        if(binary)
            return new BinaryOutputFile(file);
        return new DefaultOutputFile(file);
    }
}
