## Api Gateway using Ocelot

### Context

Let's imagine an online store which is build on Microservices architecture pattern and you're building the order details page.

you need to develop multiple version of interfaces:

 - And Interective UI for desktop and mobile browsers.
 - Native mobile apps for android and ios apps.

 Also you need to expose the api for 3rd party applications.

 Now, Order details page need to display a lot of information.

 - products that ordered
 - prices of those products
 - total order value
 - Details of the customer who placed the order
 - delivery status
 and so on ...

 Since the application uses the microservice architecture pattern this required data is spread over multiple services

 - Product service
 - Pricing service
 - order service
 - customer service
 - delivery service ....

 So, you need to collect required informations from all of those services.

 ### Problem

 How do the client application collect those information from multiple services?

 - Client application need to make a round trip through multiple services to gather those required applications.
 - Different client need different data. For example, Mobile client usually need less data than the dektop client.
 - Network performance is different for different types of client. Typically a mobile network is much slower than non-mobile network.
 - Number of service instances and their location (host and port) changes dynamically.

 ### Solution

 Impleneting single point of entry for the application using api gateway.

### What is api gateway

Api Gateway is the entrypoint of the complete system. It sits in front of the application, receives request from client application and redirects to corresponding api endpoint.

### Why api gateway

Microservice architecture is getting widely accepted day by day. With the use of this architecture api gateway is getting more important. In microservice architecture single service is used for single operation and finally we end up with huge number of services. For example in an e-comerce project we have diffrent services like Customer service, Order Service, Cart service, Payment service and so on. And we have to make call to these different services for out client applications, Which is not ideal. If we use api gateway our client api will make call only to the api gateway and api gateway redirect the call to the corresponding apis.

Another benifit of api gatway is we can secure our application using the gateway. whenever client application makes a call to api gateway it will check if the user is permitted to access the corresponding service then redirects to it if permitted. 

## Building API Gateway using dotnet core and Ocelot

There are many API Gateways both Open Source and Commercial. For example, Apigee Edge, CA Api Gateway, IBM API Connect are commercial. You have to buy license to use them. Anypoint API Manager, Tyk API Gateway are Open source.

#### Ocelot Api Gateway

Ocelot is designed to work with .NET Core only and currently build on netstandard2.0. It is a lightweight product that performs fully basic functionalities of an API Gateway.

Create a new folder

```sh
mkdir ApiGateway
cd ApiGateway
```

### Step 1
Create two webapi called customerApi and OrderApi

#### CustomerApi

```sh
dotnet new webapi -o CustomerApi -n CustomerApi
```

Open this folder on your IDE/Code Editor (For example, visual studio code)

Now open CustomerApi project to launchSettings.json and set the applicationUrl port as 5001

```
{
  "$schema": "http://json.schemastore.org/launchsettings.json",
  "iisSettings": {
    "windowsAuthentication": false,
    "anonymousAuthentication": true,
    "iisExpress": {
      "applicationUrl": "http://localhost:5001",
      "sslPort": 0
    }
  },
  "profiles": {
    "IIS Express": {
      "commandName": "IISExpress",
      "launchBrowser": true,
      "launchUrl": "weatherforecast",
      "environmentVariables": {
        "ASPNETCORE_ENVIRONMENT": "Development"
      }
    },
    "CustomerApi": {
      "commandName": "Project",
      "launchBrowser": true,
      "launchUrl": "weatherforecast",
      "applicationUrl": "http://localhost:5001",
      "environmentVariables": {
        "ASPNETCORE_ENVIRONMENT": "Development"
      }
    }
  }
}
```

Then Create a class called customer

```sh
    public class Customer
    {
        public int CustomerId { get; set; }
        public string CustomerName { get; set; }
    }
```

Create a controller called CustomerController 

```sh
    public ActionResult<IEnumerable<Customer>> Get()
    {
        var customers = new List<Customer>{
            new Customer{ CustomerId = 1, CustomerName = "Onik"},
            new Customer{ CustomerId = 2, CustomerName = "mizan"}
        };
        return Ok(customers);
    }
```
Then, run the application using command

```
dotnet run 
```
or
```
dotnet watch run
```

[customerapiresponse](https://github.com/mahedee/Articles/blob/master/img/CustomerApi_Response.png)

#### OrderApi

```sh
dotnet new webapi -o OrderApi -n OrderApi
```

open launchSettings.json, set the applicationUrl port as 5002

```
{
  "$schema": "http://json.schemastore.org/launchsettings.json",
  "iisSettings": {
    "windowsAuthentication": false,
    "anonymousAuthentication": true,
    "iisExpress": {
      "applicationUrl": "http://localhost:5002",
      "sslPort": 0
    }
  },
  "profiles": {
    "IIS Express": {
      "commandName": "IISExpress",
      "launchBrowser": true,
      "launchUrl": "weatherforecast",
      "environmentVariables": {
        "ASPNETCORE_ENVIRONMENT": "Development"
      }
    },
    "OrderApi": {
      "commandName": "Project",
      "launchBrowser": true,
      "launchUrl": "weatherforecast",
      "applicationUrl": "http://localhost:5002",
      "environmentVariables": {
        "ASPNETCORE_ENVIRONMENT": "Development"
      }
    }
  }
}

```

Then Create a class called Order

```sh
    public class Order
    {
        public int OrderId { get; set; }
        public DateTime OrderDate { get; set; }
        public int CustomerId { get; set; }
    }
```

Create a controller called OrderController 

```sh
    public ActionResult<IEnumerable<Order>> Get()
        {
            var orders = new List<Order>{
                new Order{ OrderId = 1, OrderDate = DateTime.Now, CustomerId = 1},
                new Order{ OrderId = 2, OrderDate = DateTime.Now, CustomerId = 2},
                new Order{ OrderId = 3, OrderDate = DateTime.Now, CustomerId = 1}
            };
            return Ok(orders);
        }
```

Then, run the application using command

```
dotnet run 
```
or
```
dotnet watch run
```

[orderapiresponse](https://github.com/mahedee/Articles/blob/master/img/OrderApi_Response.png)

### Step 2

Create api gateway

```sh
dotnet new webapi -o OcelotApiGateway -n OcelotApiGateway
```

Add Ocelot to the project

```sh
dotnet add package Ocelot
```
Create a new json file in the project. For example, ocelot.json and add following codes there

```
{
    "ReRoutes": [
        {
        "DownstreamPathTemplate": "/api/{all}",
        "DownstreamScheme": "http",
        "DownstreamHostAndPorts": [
            {
                "Host": "localhost",
                "Port": 5001
            }
        ],
        "UpstreamPathTemplate": "/customer-api/{all}",
        "UpstreamHttpMethod": [ "Get" ]
        },
        {
        "DownstreamPathTemplate": "/api/{all}",
        "DownstreamScheme": "http",
        "DownstreamHostAndPorts": [
            {
                "Host": "localhost",
                "Port": 5002
            }
        ],
        "UpstreamPathTemplate": "/order-api/{all}",
        "UpstreamHttpMethod": [ "Get" ]
        }
    ],
    "GlobalConfiguration": {
        "BaseUrl": "https://localhost:5000"
    }
}
```

Open Program.cs and the file in the configuration

~~~
    public static IHostBuilder CreateHostBuilder(string[] args) =>
        Host.CreateDefaultBuilder(args)
            .ConfigureAppConfiguration((host, config) =>{
                config.AddJsonFile("ocelot.json");
            })
            .ConfigureWebHostDefaults(webBuilder =>
            {
                webBuilder.UseStartup<Startup>();
            });
~~~

Then goto startup.cs

Add

```
services.AddOcelot(Configuration);
```
in ConfigureServices method and add
```
await app.UseOcelot();
```
in Configure method

Now, run the application using command

```
dotnet run 
```
or
```
dotnet watch run
```

[ocelot-customer](https://github.com/mahedee/Articles/blob/master/img/OcelotCustomer.png)
[ocelot-order](https://github.com/mahedee/Articles/blob/master/img/Ocelot_Order.png)



