package cn.tobinsc.ToNX.util;

import cn.tobinsc.ToNX.Annotation.Arg;
import cn.tobinsc.ToNX.O;
import org.apache.commons.cli.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static cn.tobinsc.ToNX.util.ReflectionUtil.setField;
import static cn.tobinsc.ToNX.util.ReflectionUtil.cast;

public class ArgUtil {
    private Options options = new Options();
    private CommandLineParser parser = new DefaultParser();
    public void parse(String[] argv, Object Configure, int level) {
        Field[] fields = Configure.getClass().getFields();
        Arg annotation;
        for(Field field : fields){
            if(field.isAnnotationPresent(Arg.class)){
                annotation = field.getAnnotation(Arg.class);
                Option option = new Option(annotation.arg(),
                        annotation.longarg().length() == 0 ? annotation.arg() : annotation.longarg(),
                        annotation.hasArg(),
                        annotation.summary());
                option.setRequired(annotation.needed());
                options.addOption(option);
            }
        }
        try {
            CommandLine commandLine = parser.parse(options, argv);
            String arg;
            for(Field field : fields) {
                if(field.isAnnotationPresent(Arg.class)){
                    annotation = field.getAnnotation(Arg.class);
                    arg = commandLine.getOptionValue(annotation.arg());
                    if((annotation.needed() || (annotation.level() & level) != 0) && arg == null)
                        throw new ParseException("Error: need " + annotation.arg() + "\n");
                    if(arg == null)
                        setField(Configure, field, cast(field.getType(),annotation.val()));
                    else
                        setField(Configure, field, cast(field.getType(), arg));
                }
            }
        }catch (ParseException e){
            O.ptln(options.toString());
            throw new RuntimeException();
        }
    }
    public void parse(String[] argv, Object Configure)  {
        parse(argv, Configure, Integer.MAX_VALUE);
    }

}
