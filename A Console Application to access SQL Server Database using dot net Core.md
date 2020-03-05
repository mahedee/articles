## A Console Application to access SQL Server Database using .NET Core

### Project Overview
This article is demonstrated how to access SQL Server database using .net core. In this case, I use simple console application.
Tools and Technology used
I used following tools and technology to develop the project – 

1.	Visual Studio 2019
2.	Visual C#
3.	.NET Core Console Application
4.	SQL Server Localdb

### Step 1: Create a Console Application
Create a console application and name it DataAccessConsole as follows.
![alt text](https://github.com/mahedee/Articles/blob/master/img/DBCore-01.png "Create a Console Application")

![alt text](https://github.com/mahedee/Articles/blob/master/img/DBCore-02.png "Create a Console Application")

### Step 2: Create a local database name “HRMDB” in Data folder
Add a local database in the project.
![alt text](https://github.com/mahedee/Articles/blob/master/img/DBCore-03.png) 

#### Create table name “Employee” in the database

```SQL
CREATE TABLE [dbo].[Employee](  
   [Id] [int] IDENTITY(1,1) NOT NULL,  
   [EmpCode] [varchar](100) NULL,
   [EmpFullName] [varchar](150) NULL,
   [Designation] [varchar](150) NULL,
   CONSTRAINT [PK_Employee] PRIMARY KEY CLUSTERED  
   (  
   [Id] ASC  
   )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON,    ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]  
) ON [PRIMARY]

```
