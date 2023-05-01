package com.bigtech.abc.io;

import java.io.*;

public class CharExam1 {
    public static void main(String[] args) {
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            br = new BufferedReader(new FileReader("src/main/java/com/bigtech/abc/io/CharExam1.java"));
            pw = new PrintWriter(new FileWriter("test.txt"));

            String line = null;
            /**
             * // 파일로부터 한줄 입력받은것이 라인에 들어감 리턴된 값이 null일때까지 반복함
             * 파일이 끝이 되면 null을 반환함 아까 -1 반환과 비슷한 과정
             * IO는 항상 열어주면 닫아주자 close를 하지 않으면 아무 값이 저장되지 않음
             */
            while ((line = br.readLine()) != null) {
                pw.println(line);
            }
            /**
             * 파일이 없을 수 있는 예외(에러아님) 가 발생할 수 있기 때문에 예외 처리를 해준다.
             * 예외처리는 매우 중요함 ..!
             * 강의에서는 try catch 문으로 예외처리를 하였는데 import java.io.FileNotFoundException 로도 할 수 있다 둘의 차이는 뭘까 ?
             */
        } catch (Exception e) {
//            throw new RuntimeException(e);
            e.printStackTrace(); // 아까와 같은 에러
        } finally {
            pw.close();
            try {
                br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
