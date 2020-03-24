# How to Dockerize both ASP.NET Core 3.1 and MySQL Server in Windows containers?

## Introduction

This article shows how to use MySQL in a Windows Docker container communicating with an ASP.NET Core 3.1 web app running on the host computer.
First of all you have to know,

### What is Docker?
Docker is a set of platform as a service products that uses OS-level virtualization to deliver software in packages called containers.Containers are isolated from one another and bundle their own software, libraries and configuration files; they can communicate with each other through well-defined channels

### What is ASP.NET Core?
ASP.NET Core is a free and open-source of web framework, and higher performance than ASP.NET, developed by Microsoft and the community. It is a modular framework that runs on both the full .NET Framework, on Windows, and the cross-platform .NET Core.

### What is MySQL?
MySQL is an open source relational database.MySQL is cross platform which means it runs on a number of different platforms such as Windows, Linux, and Mac OS etc.

## Dockerize both ASP.NET Core 3.1 and MySQL Server in Windows containers

This guide explains setting up a production-ready ASP.NET Core environment on Docker. Our Web Application can perform basic CRUD operations.

### In this article, you will learn how to

1. How to Set Up Docker in your Windows 10
2. Create a simple ASP.NET Core Web Application which do CRUD Operations using Entity Framework(with My SQL Server)
3. Created MySQL and ASP.NET Core Web Application container in Docker on Windows Container
4. Hosted both ASP.NET Core 3.1 and MySQL Server Container in Docker

### Required Tools

* Windows 10
* ASP.NET Core SDK 3.1
* Visual Studio
* VS Code
* Docker
### Step 1 . Install Docker in Windows 10
Docker Desktop for Windows is the Community version of Docker for Microsoft Windows. You can download Docker Desktop for Windows from Docker Hub.
[Download Link](https://hub.docker.com/editions/community/docker-ce-desktop-windows/)
It is a big file of 916 MB.

After downloading that--> 
- Double-click `Docker Desktop Installer.exe` to run the installer.

![Docker Install File](https://github.com/mahedee/Articles/blob/master/img/docker%20(1).PNG)

- Follow the Install Wizard: accept the license, authorize the installer, and proceed with the install.Here is Ours' Configuration

![Configuration Docker](https://github.com/mahedee/Articles/blob/master/img/docker%20(2).PNG)

- Click Finish to launch Docker.
- Docker starts automatically.
- Docker loads a “Welcome” window giving you tips and access to the Docker documentation

Docker Provides two Containers Linux and Windows . We will use Windows Container. We will use some experimental features. Experimental features are not ready for production. They are provided for test and evaluation in your sandbox environments. For that we will have to a little change in Docker. Firstly go to system tray.Then Press right buttor on ``Docker`` Logo 
``Settings`` --> `Docker Engine` --> ``"experimental": true``


![Docker Setting ](https://github.com/mahedee/Articles/blob/master/img/docker%20(6).PNG)

Our Final ``Docker Engine``

![Docker Engine ](https://github.com/mahedee/Articles/blob/master/img/docker%20(7).PNG)

Our ``Docker`` is now Ready....!

You can resize your docker memeory ,space. By Going to,
> C:\Users\UserName\AppData\Roaming\Docker\Settings.json

### Step 2. Create An ASP.NET Core Web App Using MySQL Database

- From the File menu, select New > Project.


![Project Selector](https://github.com/mahedee/Articles/blob/master/img/17.PNG "Project Selector")

- Select the ASP.NET Core Web Application template and click Next.

![WebAplicationMySQL](https://github.com/mahedee/Articles/blob/master/img/docker%20(3).PNG)

- Name the project `WebAplicationMySQL` and click Create.

![Configure Project](https://github.com/mahedee/Articles/blob/master/img/docker%20(4).PNG)

- In the Create a new ASP.NET Core Web Application dialog, confirm that .NET Core and ASP.NET Core 3.1 are selected. Select the MVC template and click Create.


N.B. Unselect the  configure for Https


## Step 3: Adding Dependencies in ASP.NET Core

Before we start our project need a few dependencies. We will add them all by NuGet Package Manager.

![NuGet Package Manager](https://github.com/mahedee/Articles/blob/master/img/42.PNG "NuGet Package Manager")

The list of packages is below:
* Microsoft.EntityFrameworkCore
* Pomelo.EntityFrameworkCore.MySql
* Microsoft.EntityFrameworkCore.Tools
* Microsoft.EntityFrameworkCore.Design

![Dependencies](https://github.com/mahedee/Articles/blob/master/img/docker%20(5).PNG "Dependencies")

I use Polemo provider instead Oracle provider (MySql.Data.EntityFrameworkCore) because Oracle’s connector doesn’t support EF migrations, also relationships there can be implemented only in Fluent.API. EF Tools I installed just for simplifying entering commands in Package Manager Console (Final Step of this story).

## Step 4: Adding Models and Database Context

For start we need basic EF Models and DbContext. In this example I will create simple models with user and his pets. This example also will demonstrate auto-creation of relationships by Entity Framework.
Firstly create a folder named `Models`,then create

``StudentDetail.cs`` Model Class

```C#
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace WebAplicationMySQL.Models
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

namespace WebAplicationMySQL.Models
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
## Step 5 : Configure Dependency Injection

Make sure this three must be include in `startup.cs` class.

![Packages](https://github.com/mahedee/Articles/blob/master/img/docker%20(27).PNG)

Let us configure our web application to use MySQL instead of SQLite. Open the Startup.cs file in your favorite editor and comment out (or delete) the following statement found in the `ConfigureServices()` method:

```c#
//services.AddDbContext<ApplicationDbContext>(options =>
//   options.UseSqlite(Configuration.GetConnectionString("DefaultConnection")));
```
Replace the above code with the following:

```C#
        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            //instead of "localhost" you can use your IP address.
            //For getting you IP Address go to `cmd` ,then type 'ipconfig'
            var host = Configuration["DBHOST"] ?? "localhost";

            var port = Configuration["DBPORT"] ?? "3306";

            var password = Configuration["DBPASSWORD"] ?? "1234";


            //StudentDetailContext is Our DB Context Class
            services.AddDbContextPool<StudentDetailContext>(
                    options =>
                    {
                        options.UseMySql($"server={host};userid=root;pwd={password};"
                        + $"port={port};database=StudentDB");
                    });
            services.AddControllersWithViews();
        }
```
Three environment variables are used in the database connection string. These are: ``DBHOST``, ``DBPORT`` and ``DBPASSWORD``. If these environment variables are not found then they will take up default values: `localhost`, `3306` and `1234` respectively.

We can instruct our application to automatically process any outstanding Entity Framework migrations. This is done by adding the following argument to the `Configure()` method in `Startup.cs`:

`StudentDetailContext` context

This means that our `Configure()` method will have the following signature:

```c#
public void Configure(IApplicationBuilder app, IHostingEnvironment env, StudentDetailContext context)
```
Next, add the following code inside the Configure() method in Startup.cs right before 
``app.UseRouting();``

``context.Database.Migrate();``

Here is our Full Code,

```C#

        public void Configure(IApplicationBuilder app, IWebHostEnvironment env, StudentDetailContext context)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            else
            {
                app.UseExceptionHandler("/Home/Error");
            }
            app.UseStaticFiles();

            context.Database.Migrate();
            app.UseRouting();

            app.UseAuthorization();
            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllerRoute(
                    name: "default",
                    pattern: "{controller=StudentDetails}/{action=Index}/{id?}");
            });
        }
```
N.B. ``StudentDetailContext`` is ours' DB Context Class ,you wrote your own.
You can see we changed our  `app.UseEndpoints()` method. Just changed the `Controlled` name .Here ``StudentDetails`` is ours controller .

This is all you need to do in order to change the database configuration of our application so that it uses MySQL instead of SQLite.


Then create `Controller` Class. VS create it automatically.
Following the steps below.

- Click right button of mouse and `add controller class`

![Add Controller Class](https://github.com/mahedee/Articles/blob/master/img/docker%20(34).PNG "Add Controller Class")

- Select `MVC Controller with views`

![MVC Controller with views](https://github.com/mahedee/Articles/blob/master/img/docker%20(33).PNG )

Views in File Explorer...

s![MVC Controller with views](https://github.com/mahedee/Articles/blob/master/img/docker%20(28).PNG )

- Select your Model class and our DB Context Class

![Model Class and Db Context Class](https://github.com/mahedee/Articles/blob/master/img/45.PNG "Model Class and Db Context Class")

## Step 6 : Migrations

If you installed EF Tools as me, than you can just run next 2 commands in Package Manager Console:

open your `Package Manager Console`

![Package Manager Console](https://github.com/mahedee/Articles/blob/master/img/46.PNG "Package Manager Console")

```C#
Add-Migration <MigrationName>
```
Here is mine,

```C#
Add-Migration InitialCreate
```

![Migration](https://github.com/mahedee/Articles/blob/master/img/docker%20(37).PNG)

If any problem in your Migration delete your previous migrations.
```c#
Remove-Migration
```
N.B. we don't need `update Database` command
After a successful migration , when we Run this app,a table named `StudentDetails` in `studentdb` database


## Step 7 : Run Our Web App in Locally

go to ``Package Manager`` then type
```C#
dotnet build
dotnet run
```
Your application will run into this ipaddress
> localhost:5000

Home Page
![WebAplicationMySQL Home page](https://github.com/mahedee/Articles/blob/master/img/docker%20(15).PNG)

## Step 8 : Dockerfile for Running ASP.NET Core APP with My SQL Database

Now we need to start ``Docker`` . It is a heavy app,``Visual Studio`` is also a heavy app.So that I prefer use ``VS Code`` for doing the rest. Close your ``Visual Studio`` and follow the instruction below for opening your project in `VS Code`.
Open the folder where your project created.Then type `cmd` in topbar.

![cmd](https://github.com/mahedee/Articles/blob/master/img/docker%20(29).PNG)

then type ``code .`` in `cmd`. Your project will open in `VS Code`
![cmd](https://github.com/mahedee/Articles/blob/master/img/docker%20(30).PNG)

Our Project-->
![Our Project](https://github.com/mahedee/Articles/blob/master/img/docker%20(36).PNG)

We will generate the release version of the application by executing the following command from a terminal window in the root directory of your ASP.NET Core 3.1 project:

``dotnet publish -o dist``

We need to create a docker image that will contain the dotnet core 3.1 runtime. A suitable image for this purpose is: ``dotnet/core/aspnet:3.1-nanoserver-1903``

Create a text file named Dockerfile and add to it the following content:
```Dockerfile
FROM mcr.microsoft.com/dotnet/core/aspnet:3.1-nanoserver-1903 AS base
COPY dist /app
WORKDIR /app
EXPOSE 80/tcp
ENTRYPOINT ["dotnet", "WebApplicationMySQL.dll"]
```
``In VS Code press Ctrl+Shift+P ,then you can auto generated Docker file``
Above are instructions to create a Docker image that will contain our ASP.NET Core 3.1 application. I describe each line below:

```Dockerfile
FROM mcr.microsoft.com/dotnet/core/aspnet:3.1-nanoserver-1903 AS base
```	
Base image dotnet/core/aspnet:3.1-nanoserver-1903 will be used

```Dockerfile
COPY dist /app
```	
Contents of the dist directory on the host computer will be copied to directory /app in the container

```Dockerfile
WORKDIR /app	
```
The working directory in the container is /app

```Dockerfile
EXPOSE 80/tcp	
```
Port 80 will be exposed in the container

```Dockerfile
ENTRYPOINT ["dotnet", "WebApplicationMySQL.dll"]
```
The main ASP.NET Core 3.1 web application will be launched by executing ``dotnet WebApplicationMySQL.dll``

We will next compose a docker yml file that orchestrates the entire system which involves two containers: a MySQL database server container and a container that holds our application. In the root folder of your application, create a text file named ``docker-compose.yml`` and add to it the following content:

```yml
version: "3.4"

volumes:
 datafiles:

services:
 db:
  image: mysql
  volumes:
    - datafiles:/var/lib/mysql
  restart: always
  environment:
    MYSQL_ROOT_PASSWORD: 1234
 mvc:
  build:
   context: .
   dockerfile: Dockerfile
  depends_on:
   - db
  ports:
   - "8888:80"
  restart: always
  environment:
    - DBHOST=db
    - ASPNETCORE_ENVIRONMENT=Development

```
N.B. Indentention is a Must in yml file
Below is an explanation of what this file does.

We will be having two containers. Each container is considered to be a service. The first service is named db and will host MySQL. The second service is named mvc and will host our ASP.NET Core 3.1 web app.

The most current version of docker-compose is version 3.4. This is the first line in our docker-compose file.

* The MySQL Container

``image: mysql`` will be used for the MySQL container.

A volume named ``datafiles`` is declared that will host MySQL data outside of the container. This ensures that even if the MySQL container is decommissioned, data will not be lost.

``restart: always`` is so that if the container stops, it will be automatically restarted.

The root password will be secret when MySQL is configured. This is set by the ``MYSQL_ROOT_PASSWORD`` environment variable.

* The ASP.NET Core 3.1 Web Application Container

The container will be built using the instructions in the ``Dockerfile`` file and the context used is the current directory.

``depends_on`` indicated that the web app relies on the MySQL container (db) to properly function.

``Port 80`` in the mvc container is mapped to port ``8888`` on the host computer.

Just like in the db container, ``restart: always`` is so that if the container stops, it will be automatically restarted.

The environment variables needed by the web app are:

``- DBHOST ``pointing to the MySQL service and  
``- ASPNETCORE_ENVIRONMENT ``set to ``Development`` more. In reality, you should change this to Production one you determine that your web app container works as expected.


Open the ``terminal`` of ``VS Code``. Then run this two command ,

``docker-compose build``

![docker-compose build](https://github.com/mahedee/Articles/blob/master/img/docker%20(24).PNG)

``docker-compose up``

![docker-compose up](https://github.com/mahedee/Articles/blob/master/img/docker%20(25).PNG)

Point your browser to http://localhost:8888/ and you should see the main web page.

Home Page

![WebAplicationMySQL Home page](https://github.com/mahedee/Articles/blob/master/img/docker%20(15).PNG)

Our Edit Page,

![WebAplicationMySQL edit page](https://github.com/mahedee/Articles/blob/master/img/docker%20(13).PNG)

After Adding One Row,

![WebAplicationMySQL Home page](https://github.com/mahedee/Articles/blob/master/img/docker%20(23).PNG)

Now go to ``Docker`` . We will see our ``WebApplictionMySQL`` Image

![WebAplicationMySQL Docker Image](https://github.com/mahedee/Articles/blob/master/img/docker%20(8).PNG)

Explore that image will we two containers as in the `docker-compose.yml`
go to system tray.Then Press right button on ``Docker`` Logo.Then `Dashboard`

![WebAplicationMySQL Docker Containers](https://github.com/mahedee/Articles/blob/master/img/docker%20(18).PNG)

![WebAplicationMySQL Docker Containers](https://github.com/mahedee/Articles/blob/master/img/docker%20(9).PNG)

``WebAplicationMySQL_mvc_1`` container log

![WebAplicationMySQL Docker Containers](https://github.com/mahedee/Articles/blob/master/img/docker%20(10).PNG)


``WebAplicationMySQL_db_1`` container log

![WebAplicationMySQL Docker Containers](https://github.com/mahedee/Articles/blob/master/img/docker%20(11).PNG)


We also checks our containers though ``Windows Power Shell``. Write this command
```
docker ps -a
```
![WebAplicationMySQL Docker Containers](https://github.com/mahedee/Articles/blob/master/img/docker%20(12).PNG)

To ensure that the database works properly, please enter a Student details here and check your database into ``Docker`` `mysql` container.

In my case, I checked them through this process.
```
docker ps -a
docker exec -it <your_Container_ID> bash
mysql -u root -p
```
our user was `root` and password is `1234`
Then log in as `root` user
```SQL
show database;
use Students;
show tables;
describe Studentdetails;
Select * from Studentdetails;
```
Normal ``SQL`` commands

![WebAplicationMySQL_DB_1 Container](https://github.com/mahedee/Articles/blob/master/img/docker%20(16).PNG)

StudentDetails Table

![StudentDetails Table](https://github.com/mahedee/Articles/blob/master/img/docker%20(17).PNG)

> Full Code

[Full Project Link](https://github.com/AtiQ-Rahman/WebApplicationMySQL)

