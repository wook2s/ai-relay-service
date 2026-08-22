# AI Async Relay Service

## 프로젝트 소개

WebFlux + R2DBC 기반의 AI 릴레이 서비스

## 기술 스택

- Java 21
- Spring Boot
- Spring WebFlux
- Spring Data R2DBC
- MariaDB
- UUID v7

## Current Status

- [x] MariaDB 환경 구성
- [x] Job 도메인 설계
- [x] R2DBC 연동
- [x] Job 생성 API
- [x] UUID v7 적용
- [x] Job 조회 API
- [ ] 비동기 Job Processor
- [ ] AI API 연동
- [ ] Retry / Timeout
- [ ] SSE
- [ ] 부하 테스트