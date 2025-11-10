import http from 'k6/http';
import { sleep, check } from 'k6';

// --- 테스트 설정 ---

// 환경 변수로부터 어떤 API를 테스트할지 가져옵니다. (기본값: 'spatial')
// 'spatial' 또는 'geohash' 값을 가질 수 있습니다.
const API_TARGET = __ENV.API_TARGET || 'spatial';

// 대한민국 위경도 대략적인 범위
const MIN_LAT = 33.0;
const MAX_LAT = 38.5;
const MIN_LON = 125.0;
const MAX_LON = 132.0;

// --- 부하 시나리오 설정 ---
export const options = {
  stages: [
    { duration: '2m', target: 50 },  // 2분 동안 가상 사용자(VU) 50명까지 증가 (Ramp-up)
    { duration: '5m', target: 50 },  // 5분 동안 VU 50명 유지 (Constant Load)
    { duration: '1m', target: 0 },   // 1분 동안 VU 0명으로 감소 (Ramp-down)
  ],
  thresholds: {
    http_req_failed: ['rate<0.01'],   // HTTP 에러율이 1% 미만이어야 함
    http_req_duration: ['p(95)<1000'], // 95%의 요청이 1000ms(1초) 안에 처리되어야 함
  },
};

// --- 캐시 워밍업 (Setup) ---
// 본 테스트 실행 전에 딱 한 번 실행되는 함수입니다.
// 여기서 발생한 요청은 최종 통계에 포함되지 않습니다.
export function setup() {
  console.log(`Cache warm-up for '${API_TARGET}' API starting...`);
  const WARMUP_REQUESTS = 15;

  for (let i = 0; i < WARMUP_REQUESTS; i++) {
    const lat = Math.random() * (MAX_LAT - MIN_LAT) + MIN_LAT;
    const lon = Math.random() * (MAX_LON - MIN_LON) + MIN_LON;
    const radius = Math.floor(Math.random() * 1000) + 1;

    let url;
    if (API_TARGET === 'spatial') {
      url = `http://localhost:8080/api/pixels/search?latitude=${lat}&longitude=${lon}&radius=${radius}`;
    } else { // geohash
      url = `http://localhost:8080/api/pixels/search/geohash?latitude=${lat}&longitude=${lon}&radius=${radius}`;
    }
    http.get(url);
    sleep(0.2); // 워밍업 요청 사이에 짧은 간격
  }
  console.log('Cache warm-up finished.');
}


// --- 메인 테스트 로직 (Default Function) ---
// 각 가상 사용자가 반복적으로 실행하는 함수입니다.
export default function () {
  // 1. 랜덤 파라미터 생성
  const lat = Math.random() * (MAX_LAT - MIN_LAT) + MIN_LAT;
  const lon = Math.random() * (MAX_LON - MIN_LON) + MIN_LON;
  const radius = Math.floor(Math.random() * 1000) + 1;

  // 2. 로그로 남길 파라미터 JSON 생성 및 출력
  const requestParams = {
    api: API_TARGET,
    latitude: lat,
    longitude: lon,
    radius: radius,
  };
  console.log(JSON.stringify(requestParams));

  // 3. API URL 설정
  let url;
  if (API_TARGET === 'spatial') {
    url = `http://localhost:8080/api/pixels/search?latitude=${lat}&longitude=${lon}&radius=${radius}`;
  } else { // geohash
    url = `http://localhost:8080/api/pixels/search/geohash?latitude=${lat}&longitude=${lon}&radius=${radius}`;
  }

  // 4. HTTP 요청 및 결과 확인
  const res = http.get(url);
  check(res, {
    'status is 200': (r) => r.status === 200,
  });

  // 5. 실제 사용자처럼 요청 사이에 1초 대기
  sleep(1);
}
