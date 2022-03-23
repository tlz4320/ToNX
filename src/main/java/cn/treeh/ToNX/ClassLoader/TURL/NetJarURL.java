package cn.treeh.ToNX.ClassLoader.TURL;

import sun.net.www.ParseUtil;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class NetJarURL {
    URL root = null;
    private class LoadNetHandler extends URLStreamHandler {

        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            if (!u.getProtocol().equals("http")) {
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
                    if(contents.containsKey(u))
                        data = contents.get(u);
                    else{
                        URL tmp = new URL(u.getPath());
                        data = tmp.openConnection().getInputStream().readAllBytes();
                        contents.put(u, data);
                    }
                }

                private void checkDataAvailability() throws IOException {
                    if (data == null)
                        throw new IOException("Network data cannot be found for: " + u.getPath());
                }

            };
        }
    }
    private final Map<URL, byte[]> contents = new HashMap<>();
    private final URLStreamHandler handler = new LoadNetHandler();

    private static NetJarURL instance = null;

    public static synchronized NetJarURL getInstance() {
        if (instance == null)
            instance = new NetJarURL();
        return instance;
    }

    private NetJarURL() {

    }

    public URL build(String path, byte[] data, String host, int port) throws IOException {
        try {
            return new URL("http", host, port, path, handler);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
