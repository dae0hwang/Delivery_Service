# 음식 등록, 음식 주문 서비스를 제공하는 Delivery_Service
Delivery_Service는 가게의 음식 등록, 고객의 음식 주문을 도와주는 배달 서비스입니다.  
가게가 등록한 음식들을 주문하여 각 고객마다 주문 목록을 확인할 수 있고, 하루, 한달 단위 다양한 매출 통계를 제공합니다.
 

이번 프로젝트의 핵심은 스프링 서버의 관계형 데이터 베이스 사용 진화입니다.   
프로젝트 초기에는 트랜잭션 처리와 DB 통신을 jdbc와 SQL 쿼리를 직접 작성하여 진행하였고,
→ 트랜잭션 매니저 적용 → JPA(hiberanate) ORM 기술 적용 → SpringDataJPA 적용 → Querydsl 적용을 통해   
RDBMS 기초부터 활용까지 확실히 이해할 수 있는 프로젝트였습니다.


프로젝트 구성을 RestAPI를 제공하는 API 서버와 화면을 구성하는 UI 서버로 나누어 개발했습니다.   
Html로 구성된 UI 서버는 javascript-axios를 사용하여 API 서버와 통신하여 Json 데이터를 받아 동적 화면을 구현했습니다.
# 관련 블로그 포스팅
[Validation 사용하여 Respuset 정보 검증하기](https://coding-business.tistory.com/89)

&nbsp;&nbsp;&nbsp;&nbsp;[org.passay 사용하여 @Password Validation 검증기 만들기](https://coding-business.tistory.com/90)

[@ExceptionHandler, @ControllerAdvice를 사용한 예외를 원하는 Response 처리](https://coding-business.tistory.com/36)

[순수 JDBC 사용으로 Transaction 처리하기](https://coding-business.tistory.com/81)

[Spring Mysql과 Querydsl 통계성 쿼리 처리](https://coding-business.tistory.com/104)

&nbsp;&nbsp;&nbsp;&nbsp;[DB OLTP와 OLAP의 차이](https://coding-business.tistory.com/39)

[Rest API Axios 통신에 대한 이해](https://coding-business.tistory.com/105)

[CORS 이해와 동작 원리 파악](https://coding-business.tistory.com/47)

[Logback 설정 파일, 콘솔과 파일에 원하는 형식 로그 출력하기](https://coding-business.tistory.com/85)

# 서버 동작 구성
### 1. validation을 사용한 가게, 고객 회원 가입
RequestBody로 들어오는 회원 가입 정보들을 Validation을 적용했습니다.  
입력 규칙에 벗어난 입력이 들어오면 예외가 발생하고 그에 맞는 상태 코드와 메세지를 UI서버에게 제공합니다.

- 회원 가입 성공

![ㅁ회원가입성공](https://github.com/dae0hwang/IgnorantEnglish/assets/103154389/5de87c0e-b4f4-4ba4-8b2b-96fa22b6e246)

- 중복된 아이디 예외 발생

![ㅁ중복된아이디](https://github.com/dae0hwang/IgnorantEnglish/assets/103154389/05e6d163-67c4-4760-9111-00354eb56d1c)

- 아이디, 비밀번호, 이름 validation 예외 발생

![ㅁvalidation](https://github.com/dae0hwang/IgnorantEnglish/assets/103154389/7b21f1df-3b9b-40d9-ade3-1bae2304b694)

### 2. 음식점 - 음식 등록, 가격 수정
각 가게마다 음식을 등록할 수 있고, 음식을 등록한 이후 가격을 수정할 수 있습니다.

- 음식 등록

![ㅁ음식등록](https://github.com/dae0hwang/IgnorantEnglish/assets/103154389/5df62def-d984-4d60-abc5-d37ff9352d5b)
- 가격 수정

![ㅁ음식수정](https://github.com/dae0hwang/IgnorantEnglish/assets/103154389/a8fc7ebe-8e33-4140-9fb4-e40fc1267af4)

### 3. 고객 - 음식 목록 확인, 음식 주문, 주문 확인
고객 개인 페이지에서는 가게 목록들과 가게가 등록한 음식 목록을 확인할 수 있습니다.  
음식점별 음식을 주문할 수 있고, 어떤 음식을 주문했는 지 확인 가능합니다.
- 음식 목록 확인

음식점에서 등록한 음식을 주문할 수 있게 음식 목록과 체크 박스를 제공합니다.

![ㅁ음식목록확인](https://github.com/dae0hwang/IgnorantEnglish/assets/103154389/10913558-4c56-4ac2-81f5-ec7cb6fba56c)


- 음식 주문하기

한번에 여러가지 음식을 여러 개 주문할 수 있습니다.

![ㅁ음식주문하기](https://github.com/dae0hwang/IgnorantEnglish/assets/103154389/af034233-03ab-407c-ae96-e7515743d176)


- 음식 주문 확인하기

고객 본인이 주문한 음식 종류와 수량을 확인할 수 있습니다.

![ㅁ음식주문확인하기](https://github.com/dae0hwang/IgnorantEnglish/assets/103154389/e9ca39df-5522-4f5c-ae43-cd8c2ca1ea5e)

### 4. 통계 - 음식점 별 손님 별 매출 통계 확인

MySQL 통계 쿼리를 사용하여 하루, 한달 단위 별로 매출과 주문 금액을 확인할 수 있습니다.

- 하루, 한달 단위 전체 판매 금액 확인하기

![ㅁ가게전체매출](https://github.com/dae0hwang/IgnorantEnglish/assets/103154389/faa3dbf0-f298-40bc-aeff-665146843537)

- 하루, 한달 단위 가게 별 판매 금액 확인하기

![ㅁ가게별매출](https://github.com/dae0hwang/IgnorantEnglish/assets/103154389/f769eae7-2365-4ebc-aa4b-27522081a447)

- 하루, 한달 단위 손님 별 구매 금액 확인하기

![ㅁ손님금액통계](https://github.com/dae0hwang/IgnorantEnglish/assets/103154389/15085eda-89bf-4e6c-936a-ad4ba531be6f)




