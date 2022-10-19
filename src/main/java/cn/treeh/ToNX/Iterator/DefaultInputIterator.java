package cn.treeh.ToNX.Iterator;

import cn.treeh.ToNX.Exception.FormatException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class DefaultInputIterator implements InputIterator{
    String file = null;
    BufferedReader reader = null;
    String sep = null;
    int cols = 0;
    String thisLine = null;
    String[] formerLine = null;
    public String getThisLine() {
        return thisLine;
    }
    public String readLine(){
        if(!hasNext())
            return null;
        next();
        return thisLine;
    }
    public void open(String file, String s, int cols) throws IOException{
        this.file = file;
        sep = s;
        this.cols = cols;
        reader = new BufferedReader(new FileReader(file));
    }
    public DefaultInputIterator(String file, String s, int cols) throws IOException {
        this.open(file, s, cols);
    }
    @Override
    public boolean hasNext() {
        String line = "";
        try {
            if (formerLine != null)
                return true;
            line = reader.readLine();
            if (line == null) {
                reader.close();//如果没有意义了就关闭
                //都空了还使用 本来就有问题 肯定是逻辑错误  那就直接结束进程
                return false;
            }
            formerLine = line.split(sep);
            if (cols == formerLine.length)
                return true;
            if (cols > 0 && formerLine.length > 1)
                throw new FormatException(file, line, "cols of file is not equal to cols you need!");
            thisLine = line;
            return line.length() != 0;
            //这里就意味着不需要列检查 只有当line自身是空的时候是错的
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FormatException e){
            e.printStackTrace();//这里是格式错误  就不停止了  说明是输入文件有问题  给用户一个容错的机会
        }
        return false;
    }

    @Override
    public String[] next() {
        String[] res = null;
        if(hasNext())
            res = formerLine;
        formerLine = null;
        return res;
    }
    public void close() throws IOException{
        reader.close();
    }
}
