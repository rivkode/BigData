package com.bigtech.abc.data;


import java.util.concurrent.TimeUnit;

public class HelloSync {
    private static boolean stopRequested;
    public static void main(String[] args) throws InterruptedException {
        StringDisplay sd = new StringDisplay();
        MyThread[] mts = new MyThread[10];
        for (int i=0; i<mts.length; i++) {
            mts[i] = new MyThread(sd, Integer.toString(i));
            mts[i].start();
            mts[i].join();
            }
        }
}
