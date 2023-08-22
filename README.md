# ToNX：A Personal Jar package toolset

This toolset,  which just make some easy function depend on other famous and great project, is just designed for myself.

## ArgUtil

ArgUtil is designed for make java program can decode the arguments automatically.

感谢apache.cli提供的参数解析，我不过是在他的基础上套了一个壳罢了。

### usage:

> 注意：我使用Arg注释作为判断变量是否需要从传入参数中解析
>
> notion: I use the annotation(Arg) to order which field want to be decode from argv.
>
> 参数列表：
>
> arg(必须)：传入参数的短格式
>
> longarg(非必须)：传入参数的长格式
>
> needed(默认为false)：是否必须
>
> val(非必须)：默认值
>
> level(若needed为false时才会生效)：比needed更加灵活，能够根据level判断该参数是够是必须的
>
> description(非必须)：参数说明
>
> hasArg(必须)：后面是否携带值
>
> 
>
> parameter of annotation Arg:
>
> arg: min name of argument 
>
> longarg: long name of argument 
>
> needed: whether have to be input to program
>
> val: default value(after all argument is contribute by string, so it won't be something complex. In this toolset only support int double float String boolean)
>
> level: sometimes needed is not enough just because program have different function, and in different function need different arguments. Therefore I design level to specify whether in this function the argument is needed. (every bit of int means one level)
>
> If the decode find some exception it will output the parameter description which is made up automatically by toolset. If it is not enough, user can just input a new Options which OverWrited the toString function when create new instance of ArgUtils.
>
> 

#### Example

```java
import cn.treeh.ToNX.O;
import cn.treeh.ToNX.util.ArgUtil;

public class MyParameter extends ArgUtil {
    @Arg(arg = "a", needed = true, hasArg = true, val = "1")//default value = 1
    public int par_1;//-a
    @Arg(arg = "b", hasArg = true, level = 1)//level = 1
    public int par_2;//-b
    @Arg(arg = "c", longarg = "cc", hasArg = false, description = "this is par_3")//also longarg is support
    public double par_3;//-c | --cc

    static public void main(String[] argv) {
        MyParameter mp = new MyParameter();
        O.ptln(mp.toHelpString());//print autogenerate help
        mp.parse(argv, 1);//1 means fields which level=1 are needed
        O.ptln(mp.toString()); //print total parameters list
        O.ptln(mp.par_2);
    }
}
```

> recommend: Use a static field to restore arguments, so that arguments can be accessible in every part of program.