# 대용량 데이터 조회

[대용량 데이터 DB 저장](https://velog.io/@rivkode/series/%EB%8C%80%EC%9A%A9%EB%9F%89-DB)

## 🚀 요구 사항

목표 : 300만건 이상의 데이터를 DB에 저장하고 페이지네이션을 통해 마지막 페이지에 대한 데이터를 60초 이내로 조회

- 300만개의 데이터를 테이블에 저장
- JPA 사용하지 않고 데이터 CRUD 구현
- Query 문을 통한 조회
- 마지막 페이지 조회시 60초 이내로 조회
- 마지막 페이지를 조회 할때 느린 이유를 찾기

## 🎯 프로그래밍 환경
- IDE : IntelliJ
- JDK : 11
- DB : MYSQL 8.0
- gradle, junit
- SpringBoot : 2.7.11
  - mysql-connector-j
- Thymeleaf

## 고민 내용

- 마지막 페이지 조회시 걔네들 느린부분 어떻게 최적화

## API 명세

| Method | URI                       | 설명                      |
|--------|---------------------------|-------------------------|
| Get    | /question/list            | 전체조회                    |
| Get    | /question/list/first      | 첫 20개 조회                |
| Get    | /question/list/last/{idx} | idx 개의 데이터 중 가장 마지막 20개 |


## 질문

# 1. CMD 창에서 쿼리문 조회 결과와 실제 데이터 저장의 찿이 

![image](https://velog.velcdn.com/images/rivkode/post/c900566c-6db4-4622-b62d-b6ef61644000/image.png)

# 2. JDBC에서 CRUD를 할때 열고 닫아줘야 하는 이유

```java
public Post save(Post post) throws SQLException {
        String sql = "insert into post (id, content, likes, createdDate, modifiedDAte) values (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null; // injection 공격을 예방하기 위해 바인딩 방식 사용

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, post.getId());
            pstmt.setString(2, post.getContent());
            pstmt.setInt(3, post.getLikes());
            pstmt.setTimestamp(4, post.getCreatedDate());
            pstmt.setTimestamp(5, post.getModifiedDate());
            pstmt.executeUpdate();
            return post;

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally { // 커넥션을 닫아줘야함
            close(con, pstmt, null);
        }
    }
```

- try catch finally 문으로 생성순으로 close 를 나중에 또 해줘야 하는데 이렇게 커넥션을 닫아줘야 하는 이유가 뭘까 ?

# 3. 데이터 저장 시 for 문보다 더 좋은 저장 방법




### 부수적인 질문 (IDE 관련)

- ide 에서 에러 코드를 보여줄때 1.6만건 이 조회되는 시간이 엄청빨랐음
- 거의 MYSQL에서 쿼리로 조회하는 수준하고 비슷함
- 이게 왜 이렇게 빠르게 출력이 될까