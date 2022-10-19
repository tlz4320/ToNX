package cn.treeh.ToNX.Tools;

import java.io.FileWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool<I, O>{
    ExecutorService service;
    Hashtable<Integer, O> outputs;
    AtomicInteger thread_id;
    public abstract class Processor extends Thread {
        private int id;
        private O result;
        private I input;
        @Override
        public void run() {
            try {
                result = call(getInput());
                outputs.put(id, result);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        public abstract O call(I input);
        public I getInput(){
            return input;
        }
        public O getResult(){
            return result;
        }
        abstract public void write(FileWriter writer, O obj);
    }

    boolean order;
    Processor processor;
    FileWriter writer;
    boolean await_flag;

    public ThreadPool(int threads, Processor p, FileWriter writer){
        this(threads, false, p, writer);
    }
    private void outputThread(){
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    int id = 1;
                    while (true) {
                        if (await_flag && id > thread_id.get())
                            break;
                        if (order) {
                            if (outputs.contains(id)) {
                                processor.write(writer, outputs.get(id));
                                outputs.remove(id);
                                id++;
                            } else
                                Thread.sleep(1000);
                        } else {
                            if(outputs.size() > 0) {
                                for(Iterator<Map.Entry<Integer, O>> iterator = outputs.entrySet().iterator();
                                    iterator.hasNext(); ) {
                                    processor.write(writer, iterator.next().getValue());
                                    iterator.remove();
                                    id++;
                                }
                            }
                            else{
                                Thread.sleep(1000);
                            }
                        }
                    }
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public ThreadPool(int threads, boolean order, Processor p, FileWriter writer){
        this.order = order;
        this.processor = p;
        this.writer = writer;
        service = Executors.newFixedThreadPool(threads);
        await_flag = false;

        outputs = new Hashtable<>();
        thread_id = new AtomicInteger(0);
    }
    public void submit(I input){
        try {
            Processor tmpP = processor.getClass().getConstructor().newInstance();
            tmpP.input = input;
            tmpP.id = thread_id.addAndGet(1);
            service.submit(tmpP);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void await(){
        try {
            await_flag = true;
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
