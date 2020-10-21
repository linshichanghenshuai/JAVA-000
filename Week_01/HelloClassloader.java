package com.lsc.geek.loader;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class HelloClassloader extends ClassLoader{
    /**
     * class文件的路径
     */
    private String path;

    public HelloClassloader(String path) {
        this.path = path;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        String classPath = path + name + ".class";

        try (InputStream in = new FileInputStream(classPath)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int i = 0;
            while ((i = in.read()) != -1) {
                out.write(i);
            }
            byte[] byteArray = out.toByteArray();
            return defineClass(byteArray, 0, byteArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

}

