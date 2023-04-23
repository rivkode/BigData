package com.bigtech.abc.data;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ListTest {


    @Test
    void listTest() {
        String a = "a";
        List<String> stringList = new ArrayList<>();

        for (int i=0; i<100; i++) {
            stringList.add(a + i);
        }
        System.out.println("stringList = " + stringList);
    }
}
