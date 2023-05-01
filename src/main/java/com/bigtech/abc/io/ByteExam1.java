package com.bigtech.abc.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ByteExam1 {
    public static void main(String[] args){
        long startTime = System.currentTimeMillis();

        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream("src/main/java/com/bigtech/abc/io/ByteExam1.java");
            fos = new FileOutputStream("byte.txt");

            int readData = -1;
            while((readData = fis.read())!= -1){
                fos.write(readData);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try {
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        long endTime = System.currentTimeMillis();

        System.out.println(endTime - startTime);
    }
}
