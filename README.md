# JVM Insight Server

Spring Boot 기반의 **JVM 관측(Observability) & GC 로그 분석** 서버입니다.  
로컬 JVM 프로세스(PID)에 붙어서 메모리/스레드 등의 스냅샷을 수집하고, GC 로그를 업로드하면 지표(총 GC 횟수, pause time, p95 등)를 계산해 리포트를 제공합니다.

> 목표: JVM을 이해 그리고 운영 관측 도구를 설계/구현

---

## Key Features (MVP)

### 1) JVM Snapshot Collection
* 입력: PID (로컬 JVM 프로세스)
* 수집 항목(초기):
  * Heap used/committed/max
  * Metaspace used/committed
  * Thread count 및 상태별(RUNNABLE/BLOCKED/WAITING) 분포
* 저장: time-series 형태로 DB에 적재

### 2) GC Log Upload & Analysis
* GC 로그 파일 업로드
* 분석 지표(초기)
  * 총 GC 횟수
  * 총/평균 pause time
  * p95 pause time
* 규칙 기반 코멘트(초기)
  * pause spike 감지
  * GC 빈번/지속 등 간단한 휴리스틱

### 3) Unified Report
* 특정 기간(from~to)의 스냅샷 + GC 분석 결과를 합쳐 **하나의 리포트(JSON)** 로 제공

---

## Tech Stack
* Java 21
* Spring Boot 4.0.3
* Spring Web / Validation
* Spring Data JPA
* Flyway
* H2 (dev) / PostgreSQL (prod)
* Actuator (optional) / Prometheus registry (optional)

---

## Getting Started

### Prerequisites
* java 21

### Run (H2 - Dev)
1) 프로젝트 실행
```bash
./gradlew bootRun
```
---
## API (Planned)

> 초기에는 Swagger(OpenAPI) 적용 예정입니다.
> 아래는 MVP 목표 스펙입니다.

### Targets
* POST /api/targets
* 요청: { "pid": 12345, "displayName": "local-demo" }
* GET /api/targets
* GET /api/targets/{id}

### Snapshots
* POST /api/targets/{id}/snapshots : 즉시 스냅샷 수집 
* GET /api/targets/{id}/snapshots?from=...&to=...

### GC Logs
* POST /api/gc-logs (multipart 업로드)
* GET /api/gc-logs/{id}/report

### Reports
* GET /api/targets/{id}/report?from=...&to=...

---
## Demo Scenario (Planned)
 > 1.	데모용 target 앱 실행 → PID 확인
 > 2.   POST /api/targets로 PID 등록
 > 3.	POST /api/targets/{id}/snapshots 호출하여 스냅샷 적재
 > 4.	GC 로그 업로드 → /api/gc-logs/{id}/report 확인
 > 5.	/api/targets/{id}/report로 기간 리포트 확인

---

## Roadmap
* PID 기반 JMX attach로 Snapshot 수집 구현
* Snapshot 스케줄러(10s/30s 간격 자동 수집)
* GC 로그 파서 1차 구현(G1 기준)
* 리포트 통합 API 구현
* PostgreSQL 프로파일 분리(application-postgres.yml)
* tagger(OpenAPI) + 예제 curl 문서화
* Prometheus/Grafana 대시보드 템플릿 제공
* Java Agent 기반 메소드 실행 추적(advanced)
