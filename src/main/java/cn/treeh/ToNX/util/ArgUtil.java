package cn.treeh.ToNX.util;

import cn.treeh.ToNX.Annotation.Arg;
import cn.treeh.ToNX.Exception.ArgNeededException;
import cn.treeh.ToNX.Exception.ArgUnSupportException;
import org.apache.commons.cli.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static cn.treeh.ToNX.util.ReflectionUtil.setField;
import static cn.treeh.ToNX.util.ReflectionUtil.cast;

public abstract class ArgUtil {
    HashMap<Option, Field> Option_of_fields = new HashMap<>();
    HashMap<ArgNeededException, Integer> unSatisfiedLevel = new HashMap<>();
    int levels = 0;
    private Options options = new Options() {
        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder("This is argument description made up by ToNX\n");
            String typeName;
            for (Map.Entry<Option, Field> entry : Option_of_fields.entrySet()) {
                buf.append("argument: ");
                buf.append(entry.getKey().getOpt());
                buf.append("  long argument: ");
                buf.append(entry.getKey().getLongOpt());
                buf.append("  type: ");
                typeName = entry.getValue().getType().getTypeName();
                buf.append(typeName.substring(typeName.lastIndexOf('.') + 1));
                if (entry.getKey().getDescription().length() > 0) {
                    buf.append("  description: ");
                    buf.append(entry.getKey().getDescription());
                }
                buf.append("\n");
            }
            buf.append("if you need change this you could OverWrite the Options.toString");
            return buf.toString();
        }
    };
    int verbose = 0;

    public ArgUtil() {
        verbose = 1;
    }

    public ArgUtil(int verbose) {
        this.verbose = verbose;
    }

    //support for user to Overwrite the toString
    public ArgUtil(Options ops, int verbose) {
        options = ops;
        this.verbose = verbose;
    }

    public ArgUtil(Options ops) {
        options = ops;
    }

    private CommandLineParser parser = new DefaultParser();

    public void _parse(String[] argv, Object Configure) {
        Field[] fields = Configure.getClass().getFields();
        Arg annotation;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Arg.class)) {
                annotation = field.getAnnotation(Arg.class);
                Option option = new Option(annotation.arg(),
                        annotation.longarg().length() == 0 ? annotation.arg() : annotation.longarg(),
                        annotation.hasArg(),
                        annotation.description());
                option.setRequired(annotation.needed());
                Option_of_fields.put(option, field);
                options.addOption(option);
            }
        }
        try {
            CommandLine commandLine = parser.parse(options, argv);
            String arg;
            for (Field field : fields) {
                if (field.isAnnotationPresent(Arg.class)) {
                    annotation = field.getAnnotation(Arg.class);
                    if (!annotation.hasArg()) {
                        if (commandLine.hasOption(annotation.arg()))
                            setField(Configure, field, cast(field.getType(), "true"));
                        else
                            setField(Configure, field, cast(field.getType(), "false"));
                        continue;
                    }
                    arg = commandLine.getOptionValue(annotation.arg());
                    if (annotation.needed() && arg == null)
                        throw new ArgNeededException(annotation, field);
                    if (annotation.level() != 0) {
                        levels = (levels | annotation.level());
                        if (arg == null)
                            unSatisfiedLevel.put(new ArgNeededException(annotation, field), annotation.level());
                    }
                    if (arg == null)
                        setField(Configure, field, cast(field.getType(), annotation.val()));
                    else
                        setField(Configure, field, cast(field.getType(), arg));
                }
            }
        } catch (ArgUnSupportException e2) {
            if (verbose > 0)
                System.err.println(options.toString());
            e2.printStackTrace();
            throw new RuntimeException();
        } catch (ArgNeededException e3) {
            if (verbose > 0)
                System.err.println(options.toString());
            e3.printStackTrace();
            throw new RuntimeException();
        } catch (Exception e) {
            System.err.println(options.toString());
            throw new RuntimeException();
        }

    }

    public boolean parse(String[] argv, Object Configure) {
        _parse(argv, Configure);
        return check();
    }
    public boolean parse(String[] argv){
        return parse(argv, this);
    }
    public boolean parse(String[] argv, Object Configure, int level) {
        _parse(argv, Configure);
        try {
            for (Map.Entry<ArgNeededException, Integer> entry : unSatisfiedLevel.entrySet()) {
                if ((entry.getValue() & level) != 0)
                    throw entry.getKey();
            }
        } catch (ArgNeededException e3) {
            if (verbose > 0)
                System.err.println(options.toString());
            e3.printStackTrace();
            throw new RuntimeException();
        }
        return check();
    }

    public int parseLevel(String[] argv, Object Configure) {
        _parse(argv, Configure);
        for (Map.Entry<ArgNeededException, Integer> entry : unSatisfiedLevel.entrySet())
            levels = levels & (~entry.getValue());
        return levels;
    }

    public boolean check() {
        return true;
    }


    public String toString() {
        if (options != null)
            return options.toString();
        else
            return "";
    }
}
