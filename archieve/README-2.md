# SAGA Pattern| Implementing Distributed Transaction Using Microservices| Part-2
In this article, we will show the implementation structure of previously explained business logic ([see here](README.pdf)). 
For this purpose, we will try to cover the following,
* giving snapshot of project structure 
* giving snapshot of database tables
* giving snapshot of Rabbitmq queues
* code link of full project

## Required technology
* Spring Boot, for building microservices
* MySQL, for handling database
* RabbitMQ, for acting as message broker
* Postman, for initiating transaction in User Request service
* Eclipse or similar IDE that supports these technologies

## Project Structure
Here I am showing snapshots of structures of the microservices. Project structure has been made following layerd architecture. 

#### Project Structure for Bank Service

![Project Structure for Bank Service](images/BankServiceCropped.png)

#### Project Structure for Client Service

![Project Structure for Client Service](images/ClientServiceCropped.png)

#### Project Structure for Orchestrator

![Project Structure for Orchestrator](images/OrchestratorCropped.png)

#### Project Structure for User Request

![Project Structure for UserRequest](images/UserRequestCropped.png)

## Database Structure
Four distinct databases have been used for four microservices. MySql has been used for handling databases.
#### Database Tables
![Bank Service Tables](images/DB/BankServiceDBCropped.png) 
![Client Service Tables](images/DB/ClientServiceDBCropped.png)
![](images/DB/UserTransactionRequestDBCropped.png)

#### User Request table
![User Request Table](images/DB/UserRequestDB.png)

#### Client Ledger table
![Client Ledger Table](images/DB/ClientLedgerDB.png)

#### Client Master table
![Client Master Table](images/DB/ClientMasterDB.png)

#### Bank Ledger table
![Bank Ledger Table](images/DB/BankLedgerDB.png)

#### Bank Master table
![Bank Master Table](images/DB/BankMasterDB.png)

## RabbitMQ Queues
![RabbitMQ queues](images/RabbitMQ/queues.png)

## Postman 
Hitting endpoint of User Request through a post request and initiating transaction.
![postman hitting endpoint of user request](images/Postman/postRequest.png)