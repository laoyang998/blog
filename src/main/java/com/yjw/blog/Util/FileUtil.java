package com.yjw.blog.Util;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileUtil {

    public static void download(String fileName, HttpServletResponse res) throws IOException {
        OutputStream outputStream = res.getOutputStream();
        byte[] buff=new byte[1024];
        BufferedInputStream bis = null;
        bis=new BufferedInputStream(new FileInputStream(fileName));
        int i=bis.read(buff);
        while (i!=-1){
            outputStream.write(buff,0,buff.length);
            outputStream.flush();
            i=bis.read(buff);
        }
        bis.close();
    }
}
