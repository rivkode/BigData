# 대용량 데이터

### [대용량 데이터](https://velog.io/@rivkode/series/%EB%8C%80%EC%9A%A9%EB%9F%89-DB)

## INDEX

- 1차 요구사항
- 2차 요구사항
- 3차 요구사항

# 🚀 1차 요구 사항

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

## 요구사항 결과

- query `SELECT * FROM post ORDER BY id LIMIT 2000000, 100;`
- id가 PK 값이므로 b-tree로 인덱싱 됨
- 39초

![](https://user-images.githubusercontent.com/109144975/233895541-3eb92758-f5bb-45ff-9fbe-e1972399b547.png)
  
    
- query `SELECT * FROM post ORDER BY createdDate LIMIT 2000000, 100;`
- 정렬을 인덱싱하지 않은 값으로 하여 비교적 오랜 시간 소요
- 58초

![](https://user-images.githubusercontent.com/109144975/233896147-52282941-fac8-4428-82b1-01d85e47f03f.png)

 
- 결과 화면

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


# 🚀 2차 요구 사항

목표 1 : 300만건 데이터 특정 페이지 조회시 1초 이내로 수행

- JPA 사용하지 않고 Query 문을 통한 조회
- 테이블 인덱싱
- 1차 요구사항에서 데이터 조회시 왜 `60초 씩이나` 느린지 알기
- 어떤 방식으로 빠르게 할 수 있는지 알기

목표 2 : 디스크에 있는 파일을 자바 프로그램으로 읽고 출력할 때의 과정을 자세히 설명

- 자바에 집중하지 말고 운영체제, 컴퓨터가 어떻게 동작하는지 집중해서 공부
- Java code 로 파일 입출력

목표 3 : DB에 데이터 저장시 속도 향상

- 프로시저
- bulk INSERT
- 병렬 스레드

## 🎯 프로그래밍 환경
- IDE : IntelliJ
- JDK : 11
- DB : MYSQL 8.0
- gradle, junit
- SpringBoot : 2.7.11
  - mysql-connector-j
- Thymeleaf

## 고민 내용

- 왜 느릴까 ?
- 디스크 파일 입출력시 운영체제의 어떤 원리를 필요로 할까?

## 요구사항 결과

- 왜 느릴까 ? (8~40 초)


1. INDEX 설정

![image](https://user-images.githubusercontent.com/109144975/235354513-d3526bca-650e-45a3-9e56-63ce8b683aae.png)

![image](https://user-images.githubusercontent.com/109144975/235355132-a922ed4f-9aac-4d32-8e5a-5638e4761d9b.png)

post 테이블을 살펴보면 아무런 INDEX도 설정되어있지 않았다

아무 인덱스가 없으니 데이터를 조회할때 매우 느린결과를 얻을 수 밖에 없습니다.

### [인덱스 클러스터, 힙](https://velog.io/@rivkode/%EC%9D%B8%EB%8D%B1%EC%8A%A4-%ED%81%B4%EB%9F%AC%EC%8A%A4%ED%84%B0-%ED%9E%99)

### [MySQL LIKE % 위치에 따른 인덱스](https://velog.io/@rivkode/MySQL-LIKE-%EC%9C%84%EC%B9%98%EC%97%90-%EB%94%B0%EB%A5%B8-%EC%9D%B8%EB%8D%B1%EC%8A%A4)

인덱스란 ?
- 추가적인 쓰기 작업과 저장 공간을 활용하여 데이터베이스 테이블의 검색 속도를 향상시키기 위한 자료구조

인덱스에서는 2가지 종류가 있습니다.
- 클러스터형 인덱스 (영어 사전 : 실제 데이터 정렬)
- 보조 인덱스 (책 뒷편의 찾아보기 : 데이터 위치를 찾은 뒤 해당 페이지를 펼치는 것)

인덱스를 사용하면
- SELECT 문으로 검색하는 속도가 매우 빨라집니다
- 컴퓨터의 부담이 줄어드므로 전체 시스템의 성능이 향상됩니다

단점은
- 인덱스는 그 역할을 위해 추가 10%의 공간이 필요합니다
- SELECT 가 아닌 데이터의 변경(INSERT, UPDATE, DELETE)가 자주 일어나면 오히려 성능이 나빠질 수 있습니다

id를 기본키(PK)로 정의하겠습니다 아래와 같이 설정하겠습니다

이 말은 id 컬럼에 클러스터형 인덱스가 생성되는 것과 동일합니다

기본키는 테이블에 하나만 지정이 가능하며 클러스터형 인덱스는 테이블에 한 개만 만들 수 있습니다


![image](https://user-images.githubusercontent.com/109144975/235355422-37d45e4d-de3b-4978-8fd9-c57b6ea157de.png)

300만개 데이터에 대해 id 컬럼에 대하여 PK를 설정해주는 시간은 약 13초가 소요되었습니다


![image](https://user-images.githubusercontent.com/109144975/235355465-d3b25857-3cf0-4a4b-a705-dc754ef90f1b.png)

PK 값을 설정하는 과정에서 id 값이 중복되는 것을 확인하였고 일일이 지워줘야하는 과정을 겪었습니다

위 과정이 발생한 일은 제가 save 코드를 작성할때 for문에서 아래와 같이 등호를 넣어주었기 때문입니다

![image](https://user-images.githubusercontent.com/109144975/235355629-9ca12255-545b-4a6b-a30f-bdb0a9172733.png)


기본적인 for문 사용을 잘못된 방법으로 사용하여 중복오류가 발생하였습니다

앞으로 이런 오류가 발생하지 않도록 for문을 주의해서 사용해야겠다는 생각이 들었습니다


인덱스를 적용한 결과 BETWEEN 사용시 4초 -> 0.00초의 결과를 얻을 수 있었습니다

![image](https://user-images.githubusercontent.com/109144975/235355748-b6c6161b-cf5e-4f44-98ae-42082187221b.png)

2. LIMIT 사용
```sql
SELECT * FROM post WHERE id > startIndex ORDER BY id LIMIT  + startIndex + , + pageSize
```
위와 같이 조회를 하게 되면 LIMIT의 경우 startIndex 인덱스까지 한 번에 넘어가면서 skip되는 것이라 생각하였지만 틀렸었습니다

skip 되어 바로 startIndex로 넘어가는 것이 아니고 모두 전부 조회가 다 되는것이었습니다

Paging을 구현할 때 위와 같이 LIMIT, OFFSET을 사용하여 구현하는 경우가 많고 이때 WHERE 조건에서 인덱스가 걸려 최적화 된다고 생각할 수 있지만 중요한 부분은 따로 있었습니다

`Row lookup`으로 인한 성능 저하 입니다

Row lookup은 인덱스가 가르키는 row 행에 접근하여 가져오는 행위를 의미합니다 즉, LIMIT OFFSET을 통해 페이지하는 동안 조회되는 row들을 바라봤기 때문에 성능이 느려지는 것입니다

lookup이 이뤄지지 않도록 인덱스 내에서만 모든 검색을 하고 실제 데이터를 가져올 때 JOIN을 하는 방법입니다

이를 Late row lookup 이라고 합니다

### Row lookup 과 Late row lookup

`Row lookup`이란 Index record 와 실제 Table record 사이에서 발생하는 Fetching을 의미합니다

다시 말해, 인덱스에서 연결된 테이블 레코드로 자료들의 값들을 가져오는 행위를 의미합니다

여기서 테이블 레코드로 접근하여 해당 자료를 읽는 시간이 필요하며 이 소요시간을 최소화 하는 방법을 Late row lookup 입니다

인덱스를 적극 사용하여 late row lookup을 최대화 하는 것이 SELECT 성능을 크게 향상시켜줍니다

이런 방법으로 인덱스 값만 이용하여 INNER SELECT 를 동작한 다음 실제 테이블 레코드와 JOIN을 하는 방법이 위에 예제로 작성된 내용입니다


![image](https://user-images.githubusercontent.com/109144975/235356990-25919ec2-f97c-4495-bab0-239df0fb898f.png)

## 2. 디스크에 있는 파일을 자바 프로그램으로 읽고 출력할 때의 과정

### [입출력장치](https://github.com/rivkode/tech-for-developer/blob/main/Computer%20Science/Computer%20Architecture/%EC%9E%85%EC%B6%9C%EB%A0%A5%EC%9E%A5%EC%B9%98.md)

- 장치 컨트롤러 : 입출력 장치를 연결하기 위한 하드웨어적 통로
- 장치 드라이버 : 입출력 장치를 연결하기 위한 소프트웨어적인 통로

자바에서 사용하는 파일 입출력은
blocking I/O - Synchronous

파일 입출력에는
- Blocking I/O , Non Blocking I/O
- Synchronous, Asynchronous
가 있음

2차 메모리 I/O - File I/O를 함

- 하드웨어에서는 운영체제와 2차 메모리를 연결해주기 위한 디스크 드라이버가 존재함
- 커널(운영체제)에서는 파일 시스템이 존재함 윈도우는 (NTFS)
- 파일 시스템은 운영체제 수준에서 동작하는 커널모드 소프트웨어임
- 하드 디스크 c 드라이브
- 다른 디스크 d 드라이브

동기화 I/O는

USER 에서 자바 프로그램을 실행시키는 순간 CPU를 할당받고 프로세스가 됨

그러면 파일 시스템에 입출력을 수행하기 위한 인터페이스가 필요함

인터페이스를 통해 파일 I/O를 시도함

FileReader를 통해 파일 디스크립터를 얻음

아래는 System.out.println();을 통해 출력한다면 이루어지는 과정

다시 말해, FileReader로 파일을 열고 println을 하면 이루어지는 과정은

![image](https://user-images.githubusercontent.com/109144975/235461482-f5867660-293d-4e24-8fac-2622413d4db6.png)


## 입출력의 진행 과정
```
1. 파일을 호출함 (Call)

2. 호출하게 되면 그 정보가 파일 시스템으로 내려감

3. 파일 시스템은 그 정보를 통해 디스크 드라이버를 움직임

4. 드라이버가 실제로 디스크 파일에 Write를 함

5. 다 쓰면 드라이버가 파일 시스템에게 alert를 함 (알려줌)

6. 그 다음 System.out.println();을 호출하게 됨 Return

```

Blocking I/O의 특징은 Call에서 return까지 계속 Wait를 하고 있음

프로그램이 응답결과를 받기 전까지 다음 단계로 넘어가지 못함


# 🚀 3차 요구 사항

목표 1 : JPA를 사용하여 페이지 네이션을 스프링으로 구현 

- modifiedDate 순으로 데이터를 주는 api 기능
  - 인덱스 활용
- 게시물 쓰기 기능

목표 2 : 읽어야 하는 내용들 정리하기

- 읽어야 하는 내용들 정리하기
  - [SQL Unplugged](https://youtu.be/TRfVeco4wZM)
  - [Binary Search Tree에서 B+ Tree까지](https://velog.io/@jewelrykim/Binary-Search-Tree%EC%97%90%EC%84%9C-BTree%EA%B9%8C%EC%A7%80Database-Index-%EC%B6%94%EA%B0%80)
  - [성능향상을 위한 SQL](https://d2.naver.com/helloworld/1155)
  - [MySql 아키텍처](https://www.youtube.com/watch?v=vQFGBZemJLQ)
  - [개발자가 알아야 하는 지연 숫자](https://colin-scott.github.io/personal_website/research/interactive_latency.html)
    - [B+Tree Visualization](https://www.cs.usfca.edu/~galles/visualization/BPlusTree.html)


목표 3 : 지난 주차 내용 에서 부족한 내용들 보충

- SQL : `EXPLAIN` 키워드를 사용하여 1초 이내로 쿼리 짜기
- 디스크의 파일을 코드로 읽고 출력
  - `Println` 즉, 모니터상 콘솔 출력에서도 입출력 프로세스를 추가하여 설명
  - System Call 을 통해 Kernel(운영체제)가 USER(운영체제) 에게 특정 리소스에 대한 접근 권한을 주고 받는 과정을 포함하여 설명
    - 프로세스가 특정 리소스에 대한 접근 권한을 운영 체제로 요청하면 운영체제는 해당하는 드라이버에 접근 권한을 반환

## 🎯 프로그래밍 환경
- IDE : IntelliJ
- JDK : 11
- DB : MYSQL 8.0
- gradle, junit
- SpringBoot : 2.7.11
  - mysql-connector-j
- Thymeleaf

## 고민 내용

- JPA는 내부적으로 어떻게 동작할까
  - CRUD 작동원리
- modifiedDate 컬럼에 대해 인덱스를 적용하면 어떤일이 발생할까
  - 또 다른 테이블 생성 (추가 10%)
- 데이터베이스는 왜 B tree 자료구조를 사용하는가

## 요구사항 결과

### 목표 1 : JPA를 사용하여 페이지 네이션을 스프링으로 구현

### API 명세

| Method | URI                     | 설명                          |
|--------|-------------------------|-----------------------------|
| Get    | /post/pageList?page=int | int 페이지 조회 (수정일 기준)         |
| Get    | /post/listUp            | Query문 사용하여 수정일 기준 최근 목록 조회 |
| Post   | /post/save              | Post 저장                     |

1. 페이지네이션을 사용하여 구현

- JPA 의 Page findAll() 메소드를 상속받아 사용하였음
- postService 에서 Sort.Order.desc("modifiedDate")를 통해 modifiedDate 컬럼을 DESC 조건으로 조회하여 리스트 생성 및 가져옴
- postController에서 생성한 리스트를 int 값으로 page값을 전달받고 해당하는 페이지를 리턴하여 결과를 화면에 보여줌

결과화면

![image](https://user-images.githubusercontent.com/109144975/236675538-df817f13-2773-495e-b90d-cec0b364dde9.png)



2. Query를 통해 최근 게시물 조회

```java
@Query(
            value = "SELECT * FROM post ORDER BY modified_Date DESC LIMIT 100",
    nativeQuery = true)
    List<Post> findByModifiedDate();
```

- JPA 리포지토리에서 위 메소드를 통해 컨트롤러 반환된 결과를 전달
  - 1 페이지만 조회하는 것이므로 바로 전달, 서비스를 이용하려했지만 연산이 필요하지 않아 바로 전달함 
- 컨트롤러에서 결과를 화면에 보여줌
- 페이징을 직접 구현하려면 수정일 기준 정보들을 조회하고 결과를 반환해야 함
- 반환하는건 쉽지만 어떻게 최적화 할지는 좀 더 고민 필요 

결과 화면

![image](https://user-images.githubusercontent.com/109144975/236675711-d8da2df8-4a14-4bc9-ac38-a7b6c8b69907.png)

### 목표 2 : 읽어야 하는 내용들 정리하기

```
고려 사항 :
- 인덱스를 잘 알고
- SQL을 이해하며
- 데이터베이스 알고리즘은 B Tree를 이진 탐색부터 B+ Tree까지 이해하고
- 데이터베이스가 어떤 아키텍처를 통해 동작하는지 큰 그림 그려보자

DB를 어떻게 왜 사용해야 하는지를 이해하는 것이 핵심 !
```

<br>


## [인덱스 클러스터, 힙](https://velog.io/@rivkode/%EC%9D%B8%EB%8D%B1%EC%8A%A4-%ED%81%B4%EB%9F%AC%EC%8A%A4%ED%84%B0-%ED%9E%99)

<br>


# 🚀 4차 요구 사항

Mysql을 이해하기 위해 3가지 키워드가 있다

- 인덱스
- 트랜잭션
- 락

이번 시간에는 트랜잭션에 대해 이해해보자

[데이터베이스 트랜잭션(transaction)](https://www.youtube.com/watch?v=sLJ8ypeHGlM)

목표 1 : 회원 게시글 좋아요 기능

- 회원 테이블 생성
  - id, name, birth, email, createdate, updatedate
- 회원 저장
- 좋아요
  - 좋아요 누를 시 회원 id 넘겨주기
  - 동일한 회원이 중복해서 좋아요를 누를 수 없도록 구현
- 구현 환경
  - 게시물은 10개, 회원도 10명 정도


목표 2 : 프로그래밍 언어에서 컴파일 과정을 자세히 설명 (C, C++, Go 중)

- Mysql에는 parser가 있음
  - 프로그래밍 언어에서는 ?



## 고민 내용

- JPA는 내부적으로 어떻게 동작할까


## 요구사항 결과

공유자원, 임계구역, 메모리 배리어, Select for update

### [관련 키워들로 정리하기](https://industrious-crow-d0f.notion.site/5-b03a69e152374a4f86b8ae32026f7ac7?pvs=4)

게시글 좋아요 사용자 조회
대량 좋아요 기능

### [게시글을 좋아요한 사용자 조회, 좋아요 대량 생성](https://industrious-crow-d0f.notion.site/6-5d573ea515354554b37a0c6aabf4a98f?pvs=4)


좋아요 동시성 테스트

### [좋아요 동시성 테스트](https://industrious-crow-d0f.notion.site/7-83524b320209427498aafa6c18220a9e?pvs=4)


# 🚀 5차 요구 사항

@Transactional 어노테이션이 동작하는 자세한 원리

이번시간에는 스프링에서 자주 사용하는 Transactional 어노테이션에 대해 알아보자

## 요구사항 결과

### [Transactional, 상속에 대해](https://github.com/rivkode/tech-for-developer/blob/main/Computer%20Science/Programing%20Language%20Theory/%EC%83%81%EC%86%8D%20Transactional.md)

# 🚀 6차 요구 사항

- 타임라인
  - 팔로워들의 글들을 무한스크롤 기능 구현
- 약타입, 강타입 언어 특징
- 프로세스 스레드 exec fork 정리


## 고민내용

스키마 변경

기존의 Post 스키마는 아래와 같다

```
+---------------+--------------+------+-----+---------+----------------+
| Field         | Type         | Null | Key | Default | Extra          |
+---------------+--------------+------+-----+---------+----------------+
| post_id       | bigint       | NO   | PRI | NULL    | auto_increment |
| content       | varchar(255) | YES  |     | NULL    |                |
| created_date  | datetime(6)  | YES  |     | NULL    |                |
| modified_date | datetime(6)  | YES  |     | NULL    |                |
| subject       | varchar(255) | YES  |     | NULL    |                |
+---------------+--------------+------+-----+---------+----------------+
```

여기서 팔로워들의 글들을 가져오려면 Post를 누가 썼는지에 대한 Member id가 있어야 한다

이전에 구현했던 좋아요 기능을 생각해보면
누가 좋아요를 눌렀는지에 대한 정보는 Like 테이블 내에 좋아요한 **member id**와 **post id**를 하나의 column에 넣어 관리했다

아래 Like의 테이블을 살펴보자
```
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| id          | bigint       | NO   | PRI | NULL    | auto_increment |
| like_status | varchar(255) | YES  |     | NULL    |                |
| member_id   | bigint       | YES  | MUL | NULL    |                |
| post_id     | bigint       | YES  | MUL | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+
```

이와 같이 하나의 column에 넣어 관리하였고 누가 좋아요를 했는지를 알기 위해서는

`SELECT member_id FROM like where post_id : id` 로 확인하면 되었었다

이번에도 비슷하게 member_id에 따른 post_id가 필요했기 때문에

`SELECT post_id FROM post where member_id : id` 로 확인하려 했지만
member_id가 없었다

~~이때 스키마를 변경해야할지, 혹은 JOIN을 사용해야할지 고민을 하였다

하지만 JOIN을 사용하게 되면 post 테이블과 member테이블을 JOIN 하게되면 성능이 좋지 않을까 생각하였다~~

다시보니 애초에 post나 member에서 외래키를 관리하지 않았음 .. 이러면 누가 post를 작성했는지 모른다는 건데 왜 이런식으로 설계했을까.. 다시 되돌아보게 된다

한 명의 사용자가 여러 게시글을 쓸 수 있으므로 post 테이블이 member id인 FK를 관리하도록 한다

즉, post 테이블에 member_id를 추가하자

이후 아까 시도하려고 하였던 `SELECT post_id FROM post where member_id : id` 로 post_id를 가져온 뒤

post 리스트를 만들어서 화면에 표시해보자


