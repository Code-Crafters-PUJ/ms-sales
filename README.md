# ms-commercial-sales

To run the microservice, you need to have the .env file created.

To do that:
1. Copy the example.env file and rename it to .env.
2. Then complete the environment variables with your own credentials.

To run this ms:


Compile the Java microservice application:
```bash
cd commercial.sales
mvn compile
```

Compile the microservice, the database and the database replica:
```bash
cd ..
docker-compose build
```

Start the microservice, the database and the database replica:
```bash
docker compose up -d
```