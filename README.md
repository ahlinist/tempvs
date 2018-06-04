# tempvs
[![Circle CI](https://circleci.com/gh/ahlinist/tempvs/tree/master.svg?&style=shield)](https://circleci.com/gh/ahlinist/tempvs/tree/master)

A social network for interested in historical reenactment

## Goal

Make reenactor profiles easier to share/review/maintain.

## Getting started

To get started you need:
 * Java 1.8
 * Groovy 2.4.11
 * Grails 3.3.5
 * Gradle 4.4.1
 * PostgreSQL 9.5.10
 * MongoDB 3.2
 
### Email messaging
Email messaging is held by a separate microservice and requires the following env variables being set up:
 * EMAIL_SERVICE_URL
 * EMAIL_SECURITY_TOKEN
 
### DB configuration

#### PostgreSQL:
Connection parameters are retrieved from env variables:
 * JDBC_DATABASE_USERNAME
 * JDBC_DATABASE_PASSWORD
 * JDBC_DATABASE_URL

#### MongoDB:
Connection parameters are retrieved from env variables:
 * MONGODB_URI (mongodb://\<user\>:\<pass\>@\<host\>:\<port\>/\<db_name\>)

#### Demodata population
Demodata admin user retrieves it's password from the following env var:
 * ADMIN_PASSWORD

## Running installations
### Stage
http://stage.tempvs.club
### Prod
http://tempvs.club

## Email messaging
Email messaging is encapsulated in a separate microservice (see: https://github.com/ahlinist/tempvs-email). It's url should be provided in EMAIL_SERVICE_URL env variable and EMAIL_SECURITY_TOKEN should match the TOKEN variable configured in remote email service.
