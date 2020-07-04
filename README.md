# todo-service 

![Java CI](https://github.com/cwansart/testcontainers-university/workflows/Java%20CI/badge.svg)

## Run locally

### Start the database

After you configured the server you can run the application either manually by starting Open Liberty and putting your _war_ inside the dropins folder or by deploying it via your IDE.

Before you can access your application, you need to make sure the database is running. To do so start Docker and run:

```bash
$ docker-compose -f docker-compose.db.yml up
```

### Start the application

Then you can start the application via:
```bash
$ mvn -Plocalhost liberty:run
```
