package com.bigtech.abc.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    public void saveAllAndPrint(List<Integer> numbers) {
        saveAll(numbers);
    }

    @Transactional
    public void saveAll(List<Integer> numbers) {
        //
        numbers.forEach((i -> save(i)));
        var n = numbers;
    }

    @Transactional
    public void save(Integer i) {
        if (i == 5) {
            throw new RuntimeException();
        }
        // save
    }

    public class BookServiceProxy extends BookService {

        public void saveAllAndPrint(List<Integer> numbers) {
            super.saveAllAndPrint(numbers);
        }

        public void saveAll(List<Integer> lst) {
            // 시작
            startTransaction();
            try {
                super.saveAll(lst);
            } catch(Exception e) {
                rollback();
                throw e;
            }

            commit();
            // 커밋
        }

        public void save(Integer i) {
            // 시작
            startTransaction();
            try {
                super.save(i);
            } catch(Exception e) {
                rollback();
                throw e;
            }

            commit();
            // 커밋
        }
      }

      private void startTransaction() {

      }

      private void commit() {

      }

      private void rollback() {

      }


    }
}

