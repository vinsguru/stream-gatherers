# **External Services**

To keep the focus on learning **Stream Gatherers** and avoid distractions, all supporting services have been bundled into a single executable JAR `external-services.jar`.

Running this JAR will expose mocked API endpoints for:

* `product-service`
* `rating-service`
* `geo-data-service` (for final project)

These services are **heavily mocked and hardcoded**, designed specifically to help you explore and experiment with **advanced concurrency patterns using Stream Gatherers**.

## How To Run

- Ensure that you have Java 21 or anything above installed
- Please download [this jar](https://github.com/vinsguru/stream-gatherers/raw/refs/heads/main/02-external-services/external-services.jar)
- Keep the downloaded jar somewhere in your machine
- Open terminal/command line and navigate to the path where you have the jar.
- Run this below command.

```bash
java -jar external-services.jar
```
- It uses port `7070` by default.
- You can access the Swagger-UI using this URL - http://localhost:7070/swagger-ui.html

## Docker

If you do not want to run the jar directly on your machine, You can use docker. Build your image and run by doing the port mapping.
```Dockerfile
FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR app
ADD https://github.com/vinsguru/stream-gatherers/raw/refs/heads/main/02-external-services/external-services.jar external-services.jar
CMD java -jar external-services.jar
```

## To change the port

```bash
java -jar external-services.jar --server.port=8080
```


