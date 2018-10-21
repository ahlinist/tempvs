# tempvs
[![Circle CI](https://circleci.com/gh/ahlinist/tempvs/tree/master.svg?&style=shield)](https://circleci.com/gh/ahlinist/tempvs/tree/master)

A social network for interested in historical reenactment

## Goal

Make reenactor profiles easier to share/review/maintain.

## Getting started

To get started you need:
 * Java 1.8
 * Groovy 2.4.11
 * Grails 3.3.8
 * Gradle 4.4.1
 * PostgreSQL 9.5.10
 
### Email messaging
Email messaging is held by a separate microservice (see: https://github.com/ahlinist/tempvs-email) and requires the following env variables being set up:
 * EMAIL_SERVICE_URL
 * EMAIL_SECURITY_TOKEN (should match the TOKEN variable on the opposite side)
 
### Graphic content
Images upload/view/deletion are held by a separate microservice (see: https://github.com/ahlinist/tempvs-image) and require the following env variables being set up:
 * IMAGE_SERVICE_URL
 * IMAGE_SECURITY_TOKEN (should match the TOKEN variable on the opposite side)
 
### Profile-to-profile messaging
Users may create conversations between their profiles with the help of messaging microservice (see: https://github.com/ahlinist/tempvs-message). It requires the following env variables being set up:
 * MESSAGE_SERVICE_URL
 * MESSAGE_SECURITY_TOKEN (should match the TOKEN variable on the opposite side)
 
### AMQP support
Some events (e.g. profile update) trigger information exchange between microservices to refresh the bounded contexts. To make queuing work set the following env variables:
 * CLOUDAMQP_URL
 * CLOUDAMQP_APIKEY
 
### DB configuration

#### PostgreSQL:
Connection parameters are retrieved from env variables:
 * JDBC_DATABASE_USERNAME
 * JDBC_DATABASE_PASSWORD
 * JDBC_DATABASE_URL

#### Demodata population
Demodata admin user retrieves it's password from the following env var:
 * ADMIN_PASSWORD

## Running installations
### Stage
http://stage.tempvs.club
### Prod
http://tempvs.club
