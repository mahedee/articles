

### ASP.NET Core
ASP.NET Core is a free and open-source web framework, and higher performance than ASP.NET, developed by Microsoft and the community. It is a modular framework that runs on both the full .NET Framework, on Windows, and the cross-platform .NET Core.

### NGINX
Nginx is a web server which can also be used as a reverse proxy, load balancer, mail proxy and HTTP cache. The software was created by Igor Sysoev and first publicly released in 2004. A company of the same name was founded in 2011 to provide support and Nginx plus paid software.

Hosting ASP.NET Core Web API on Linux with Nginx
This guide explains setting up a production-ready ASP.NET Core environment on an Ubuntu 18.04 server.

In this article, you will learn how to
Create a simple ASP.NET Core Web API which do CRUD Operations using Entity Framework(with MS SQL Server)
Configure Nginx
Hosting ASP.NET Core web API on Linux
Run and interact with it

Required Tools
Linux Operating System (16.04 or above)
ASP.NET Core SDK 3.1
VS Code

#### Step 1 . Install Linux Operating System

The Ubuntu operating system is lean. As a result, installing Ubuntu on an older computer can bring it back to life. If the computer you want to install Ubuntu on doesn't have a DVD-ROM drive where you can install the operating system from a disc, you can use the Rufus USB Installer to take the ISO you download and use it to create a bootable USB Ubuntu installer. This article walks you through the process to create a bootable USB Ubuntu installer in Windows.

Full details for how to Create Ubuntu, Linux OS Bootable USB in Windows
Rufus USB Installer
Ubuntu Installer

Now your USB is ready to boot.

Press the Power button for your computer.
During the initial startup screen, press ESC, F1, F2, F8 or F10. (Depending on the company that created your version of BIOS, a menu may appear.)
When you choose to enter BIOS Setup, the setup utility page will appear.
Using the arrow keys on your keyboard, select the BOOT tab. All of the available system devices will be displayed in order of their boot priority. Select your USB Device

Now you can setup your Linux alongside your Windows.See Here (Dual Boot)
For customizing the partition for Ubuntu see here (Dual Boot)
N.B. Sometimes you may face dual Boot problem . This tutorial will help you

#### Step 2 : Installing ASP.NET Core SDK 3.1 in Linux
Register Microsoft key and feed
Before installing .NET, you'll need to:
Register the Microsoft key.
Register the product repository.
Install required dependencies.
This only needs to be done once per machine.
Open a terminal and run the following commands.

For opening terminal type ctrl+alt+t
wget -q https://packages.microsoft.com/config/ubuntu/19.04/packages-microsoft-prod.deb -O packages-microsoft-prod.deb
sudo dpkg -i packages-microsoft-prod.deb
N.B. sometimes dpkg is locked due to install two applications at a time. If dpkg is locked,
	1. Stop or wait till another install is completed
2. Run this two command
sudo rm /var/lib/dpkg/lock 
sudo dpkg --configure -a
3.If above two not working. try this
Install the .NET Core SDK
Update the products available for installation, then install the .NET Core SDK. In your terminal, run the following commands.

sudo apt-get update
sudo apt-get install apt-transport-https
sudo apt-get update
sudo apt-get install dotnet-sdk-3.1

After that check your dot net core using 
dotnet --version


Install the ASP.NET Core runtime
Update the products available for installation, then install the ASP.NET Core runtime. In your terminal, run the following commands.

sudo apt-get update
sudo apt-get install apt-transport-https
sudo apt-get update
sudo apt-get install aspnetcore-runtime-3.1


#### Step 3: Installing VS Code for write down the code ###
sudo snap install --classic code
####  Step 4  : Installing MS SQL Server in Linux
For Details go to this page .

#### Step 5 : Create Database Using Entity Framework
Open your linux Terminal ,for creating a webapi use this command
dotnet new webapi -o <ProjectName>
My Project Name was StudentManagement.
dotnet new webapi -o StudentManagement

Then go to StudentManagement folder simply type
cd StudentManagement
Open this full project in VS code just write in the cmd
Code .
Then install two extentions
c# and MSSL
Now we are creating our model class. 
Model represents domain specific data and business logic in MVC architecture. It maintains the data of the application. Model objects retrieve and store model state in the persistance store like a database.
Model class holds data in public properties. All the Model classes reside in the Model folder in MVC folder structure
For this operation you need SQL Server and InMemory
dotnet add package Microsoft.EntityFrameworkCore.SqlServer
dotnet add package Microsoft.EntityFrameworkCore.InMemory

Now write down your model class and DBContext Class.
Then go to the appsettings.json file add your connection strings

 > "ConnectionStrings": {
 >   "DBConn": "Server=<server IP>;Database=<Database Name>;User Id=SA;Password=Password123;"
 >}

Here is Our’s

>  "ConnectionStrings": {
>    "DBConn": "Server=localhost;Database=StudentDB;User Id=SA;Password=Password123;"

> },

After that go to Startup.cs file add this service for your Database Connection
services.AddDbContext<StudentDetailContext>(options => 
                options.UseSqlServer(this.Configuration.GetConnectionString("DBConn")));
Then import your model class here. Add this at top
using StudentManagement.Models;
Now go to your VS Code and open SQL Server. Add a connection there,
  "server": "localhost",
DataBase : StudentDB
  "username": "SA"
“Password” : “Password123”
Profile Name : SQL Server
We wrote as ours.
If complete you see this.
Again go to your VS Code terminal.
Now install our Entity Framework tool using this two commands
dotnet tool install --global dotnet-ef
dotnet add package Microsoft.EntityFrameworkCore.Design
Now our full environment is setup. Now we can update our database.Using twom commands,


dotnet-ef migrations add DBInitialize
dotnet-ef database update
If build succesful ,again go to MS SQL Extention. Disconnet the connection .Then reconnect that connection,you will see your desired table which you design in your Model Class.

#### Step 6: Creating Controller Class
Asp.net Core MVC Controllers are responsible for controlling the flow of the application execution. When you make a request (means request a page) to MVC application, a controller is responsible for returning the response to that request. The controller can perform one or more actions. The controller action can return different types of action results to a particular request.

Firstly we have to create environment for generating Controller Class.
dotnet add package Microsoft.VisualStudio.Web.CodeGeneration.Design
dotnet add package Microsoft.EntityFrameworkCore.Design
dotnet tool install --global dotnet-aspnet-codegenerator

Now you are ready .
dotnet aspnet-codegenerator controller -name <ControllerName> -async -api -m <Model Class> -dc <DB Context Class Name> -outDir Controllers

Here is ours
dotnet aspnet-codegenerator controller -name StudentDetailController -async -api -m StudentDetail -dc StudentDetailContext -outDir Controllers

Hello ,our ASP.NET Core Web API is ready…….

dotnet build
dotnet run

Then go to https://localhost:5001/WeatherForecast 
For better view install this extention

#### Step 7 : Installing Nginx
Open the terminal in Linux.Then update
sudo apt-get update
Then install nginx
sudo apt-get install nginx

If install check nginx
sudo service  nginx start
Then check the status
 sudo service  nginx status

Then edit the default file of sites-enable which is a folder of nginx 
sudo nano /etc/nginx/sites-enabled/default

Then write down this
`server {
	listen 80;	
	location / {
	proxy_pass http://localhost:5000;
	
	proxy_set_header Upgrade $http_upgrade;
	proxy_set_header Connection 'upgrade';
	proxy_set_header Host $host;
	proxy_cache_bypass $http_upgrade;
	
	}
}`

To save that press ctr+x+y

Then we create our own service that run our ASP.NET Core app in local host.
We create this service in /var/www 
cd /var/www
Then give permission to create a app here.for that 
sudo chown -R 777 /var/www 
Now we can create our own folder
mkdir StudentWebAPI
Then create a service for running our app.
sudo nano /etc/systemd/system/StudentWebAPI.service




`[Unit]
Description=Student Dotnetcore Web API

[Service]
WorkingDirectory=/var/www/StudentWebAPI
ExecStart=/usr/bin/dotnet /var/www/StudentWebAPI/StudentManagement.dll
Restart=always

RestartSec=10
KillSignal=SIGINT
SyslogIdentifier=Student Web API
Environment=ASPNETCORE_ENVIRONMENT=Production


[Install]
WantedBy=multi-user.target`



Our Nginx Configuration is done. 

#### Step 8 - Publish our dot net core WEB API
Go to your ASP.NET Core project again.
Invoke the UseForwardedHeaders method in Startup.Configure before calling UseAuthentication or similar authentication scheme middleware. Configure the middleware to forward the X-Forwarded-For and X-Forwarded-Proto headers:
// using Microsoft.AspNetCore.HttpOverrides;

app.UseForwardedHeaders(new ForwardedHeadersOptions
{
    ForwardedHeaders = ForwardedHeaders.XForwardedFor | ForwardedHeaders.XForwardedProto
});

app.UseAuthentication();


Proxies running on loopback addresses (127.0.0.0/8, [::1]), including the standard localhost address (127.0.0.1), are trusted by default. If other trusted proxies or networks within the organization handle requests between the Internet and the web server, add them to the list of KnownProxies or KnownNetworks with ForwardedHeadersOptions. The following example adds a trusted proxy server at IP address 10.0.0.100 to the Forwarded Headers Middleware KnownProxies in Startup.ConfigureServices:


// using System.Net;

services.Configure<ForwardedHeadersOptions>(options =>
{
    options.KnownProxies.Add(IPAddress.Parse("10.0.0.100"));
});

Now build our WebAPI
dotnet build
Then publish it in our 
dotnet publish -c release -o /var/www/StudentWebAPI/
We are all set…….

#### Step 9 
Running our Student Web API as service
sudo systemctl start StudentWebAPI.service
Then start Nginx
sudo service  nginx start

Install ufw and configure it to allow traffic on any ports needed.


sudo apt-get install ufw

sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp




sudo ufw enable
sudo ufw allow http
sudo ufw allow https 

Final Checking
Go to your controller file. Then we find the subdomain of  our desire result.
Here Our sub domain is api/StudentDetail
For this request  i have to go to this url 192.168.1.107/api/StudentDetail
