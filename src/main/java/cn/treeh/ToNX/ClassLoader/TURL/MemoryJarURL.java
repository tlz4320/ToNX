package cn.treeh.ToNX.ClassLoader.TURL;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MemoryJarURL {
    private class LoadMemoryHandler extends URLStreamHandler {
        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            if (!u.getProtocol().equals("memory")) {
                throw new IOException("Cannot handle protocol: " + u.getProtocol());
            }
            return new URLConnection(u) {

                private byte[] data = null;
                @Override
                public void connect() throws IOException {
                    initDataIfNeeded();
                    checkDataAvailability();
                    // Protected field from superclass
                    connected = true;
                }

                @Override
                public long getContentLengthLong(){
                    try {
                        initDataIfNeeded();
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                    if (data == null)
                        return 0;
                    return data.length;
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    initDataIfNeeded();
                    checkDataAvailability();

                    return new ByteArrayInputStream(data);
                }

                private void initDataIfNeeded() throws IOException {
                    data = contents.get(u);
                }

                private void checkDataAvailability() throws IOException {
                    if (data == null)
                        throw new IOException("In-memory data cannot be found for: " + u.getPath());
                }

            };
        }
    }
    private final Map<URL, byte[]> contents = new HashMap<>();
    private final URLStreamHandler handler = new LoadMemoryHandler();

    private static MemoryJarURL instance = null;

    public static synchronized MemoryJarURL getInstance() {
        if (instance == null)
            instance = new MemoryJarURL();
        return instance;
    }

    private MemoryJarURL() {

    }

    public URL build(String path, String data) throws IOException{
        try {
            return build(path, data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
    public URL build(String path, byte[] data) throws IOException {
        try {
            URL url = new URL("memory", "", -1, path, handler);
            contents.put(url, data);
            ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(data));
            ZipEntry entry;
            byte[] tmp;
            while((entry = zip.getNextEntry()) != null){
                if(entry.isDirectory())
                    continue;
                tmp = zip.readAllBytes();
                contents.put(new URL(url, entry.getName()), tmp);
            }
            return url;
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
