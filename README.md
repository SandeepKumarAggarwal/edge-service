This is the edge service which acts as the front door and proxy for the API calls. Rate limiting Feature is added as part of this version implementation
1. By default, it allows 2 request per minute per user for APIs which is configurable in the application YAML file
    ratelimit:
        default: 2
        time: 1
2. User register through Marketplace Portal, and get clientID and API rate limit based on the plan selection
 a. User http://localhost:8081/swagger-ui.html#/access-controller to configure rate limiting policy
{
  "api": "firstapi",
  "client": "123456",
  "id": 0,
  "rateLimit": 5,
  "rateLimitMinutes": 2,
  "user": "abc@gmail.com"
}

#Demo APIs Under Test
1. First API - http://localhost:8081/firstapi/summary
2. Second API - http://localhost:8081/firstapi/summary

#Docker Build
1. docker build -t ska/edge-service .
2. docker run -d -p 127.0.0.1:8081:8081/tcp --name edge-service-container ska/edge-service

#Technology Used
1. Springboot - For Edge Service & Demos APIs
2. Swagger UI - For listing down all API endpoints
3. Netflix Zuul - For using Pre Filter and Zuul routings to intercept each request
4. Docker - For Containerization
5. MySQL - To maintain client API registration information, and access tracking logs
5. Rest Assured - For Automation API Testing

#Logic
1. Zuul Pre Filter intercepts each incoming request
2. System considers access control at the client ID and API combination level if set otherwise default is taken
3. System maintains the access control tracking at the client, API level with first time access & count. These are reset if count is overshooting rate limit and time window is over
4. If rate limits are over for user & API combination and request is received within the time limit, system throws exception with HTTP Status Code as
{
    "timestamp": "2020-11-25T13:12:59.671+0000",
    "status": 429,
    "error": "Too Many Requests",
    "message": "too many requests in a given amount of time (rate limiting)",
    "path": "/secondapi/summary"
}

