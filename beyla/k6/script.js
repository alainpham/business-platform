import http from 'k6/http';
import { check, sleep } from 'k6';

// Define the base URL of your API
const BASE_URL = __ENV.BASE_URL || 'http://nginx:3333';

// Define the options for your test
export let options = {
    stages: [
        { duration: '30s', target: 20 }, // Ramp up to 50 virtual users over 1 minute
        { duration: '4m', target: 50 }, // Stay at 50 virtual users for 3 minutes
        { duration: '30s', target: 0 }   // Ramp down to 0 virtual users over 1 minute
    ],
};

// Define the main function that represents your test scenario
export default function () {
    let response;

    response = http.get(`${BASE_URL}/greeting`);

    check(response, {
        'Status 200': (r) => r.status === 200,
    });

    sleep(1);

    response = http.get(`${BASE_URL}/smoke`);

    check(response, {
        'Status 200': (r) => r.status === 200,
    });

    sleep(1);

    response = http.get(`${BASE_URL}/ping?delay=300ms`);

    check(response, {
        'Status 200': (r) => r.status === 200,
    });

    sleep(1);

}