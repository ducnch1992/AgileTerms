# Agile terms



## Description:

This is an online dictionary about Agile for Axon Active members who want to learn about Agile.
Learners can search everything Agile-related and proper description of them will be displayed.
Terms and description are contributed by Agile experts and can be voted by learners.

## Clone the project

- Create a folder where you want to put the project
- Go to the folder, left click and choose "Git bash here", write the following command:


    git clone --single-branch --branch dev https://gitsource.axonactive.com/hcmc-itclass/agile-terms.git


## Maven test and compile:

     mvn test

## Build local database:

    docker run --name postgres --restart unless-stopped -e POSTGRES_DB=agile_term_local -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=aavn123 -p 5466:5432 postgres:14.4

## Run the application:

- Go to agile-terms -> src -> main -> java -> com.axonactive.agileterm -> AgileTermApplication
- Run the application
  (If there is a pop-up asked to enable lombok, click enable)

## Deploy the project:

## Swagger:

- URL for Swagger of test server:


    192.168.70.67:8081/api/swagger-ui.html

- Please change the URL and port accordingly to your project.

## Release Note:
- Version: v0.0.1


123 test