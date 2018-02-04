# tempvs
[![Circle CI](https://circleci.com/gh/ahlinist/tempvs/tree/master.svg?&style=shield)](https://circleci.com/gh/ahlinist/tempvs/tree/master)
## Getting started

To get started you need:
 * Java 1.8
 * Groovy 2.4.11
 * Grails 3.3.2
 * Gradle 4.4.1
 * PostgreSQL 9.5.10
 * MongoDB 3.2
 
## DB configuration

### PostgreSQL:
Connection parameters are retrieved from env variables:
 * JDBC_DATABASE_USERNAME
 * JDBC_DATABASE_PASSWORD
 * JDBC_DATABASE_URL

### MongoDB:
Connection parameters are retrieved from env variables:
 * MONGO_USER
 * MONGO_PASS
 * MONGO_HOST
 * MONGO_PORT
 * MONGO_DB_NAME
 