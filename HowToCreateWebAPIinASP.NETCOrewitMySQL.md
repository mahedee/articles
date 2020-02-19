# How to Create a ASP.NET Core Web API with Entity Framework using My SQL

## Introduction

In this article I’ll show step by step instructions of correct use Entity Framework Core in your ASP.NET Core project with MySql Database and traditional `EF migrations`. All actions was made in Visual Studio 2019 on Windows 10 machine with installed  MySql server. In example used ASP.NET Core Wep API project (.NET Core v3.1).

First of all you have to know some basic questions.I discussed them below.

### What is ASP.NET Core?
ASP.NET Core is a cross-platform, high-performance, open-source framework for building modern cloud-based, Internet-connected applications. With ASP.NET Core, you can: Build web apps and services, IoT apps, and mobile backends. Use your favorite development tools on Windows, macOS, and Linux.

### What is Web API?
A Web API is an application programming interface for either a web server or a web browser. It is a web development concept, usually limited to a web application's client-side (including any web frameworks being used), and thus usually does not include web server or browser implementation details such as SAPIs or APIs unless publicly accessible by a remote web application

### What is Entity Framework?
Entity Framework is an Object Relational Mapper (ORM) which is a type of tool that simplifies mapping between objects in your software to the tables and columns of a relational database. Entity Framework (EF) is an open source ORM framework for ADO.NET which is a part of . NET Framework.

### What is ADO.NET ?
ADO.NET is a data access technology from the Microsoft .NET Framework that provides communication between relational and non-relational systems through a common set of components. ADO.NET is a set of computer software components that programmers can use to access data and data services from a database.

### What is ORM ?
Object-relational mapping in computer science is a programming technique for converting data between incompatible type systems using object-oriented programming languages. This creates, in effect, a "virtual object database" that can be used from within the programming language.

### What is MySQL?
MySQL is an open source relational database.MySQL is cross platform which means it runs on a number of different platforms such as Windows, Linux, and Mac OS etc.

## EF Core in ASP.NET Core Web API with MySql Database

This guide explains setting up a production-ready ASP.NET Core Web API using Entity Framework with MySQL Database. Our Web API can perform basic CRUD operations.

### In this article, you will learn how to
- Create a simple ASP.NET Core Web API which do CRUD Operations using Entity Framework(with My SQL Server)
- `EF migrations` with My SQL 
- Run and interact with it

### Required Tools
* ASP.NET Core
* Virtual Studio 2019
* My SQL Workbench
* Postman



## Step 1: MySQL Installation 
Following the steps to install MySQL in your windows 10
### Download MySQL
The general MySQL Installer download is available at [this link](https://dev.mysql.com/downloads/windows/installer/).MySQL Workbench can be installed using the Windows MSI Installer package. The MSI package bears the name `mysql-installer-web-community-version.msi`, where version indicates the MySQL Workbench version number, and arch the build architecture (winx64).

![MySQL](https://github.com/mahedee/Articles/blob/master/img/31.PNG "MySQL")

The MySQL Installer application can install, upgrade, and manage most MySQL products, including MySQL Workbench.

### Setup My SQL including MySQL Workbench
- Run the ``msi`` file
- Select `Developer Default` or `Full`

![Setup Type ](https://github.com/mahedee/Articles/blob/master/img/21.PNG "Setup Type")

- Then Select `Next`

![Check Requirements](https://github.com/mahedee/Articles/blob/master/img/22.PNG "Check Requirements")

- Then click `Execute` after downloaded all then select `Next`--> `Next` 

![Execute](https://github.com/mahedee/Articles/blob/master/img/22.PNG "Execute")

-  Configure `Type and Networking` as `Standalone MYSQL Server`,next configure show in the below

![Type and Networking](https://github.com/mahedee/Articles/blob/master/img/32.PNG "Type and Networking")

- Configure `Accounts and Rules` .Here set your password for `root` user
- Configure `Windows Service` ,keep it as default

![Windows Service](https://github.com/mahedee/Articles/blob/master/img/33.PNG "Windows Service")

Next-->

![Extension](https://github.com/mahedee/Articles/blob/master/img/34.PNG "Extension")

- After that click `Execute` ---> `Finish`

![Apply Configuration](https://github.com/mahedee/Articles/blob/master/img/35.PNG "Apply Configuration")

- Then start `MySQL Workbench`

![MySQL Workbench](https://github.com/mahedee/Articles/blob/master/img/36.PNG "MySQL Workbench")

You are set...
## Step 2: Create a Database using MySQL Workbench
You can use the MySQL Workbench GUI to create a database. You can also create a database programmatically but here's how to do it via the GUI.

In the following example, we create a new database called "StudentDB".
* Click your connection,and log in your account with password.

![MySQL Workbench](https://github.com/mahedee/Articles/blob/master/img/36.PNG "MySQL Workbench")

* Click the icon for creating a new schema (you'll find this on the Workbench toolbar):


* Enter the schema name (in this case, StudentDB) and the default collation, then click the Apply button

![new Database](https://github.com/mahedee/Articles/blob/master/img/37.PNG "new Database")

* You are prompted to review the SQL statement that will be run to create the database. To run the statement (and create the database) click Apply:

![SQL Statement](https://github.com/mahedee/Articles/blob/master/img/38.PNG "SQL Statement")

* You should see the following screen once the database has been created:

![Confirmation](https://github.com/mahedee/Articles/blob/master/img/39.PNG "Confirmation")

* The database has now been created. You will now see your new database listed under the `SCHEMAS` tab on the left pane:

![StudentDB Database](https://github.com/mahedee/Articles/blob/master/img/40.PNG "StudentDB Database")

## Step 3: Create a ASP.NET Core Web API

- From the File menu, select New > Project.

![Project Selector](https://github.com/mahedee/Articles/blob/master/img/17.PNG "Project Selector")

- Select the ASP.NET Core Web Application template and click Next.
- Name the project `CRUDWebAPIMySQL` and click Create.

![Configure Project](https://github.com/mahedee/Articles/blob/master/img/18.PNG "Configure Project")

- In the Create a new ASP.NET Core Web Application dialog, confirm that .NET Core and ASP.NET Core 3.1 are selected. Select the API template and click Create.

![Template Selector](https://github.com/mahedee/Articles/blob/master/img/19.PNG "Template Selector")

![CRUDWebAPIMySQL](https://github.com/mahedee/Articles/blob/master/img/20.PNG "CRUDWebAPIMySQL")

N.B. Unselect the  configure for Https
Press ``Ctrl+F5`` to run the app.

## Step 4: Adding Dependencies in ASP.NET Core

Before we start our project need a few dependencies. We will add them all by NuGet Package Manager.

![NuGet Package Manager](https://github.com/mahedee/Articles/blob/master/img/42.PNG "NuGet Package Manager")

The list of packages is below:
* Microsoft.EntityFrameworkCore
* Pomelo.EntityFrameworkCore.MySql
* Microsoft.EntityFrameworkCore.Tools
* Microsoft.EntityFrameworkCore.Design

![Dependencies](https://github.com/mahedee/Articles/blob/master/img/41.PNG "Dependencies")

I use Polemo provider instead Oracle provider (MySql.Data.EntityFrameworkCore) because Oracle’s connector doesn’t support EF migrations, also relationships there can be implemented only in Fluent.API. EF Tools I installed just for simplifying entering commands in Package Manager Console (Final Step of this story).
## Step 5: Adding Models and Database Context

For start we need basic EF Models and DbContext. In this example I will create simple models with user and his pets. This example also will demonstrate auto-creation of relationships by Entity Framework.
Firstly create a folder named `Models`,then create

``StudentDetail.cs`` Model Class

```C#
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace CRUDWebAPIWithMySQL.Models
{
    public class StudentDetail
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int ID { get; set; }

        [Required]
        [Column(TypeName = "nvarchar(100)")]
        public string Name { get; set; }

        [Required]
        [Column(TypeName = "varchar(11)")]
        public string Phone_Number { get; set; }

        [Required]
        [Column(TypeName = "varchar(10)")]
        public string Birth_Date { get; set; }
    }
}
```

Then Create DBContext file named ``StudentDetailContext.cs``

```C#
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CRUDWebAPIWithMySQL.Models
{
    public class StudentDetailContext : DbContext
    {
        public StudentDetailContext(DbContextOptions<StudentDetailContext> options) : base(options)
        {

        }
        public DbSet<StudentDetail> StudentDetails { get; set; }
        //Table Name StudentDetails
    }
}

```
## Step 6 : Configure Dependency Injection

In this example I will save our connection string in `appsettins.json` file, because it useful in work with Git and Security measures.

```C#
"ConnectionStrings": {
    "DBConn": "Server=<Your IP>;port=3306;Database=studentdb;User Id=root;Password=1234;"
}
```
Here is our's

```C#
"ConnectionStrings": {
    "DBConn": "Server=192.168.80.68;port=3306;Database=studentdb;User Id=root;Password=1234;"
}
```

Now we must notify about existing our database context in `ConfigureServices` method of `Startup.cs` . NOTICE that in configuration you must use `AddDbContextPool` method instead `AddDbContext`. You must import this three. `Entity Framework`, `Pomelo EF` and your Model and DBContext class which are in `Models` folder.

```C#
using Pomelo.EntityFrameworkCore.MySql.Infrastructure;
using CRUDWebAPIWithMySQL.Models;
using Microsoft.EntityFrameworkCore;
```

Then adding `AddDbContextPool` method for connection.

```C#
public void ConfigureServices(IServiceCollection services)
{
   services.AddDbContextPool<Your DBContext Class name>(
      options => options.UseMySql(Configuration.GetConnectionString(<Connection String Name>)
   ));
   services.AddMvc();
}
```

```C#
 public void ConfigureServices(IServiceCollection services)
{

    services.AddDbContextPool<StudentDetailContext>(
        options => options.UseMySql(Configuration.GetConnectionString("DBConn")
        ));
    services.AddControllers();
}
```

Then create `Controller` Class. VS create it automatically.
Following the steps below.

- Click right button of mouse and `add controller class`

![Add Controller Class](https://github.com/mahedee/Articles/blob/master/img/43.PNG "Add Controller Class")

- Select `API Controller with action using EF`

![API Controller with action using EF](https://github.com/mahedee/Articles/blob/master/img/44.PNG "API Controller with action using EF")

- Select your Model class and our DB Context Class

![Model Class and Db Context Class](https://github.com/mahedee/Articles/blob/master/img/45.PNG "Model Class and Db Context Class")

## Step 7 : Migrations

If you installed EF Tools as me, than you can just run next 2 commands in Package Manager Console:

open your `Package Manager Console`

![Package Manager Console](https://github.com/mahedee/Articles/blob/master/img/46.PNG "Package Manager Console")

```c#
Add-Migration <MigrationName>
Update-Database
```
Here is mine,

```C#
Add-Migration InitialCreate
Update-Database
```

![Migration](https://github.com/mahedee/Articles/blob/master/img/27.PNG "Migration")

If any problem in your Migration delete your previous migrations.
```c#
Remove-Migration
```

After a successful migration , a table named `StudentDetails` in `studentdb` database

![Student Table](https://github.com/mahedee/Articles/blob/master/img/28.PNG "Student Table")

## Step 8 : Final Checking

We are now all set. I added some rows myself. Open `MySQL Workbench` ,then select your table then adding one row.

* Select 1st 100 rows

![Select 1st 100 rows](https://github.com/mahedee/Articles/blob/master/img/47.PNG "Select 1st 100 rows")

* Then Edit first Row ,not needed to edit `ID` because it's identity type(auto incremented)

![1st row](https://github.com/mahedee/Articles/blob/master/img/48.PNG "1st row")

* You are prompted to review the SQL statement that will be run to create the database. To run the statement (and create the database) click Apply:

![SQL Statement](https://github.com/mahedee/Articles/blob/master/img/49.PNG "SQL Statement")

Now go to controller class named `StudentDetailsController.cs` for getting your apis link. Now 

* build your project and run.

Here is an example.This API works for getting details of all student,

>http://localhost:2029/api/StudentDetails

Or `GET` request through ``Postman`` 

![StudentDetails](https://github.com/mahedee/Articles/blob/master/img/30.PNG "StudentDetails")

For adding row ,we can `POST` request through ``Postman`` 
