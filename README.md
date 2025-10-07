# 요구사항 정의

주문기능을 만들려고 하며 다음과 같은 역할을 하여야 한다.

```text
- 주문데이터를 저장하여야 한다.
- 재고관리를 하여야한다.
- 포인트를 사용해야한다.
- 주문, 재고, 포인트 데이터의 정합성이 맞아야한다.
- 동일한 주문은 1번만 이루어져야한다.
```


# MSA로의 전환

## Monolithic Architecture

모든 기능이 하나의 애플리케이션에 포함된 구조입니다.
- 서비스 전체가 단일 애플리케이션으로 묶여 있어 결합도가 높습니다.
  - 하나의 코드페이스로 운영되며, 빌드/배포도 한번에 수행합니다.
  - 다양한 도메인(예: 주문, 결제, 회원 등)이 하나의 서비스 안에 섞여 있습니다.
  - 특정 기능의 성능을 높이려면 전체 애플리케이션을 함께 확장해야 합니다.
- 장점 : 구조가 단순하여 빠른 개발 및 테스트 가능, 트랜잭션 관리 용이
- 단점 : 서비스가 커질수록 유지보수와 화장이 어려워집니다.

---

## MSA(Microservices Architecture)

기능을 여러 개의 작고 독립적인 서비스로 나누어 운영하는 아키텍처입니다.
- 각 서비스는 도메인 단위로 분리되어 독립적으로 운영합니다. 
  - 예 : 주문, 포인트, 재고등
- 개별적으로 빌드/배포 되며, 서로 다른 기술 스택도 사용할 수 있습니다.
- 성능 확장이 필요한 부분만 선택적으로 확장이 가능합니다.
- 장점 : 도메인별 소유권 부여 등 유연성 극대화
- 단점 : 초기 인프라 구성, 트랜잭션 관리, 모니터링 등 운영 복잡도 증가


## MSA로 전환하면서 생기는 문제점

- 서비스간 데이터정합성을 보장하기 까다로워 짐

## 분산트랜잭션을 보장하기 위한 방법들

1. 2PC
2. TCC
3. SAGA

## 2PC란?

- Two-Phase Commit Protocol의 약자로 분산시스템에서 트랜잭션의 원자성을 보장하기 위해 사용하는 프로토콜
- 트랜잭션을 두단계로 나누어 처리함
  - Prepare 단계 : 트랜잭션 매니저가 참여자에게 작업 준비가 가능한지 묻는다
  - Commit 단계 : Prepare단계에서 모든 참여자가 작업이 가능하다고 응답하면 실제로 커밋을 수행한다.
- 대표적인 구현으로는 XA 트랜잭션이 존재합니다.

- 1번
```sql
create database 2pc1;
       
create table product (
    id INT PRIMARY KEY,
    quantity INT);

insert into product values(1,1000);

xa start 'product_1';
update product set quantity=900 where id =1;
xa end 'product_1';
   
xa prepare 'product_1';
xa commit 'product_1'; 
```
- 2번
```sql
create database 2pc2;
       
create table point (
    id INT PRIMARY KEY,
    amount INT);


xa start 'point_1';
insert into point values(1,1000);
xa end 'point_1'
```

- 3번
```sql
update product set quantity=900 where id =1; --락 걸림 xa commit 'product_1'; 이후 수행됨
```

# 2PC 장단점

- 장점 
  - 강력한 정합성을 보장
  - 사용하는 데이터베이스가 XA를 지원한다면 구현난이도가 낮음
- 단점
  - 사용하는 데이터베이스가  XA를 지원하지 않는다면 구현하기 어렵습니다.
  - prepare 단계 이후 commit이 완료될떄까지 lock을 유지하기 때문에 가용성이 낮아집니다.
  - 장애복구시 수동으로 개입하여 해결해야 합니다.
  - 실용성이 낮습니다.
- 실무에서는?
  - 2PC보다 다른 방법을 사용하여 분산트랜잭션을 구현합니다.

# TCC 패턴이란?

- TCC(Try-Confirm-Cancel)는 분산 시스템에서 데이터 정합성을 보장하기 위해 사용하는 분산트랜잭션 처리 방식
- 세단계로 나누어 트랜잭션을 관리
  - Try: 필요한 리소스를 점유할 수 있는지 검사하고 임시로 예약합니다.
  - Confirm: 실제 리소스를 확정 처리 반영합니다.
  - Cancel: 문제가 생긴 경우, 예약상태를 취소하여 원복합니다.
- Try, Confirm, Cancel 단계는 멱등하게 설계가 되어야합니다.

# TCC의 장단점

- 장점
  - 확장성과 성능에 유리
    - 2PC에 비해 데이터베이스 Lock 점유시간이 짧음
    - 2PC에 비해 Long Transaction에 덜 취약
  - 장애 복구와 재시도 처리에 유연
    - 비즈니스 정책에 따라 전략을 정할 수 있음
- 단점
  - 기존 시스템에 비해 설계와 구현이 복잡하다.
    - 모든 단계 (Try, Confirm, Cancel)는 멱등적으로 설계되어야 합니다.
    - 네트워크 오류, 재시도 시나리오를 고려한 복잡한 로직 구현이 필요합니다.


# Pending 상태 대처

- Confirm단계 실패
  - 특정시간이 지난 경우 Admin에서 확인
- Cancel단계 실패
  - Order의 상태에 맞춰 자동으로 취소 처리


# Saga란?

- 분산 시스템에서 데이터 정합성을 보장하기 위해 사용하는 분산트랜잭션 처리방식
- 각 작업을 개별 트랜잭션으로 나누고 실패시 보상 트랜잭션을 수행하여 정합성을 맞추는 방식
  - 보상트랜잭션 로직은 멱등성이어야하며 재시도 가능해야함
- TCC와 달리 Saga는 리소스 예약 없이 즉시 상태 변경을 수행
  - 재고차감 예약이 아닌 즉시 차감
  - 최종적 일관성(Eventual Consistency)을 보장
- Choreography 방식과 Orchestration 방식이 존재


# Saga - Orchestration

- Coodinator(또는 Orchestrator)가 각 참여 서비스들을 순차적으로 호출하며 전체 트랜잭션의 흐름을 제어
- 장점
  - 구현 난이도와 유지보수 난이도가 낮음
- 단점
  - 시간이 지날수록 Coordinator(Orchestrator)가 복잡해질 수 있음
  - 서비스간 결합도가 증가함