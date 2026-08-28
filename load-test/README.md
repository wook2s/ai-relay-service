## Load Test

비동기 Job 처리 구조의 성능을 확인하기 위해 **k6**를 이용한 부하 테스트 진행.

### Test Environment

* Tool: k6
* Virtual Users: 100 VU
* Duration: 30s
* Scenario: `POST /api/jobs` → Job ID 획득 → `GET /api/jobs/{id}` 상태 Polling
* AI Client: MockAiClient
* AI Processing Delay: 5~10초 랜덤

### Result

| Metric               |      Result |
| -------------------- | ----------: |
| HTTP Requests        |       4,154 |
| HTTP Throughput      | 101.4 req/s |
| HTTP p95             |    60.76 ms |
| HTTP Error Rate      |          0% |
| Completed Iterations |         394 |
| Iteration p95        |      11.1 s |

### Analysis

Job 생성 API는 AI 처리와 분리된 비동기 구조로 구성하여 AI 처리에 5~10초의 지연이 발생하더라도 HTTP 응답은 바로 내려가도록 구현.
100 VU 환경에서 총 4,154건의 HTTP 요청을 처리했으며, **HTTP 요청 p95는 60.76ms, 오류율은 0%**를 기록.

