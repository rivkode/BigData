package com.bigtech.abc.data;

public class MyThread extends Thread{
    StringDisplay sd;
    String s = "";

    public MyThread(StringDisplay sd, String s)  {
        this.sd = sd;
        this.s = s;
    }

    @Override
    public void run() {
        sd.display(s);
    }
}
