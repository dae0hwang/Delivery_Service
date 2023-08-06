## 프로젝트 목표
사업자는 음식을 등록하고, 고객은 음식을 주문할 수 있는 개인 프로젝트입니다.  
통계성 페이지에서 하루, 한달 단위의 가게 별, 손님 별 매출 정보를 확인할 수 있습니다.

이번 프로젝트의 목적은 JDBC → 트랜잭션 매니저 → JPA 순으로 기술을 적용 시키면서, DB에 대한 이해와 실력을 숙달하는 것입니다.

그리고 상황을 가정하여, Nginx(무중단 배포 필요), Redis(많이 조회하는 음식 목록 조회), Prometheus(API별 시간, 오류 파악), ELK Stack(에러 로그 중앙에서 효율적 관리) 기술들을 프로젝트에 적용했습니다.

## 기술 스택
언어 : Java11, HTML, JavaScript(axios)  
DB : MySQL 8.0  
라이브러리 : SpringBoot 2.7.5, JPA, Thymeleaf

## ERD
![ERD](https://github.com/dae0hwang/Delivery_Service/assets/103154389/784b80f5-1bcb-415a-bb56-26eb2eac2304)

## 프로젝트 동작
### 1.음식점, 고객 회원 가입과 정보 검증
RequestBody로 들어오는 회원 가입 정보를 검증하고 그에 따른 상태 코드와 메세지를 전달합니다.
- 회원 가입 성공
<p align="left">
<img src="https://github.com/dae0hwang/Delivery_Service/assets/103154389/880c68e1-2cf1-47a1-81cb-85cee86d6133">
</p>

- 중복된 아이디 검증
<p align="left">
<img src="https://github.com/dae0hwang/Delivery_Service/assets/103154389/1c351ebe-8886-46e9-92c2-c18dea6047f9">
</p>

- 아이디, 비밀번호, 이름 정보 검증
<p align="left">
<img src="https://github.com/dae0hwang/Delivery_Service/assets/103154389/260836d4-8371-4859-bbb2-32ee15d65699">
</p>

### 2. 가게 기능 - 음식 등록과 가격 수정
각 가게마다 판매하고 싶은 음식을 등록할 수 있고, 수정 페이지에서 가격을 수정할 수 있습니다.
<p align="left">
<img src="https://github.com/dae0hwang/Delivery_Service/assets/103154389/2f98a648-9a7a-41c8-81a2-8888fe6c03eb">
</p>

### 3. 고객 기능 - 음식 주문과 주문 내역 확인
고객들은 등록된 가게와 음식 정보를 확인할 수 있고, 다중 체크 방식을 통해 여러 개의 음식을 주문할 수 있습니다.   
그리고 주문 내역 페이지에서 지금까지 주문한 음식 종류와 개수를 확인할 수 있습니다.
<p align="left">
<img src="https://github.com/dae0hwang/Delivery_Service/assets/103154389/528e3338-4475-4895-b17b-8ecc2421d197">
</p>

### 4. 통계 기능 - 가게, 손님 별 매출 통계 확인
SQL 통계성 쿼리를 사용하여, DB에 있는 데이터로  
하루, 한달 단위 전체 판매 금액 확인  -  가게 별 판매 금액 확인  -  손님 별 구매 금액 확인 통계를 확인할 수 있습니다.
<p align="left">
<img src="https://github.com/dae0hwang/Delivery_Service/assets/103154389/80c43b5e-401c-4c02-8824-eddec4b9eeb5">
</p>



