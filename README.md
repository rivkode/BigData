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

## Query

```sql
save() : INSERT INTO post (id, content, likes, createdDate, modifiedDAte) values (?, ?, ?, ?, ?)
- 저장
findById() : SELECT * FROM post WHERE id = ?
- 단건 조회  
findAll() : SELECT * FROM post
- 전체 조회
getPostByPage() : SELECT * FROM post LIMIT + startIndex + , + pageSize
- 마지막 페이지 조회
update() : UPDATE post SET content=? WHERE id=?
- 수정
delete() : DELETE FROM post WHERE id=?
- 삭제
```

## 고민 내용

- 마지막 페이지 조회시 걔네들 느린부분 어떻게 최적화

## API 명세

| Method | URI                       | 설명                      |
|--------|---------------------------|-------------------------|
| Get    | /question/list            | 전체조회                    |
| Get    | /question/list/first      | 첫 20개 조회                |
| Get    | /question/list/last/{idx} | idx 개의 데이터 중 가장 마지막 20개 |

## 마지막 페이지 조회 결과

![image](https://user-images.githubusercontent.com/109144975/233847618-7d0c51f7-de18-4408-967b-dd617a87e5bc.png)


## 질문

# 1. CMD 창에서 쿼리문 조회 결과와 실제 데이터 저장이 다르게 조회되는 이유 

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


```java
@Test
    void makeData() throws SQLException {
        for (int i = 2500000; i <= 3000000; i++) {
            Long id = (long) i;
            String content = "내용 없음";

            String s1 = "2022-01-01 00:00:00.000";
            String s2 = "2022-12-31 23:59:59.000";
            Timestamp tp1 = Timestamp.valueOf(s1);
            Timestamp tp2 = Timestamp.valueOf(s2);

            //save
            Post post = new Post(id, content, 10, tp1, tp2);
            this.postRepository.save(post);
        }
```

위와 같이 for문을 사용하여 대용량 데이터를 저장하였지만 매우 느린 속도
(post객체를 List 에 담아 Batch 방법을 시도하였지만 실패)

batch 방법 이외로 빠르게 저장하는 방법이 있을까요 ?
- 50만건 기준 24분

![](https://user-images.githubusercontent.com/109144975/233825958-0cd81bde-fc83-4284-b512-95c7229dcecb.png)

### 부수적인 질문 (IDE 관련)

- ide 에서 에러 코드를 보여줄때 1.6만건 이 조회되는 시간이 엄청빨랐음
- 거의 MYSQL에서 쿼리로 조회하는 수준하고 비슷함
- 이게 왜 이렇게 빠르게 출력이 될까