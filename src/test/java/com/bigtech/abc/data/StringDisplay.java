package com.bigtech.abc.data;

public class StringDisplay {
    synchronized void display(String s) {
        for (int i=0; i<5; i++) {
            System.out.print(s);
        }
        System.out.println("");
    }
}
