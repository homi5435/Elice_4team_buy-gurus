# Buy-Gurus

## 프로젝트 소개
Buy-Gurus는 전자기기 전자상거래 플랫폼으로, 사용자들이 다양한 전자기기를 살펴보고 구매할 수 있는 공간입니다. 이 프로젝트는 소비자가 상품 정보와 리뷰를 통해 합리적인 소비를 제공하는 것을 목표로 하고 있습니다.

## 팀원 소개
| 이름   | 역할    | 담당 기능                        |
|--------|---------|----------------------------------|
| 한상현 | Order   | 주문 (작성, 수정, 삭제, 배송지 작성, 배송지 수정, 배송지 삭제 등)   |
| 나용진 | User    | 사용자 (회원가입, 로그인 등)   |
| 유호성 | Product, Review  | 상품 (작성, 수정, 삭제, 이미지 업로드 등), 댓글 (작성, 수정, 삭제 등)   |
| 홍민영 | OrderItem   | 장바구니 (장바구니 물품 생성, 수정, 삭제 등)   |
| 김다애 | Category   | 카테고리 (카테고리 생성, 삭제 등)   |

## ERD
- [ERD 링크](https://www.erdcloud.com/d/XEQxhup44GQxREX7b)

## System Architecture
![architecture1](/uploads/fa07492c46a785b3e1f45f53e05dea1d/architecture1.png)

## 기술 스택
- **Frontend**: Bootstrap, React
- **Backend**: Spring Boot
- **Database**: MySQL
- **Security**: Spring Security, JWT(JSON Web Token)
- **DevOps**: AWS (EC2, S3, RDS), Redis

## 주요 기능
- **사용자**: 회원가입, 로그인, 로그아웃, 이메일 인증, 사용자 정보 수정
- **상품**: 작성, 수정, 삭제, 이미지 업로드 기능
- **댓글**: 작성, 수정, 삭제
- **장바구니**: 생성, 수정, 삭제
- **주문(배송지)**: 생성, 수정, 삭제
- **카테고리**: 생성, 수정, 삭제

## 배포 환경
```
- 서버 : GCP VM
    - OS: Ubuntu 20.04.6 LTS(GCP)
    - JRE: OpenJDK 17
    - 애플리케이션 서버: Spring Boot (내장 Tomcat 사용)
- 데이터베이스: AWS RDS (MySQL)
    - MySQL 버전: 8.0.35
- 파일 저장소: AWS S3
- 기타 라이브러리 및 도구
    - AWS CLI: AWS 명령줄 인터페이스 v2
```

## 개발 환경
```
- 운영체제: Windows 11, macOS Sonoma
- IDE: IntelliJ IDEA
- 빌드 도구: Gradle
- JDK 버전: JDK 17
- 버전 관리: Git / Gitlab
- 기타 툴: Postman (API 테스트), Lombok
```

## 의존성 목록
### 주요 라이브러리 및 버전
- **Spring Boot Starter Libraries**
  - `spring-boot-starter-data-jpa`
  - `spring-boot-starter-oauth2-client`
  - `spring-boot-starter-security`
  - `spring-boot-starter-thymeleaf`
  - `spring-boot-starter-validation`
  - `spring-boot-starter-web`
  
- **Thymeleaf Extras**
  - `thymeleaf-extras-springsecurity6`

- **Lombok**
  - `lombok` (컴파일 전용)

- **개발 도구**
  - `spring-boot-devtools` (개발 전용)

- **데이터베이스**
  - `h2` (런타임 전용)
  - `mysql-connector-j` (런타임 전용)

- **테스트 라이브러리**
  - `spring-boot-starter-test`
  - `spring-security-test`
  - `junit-platform-launcher` (런타임 전용)

- **Swagger UI**
  - `springfox-boot-starter:3.0.0`

- **MapStruct**
  - `mapstruct:1.5.3.Final`
  - `mapstruct-processor:1.5.3.Final` (어노테이션 프로세서)

- **JWT**
  - `java-jwt:4.2.1`

- **AWS**
  - `spring-cloud-starter-aws:2.2.6.RELEASE`

- **이메일**
  - `spring-boot-starter-mail`

- **Redis**
  - `spring-boot-starter-data-redis`



## API 명세서
### 접속 관리
- [API명세서 링크](https://www.notion.so/elice-track/API-3736518d004341ffb20e39cedd8975d1)
