import http from 'k6/http';
import { check } from 'k6';
import { sleep } from 'k6';

export const options = {
    vus: 100,
    duration: '30s',
};
export default function () {

    // 1. Job 생성
    const createResponse = http.post(
        'http://localhost:8080/api/jobs',
        JSON.stringify({
            prompt: '부하 테스트'
        }),
        {
            headers: {
                'Content-Type': 'application/json',
            },
        }
    );

    check(createResponse, {
        'create status is 200': (r) => r.status === 200,
    });

    const jobId = createResponse.json('id');

    // 2. 완료될 때까지 조회
    for (let i = 0; i < 15; i++) {

        const response = http.get(
            `http://localhost:8080/api/jobs/${jobId}`
        );

        const status = response.json('status');

        if (status === 'COMPLETED' || status === 'FAILED') {
            break;
        }

        sleep(1);
    }
}