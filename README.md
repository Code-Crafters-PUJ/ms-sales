# ms-commercial-sales

To run the microservice, you need to have the .env file created.

To do that:
1. Copy the example.env file and rename it to .env.
2. Then complete the environment variables with your own credentials.

To run this ms:

If you don´t have RabbitMQ running yet:
```bash
docker run -d --name rabbit-server -p 8060:15672 -p 5672:5672 rabbitmq:3.9-management
```

Compile the Java microservice application:
```bash
cd commercial.sales
mvn compile
mvn clean package -DskipTests

```

Compile the microservice, the database and the database replica:
```bash
cd ..
docker compose -f compose-app.yml build java_app
docker-compose build
```

Start the microservice, the database and the database replica:
```bash
docker compose -f compose-app.yml up java_app
docker-compose up
```