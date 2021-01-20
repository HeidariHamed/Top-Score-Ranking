# Top Score Game

Basic Java Spring Test: Top Score Ranking
Build Restful API for a simple Note-Taking application using Spring Boot, Mysql, JPA and Hibernate.

## Requirements

1. Java - 1.8.x

2. gradle - 6.x.x

3. Mysql - 5.x.x

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/HeidariHamed/Top-Score-Ranking.git
```

**2. Create Mysql database**
```bash
create database game
```
Exmaple file are provided in: src/resources/data.sql


**3. Change mysql username and password as per your installation**

+ open `src/main/resources/application.properties`

+ change `spring.datasource.username` and `spring.datasource.password` as per your mysql installation

**4. Build and run the app using gradle**

```bash
./gradlew -Pprod clean bootJar
java -jar build/libs/*.jar
```

Alternatively, you can run the app without packaging it using -

```bash
./gradlew
```

The app will start running at <http://localhost:8080>.

## Explore Rest APIs

The app defines following APIs.

Get Score

    GET /api/v1/player/{id}
    
Delete score

    DELETE /api/v1/player/delete/{id}
    
Get list of score

    GET /api/v1/list?name=hamed&after=2017-01-01&befre=2020-01-01
    
Player's history
    
    GET /api/v1/player/{player}
    
Add Player

    POST /api/v1/player
    
    

You can test them using postman or any other rest client.


## Testing

To launch your application's tests, run:

    ./gradlew test integrationTest 
    
    
