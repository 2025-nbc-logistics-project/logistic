# 📦 물류 관리 및 배송 시스템을 위한 MSA 기반 플랫폼 개발

## 👥 팀원 역할분담
- **[최호진](https://github.com/gentle-tiger):** 허브 컨텍스트
- **[이종원](https://github.com/zapzookj):** 주문 컨텍스트, 알람 컨텍스트
- **[이채연](https://github.com/dkki4887):** 유저 컨텍스트, API 게이트웨이
- **[손세라](https://github.com/srrrn):** 업체 컨텍스트, AI 컨텍스트

## ⚙️ 서비스 구성 및 실행방법
### 서비스 엔드포인트

| 도메인           | 포트   | 기본 URL                                   |
|------------------|--------|--------------------------------------------|
| API Gateway      | 18081  | http://localhost:18081                     |
| Eureka           | 8761   | http://localhost:8761                      |
| Auth             | 18082  | http://localhost:18082/api/v1/auth           |
| User             | 18082  | http://localhost:18082/api/v1/users          |
| DeliveryManager  | 18082  | http://localhost:18082/api/v1/users/delivery-managers |
| Hub              | 18083  | http://localhost:18083/api/v1/hubs           |
| Order            | 18084  | http://localhost:18084/api/v1/orders         |
| Product          | 18085  | http://localhost:18085/api/v1/products       |
| Company          | 18085  | http://localhost:18085/api/v1/companies      |
| AI               | 18086  | http://localhost:18086/api/v1/ai             |
| Slack            | 18087  | http://localhost:18087/api/v1/slack          |
| Delivery         | 18088  | http://localhost:18088/api/v1/deliveries      |

### 데이터베이스 정보

| 도메인           | DB 명         | DB 사용자  | DB 포트 |
|------------------|---------------|------------|---------|
| User             | /db_user      | postgres   | 5432    |
| DeliveryManager  | /db_deliverymgr | postgres | 5432    |
| Hub              | /db_hub       | postgres   | 5432    |
| Order            | /db_order     | postgres   | 5432    |
| Product          | /db_product   | postgres   | 5432    |
| Company          | /db_company   | postgres   | 5432    |
| AI               | /db_ai        | postgres   | 5432    |
| Slack            | /db_slack     | postgres   | 5432    |
| Delivery         | /db_delivery  | postgres   | 5432    |

###  실행 방법

1. **환경 구축**
   - **PostgreSQL 설치**  
     - 로컬에 PostgreSQL을 설치하거나 Docker 컨테이너를 사용하여 실행합니다.
   - **Redis 설치**  
     - 로컬에 Redis를 설치하거나 Docker 컨테이너를 사용하여 실행합니다.

2. **애플리케이션 실행**

3. **API 검증 및 이용**
   - **Postman** 또는 Swagger UI를 이용하여 백엔드 서비스의 엔드포인트에 정상적으로 접근할 수 있는지 확인합니다.


## 📄 프로젝트 개요 및 목적
### 프로젝트 개요
본 프로젝트는 Spring Cloud와 MSA를 활용해 백엔드 시스템을 구축하는 것을 목표로 하며, 특히 물류서비스의 비즈니스 특성을 고려하여 각 서비스의 역할과 경계를 명확히 설정하였습니다. 이를 통해 독립적이고 확장 가능한 아키텍처를 설계하였습니다.

### 프로젝트 목적
본 프로젝트는 Spring Cloud, Eureka, API Gateway 등을 활용해 복잡한 분산 시스템을 구축하며, 물류서비스 도메인을 철저히 분석하여 서비스 분리와 독립적 데이터 관리/통신 체계를 마련하는 한편, GeminiAPI, RESTful API, Spring Security를 적용해 통합 및 보안을 강화하는 것을 목표로 하였습니다.

## 📊 ERD
- [ERD 명세서](https://github.com/2025-nbc-logistics-project/logistic/wiki/ERD-%EB%AA%85%EC%84%B8%EC%84%9C)

## 🛠️ 기술 스택

### 백엔드
- **Spring Boot 3.4.4**: 애플리케이션 기본 프레임워크
- **Spring Cloud**: 마이크로서비스 환경 구성
  - **Eureka**: 서비스 등록 및 발견
  - **API Gateway**: API 라우팅 및 집약
  - **OpenFeign**: 서비스 간 HTTP 클라이언트 통신
- **Spring Security**: 인증 및 권한 관리
- **Zipkin**: 분산 트레이싱 및 이벤트 메시지 추적

### 데이터베이스
- **PostgreSQL**: 관계형 데이터베이스
- **JPA**: ORM 기반 데이터 매핑
- **Spring Data**: 데이터 접근 계층의 효율적 관리

### 캐시 및 메시징
- **Redis**: 캐시 및 메시징 도구

### 통합 API
- **GeminiAPI**: AI 연동 및 질문/답변 처리
- **RESTful API**: 서비스 간 표준화된 데이터 통신

### 기타
- **MSA 아키텍처**: 독립적이고 확장 가능한 시스템 구성
- **이벤트 소싱**: 데이터 무결성과 이력 관리를 위한 패턴

### 빌드 및 협업 도구
- **Gradle**: 빌드 툴
- **Swagger**: API 문서화 도구
- **Notion**: 협업 및 문서 관리
- **git**: 버전 관리 시스템
- **GitHub**: 협업 및 소스 코드 관리

## 📚 API Docs
- [API Docs](https://github.com/2025-nbc-logistics-project/logisti)  

