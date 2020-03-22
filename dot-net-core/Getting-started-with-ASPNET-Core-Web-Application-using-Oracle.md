## Getting started with ASP.NET Core Web Application using Oracle

**Project overview**  
Here I create an asp.net core web application to show employee information of default hrm database of Oracle. This is actually an API application.

**Tools and Technology used**  
I used following tools and technology to develop the project – 

1.	Visual Studio 2019
2.	Visual C#
3.	ASP.NET Core 3.0

Let’s start the project.

**Step 1: Create a ASP.NET Core application**  
Go to File->New->Project->ASP.NET Core Web Application

![alt text](https://github.com/mahedee/Articles/blob/master/img/0120/01.png)

Click Next and type the application name as “HRM.Web”

![alt text](https://github.com/mahedee/Articles/blob/master/img/0120/02.png)

Now choose API Template. Select Framework ASP.NET Core 3.0
 
![alt text](https://github.com/mahedee/Articles/blob/master/img/0120/03.png)

**Step 2: Install ODP.NET Core from Nuget.org**  

Select Oracle.ManagedDataAccess.Core from Nuget Package Manager and Click Install
 
![alt text](https://github.com/mahedee/Articles/blob/master/img/0120/04.png)

**Step 3: Add the connection string in appsettings.json**  
Add the connection string as follows for oracle in appsettings.json   

```JSON

{
  "ConnectionStrings": {
    "DefaultConnection": "Data Source = (DESCRIPTION = (ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = MahedeePC)(PORT = 1521)))(CONNECT_DATA = (SERVER = DEDICATED)(SERVICE_NAME = XE))); User Id = hr; Password = hr;"
  },

  "Logging": {
    "LogLevel": {
      "Default": "Information",
      "Microsoft": "Warning",
      "Microsoft.Hosting.Lifetime": "Information"
    }
  },
  "AllowedHosts": "*"
}

```

**Step 4: Create IHRMDBContex and HRMDBContex class in Context folder**  

**IHRMDBContext.cs**  

```C#
using Oracle.ManagedDataAccess.Client;

namespace HRM.Web.Context
{
    public interface IHRMDBContext
    {
        OracleCommand GetCommand();
        OracleConnection GetConn();
    }
}
```

**HRMDBContext.cs**  

```C#
using Microsoft.Extensions.Configuration;
using Oracle.ManagedDataAccess.Client;

namespace HRM.Web.Context
{
    public class HRMDBContext : IHRMDBContext
    {
        private IConfiguration _config;
        private OracleConnection _connection;
        private OracleCommand _cmd;
        private string _connectionString;

        public HRMDBContext(IConfiguration configuration)
        {
            _config = configuration;
            _connectionString = _config.GetConnectionString("DefaultConnection");
        }

        public OracleConnection GetConn()
        {
            _connection = new OracleConnection(_connectionString);
            return _connection;
        }

        public OracleCommand GetCommand()
        {
            _cmd = _connection.CreateCommand();
            return _cmd;
        }
    }
}

```


**Step 5: Create model class employee in Model folder**  

**Employee.cs** 

```C#
namespace HRM.Web.Model
{
    public class Employee
    {
        public int Id { get; set; }
        public string FirstName { get; set;  }
        public string LastName { get; set; }
        public string Email { get; set; }
    }
}
```

**Step 6: Create IEmployeeRepository and EmployeeRepository class in Repository folder** 

```C#

IEmployeeRepository.cs
using System.Collections.Generic;
using HRM.Web.Model;

namespace HRM.Web.Repository
{
    public interface IEmployeeRepository
    {
        List<Employee> GetAllBranches();
    }
}

```

**EmployeeRepository.cs**  

```C#
using HRM.Web.Context;
using HRM.Web.Model;
using Oracle.ManagedDataAccess.Client;
using System;
using System.Collections.Generic;

namespace HRM.Web.Repository
{
    public class EmployeeRepository : IEmployeeRepository
    {

        private IHRMDBContext _hrmdbContext;

        public EmployeeRepository(IHRMDBContext hrmdbContext)
        {
            _hrmdbContext = hrmdbContext;
        }

        public List<Employee> GetAllBranches()
        {
            List<Employee> employees = new List<Employee>();

            using (OracleConnection con = _hrmdbContext.GetConn())
            {
                using (OracleCommand cmd = _hrmdbContext.GetCommand())
                {
                    try
                    {
                        con.Open();
                        cmd.BindByName = true;

                        cmd.CommandText = "SELECT * FROM EMPLOYEES";

                        //Execute the command and use DataReader to display the data
                        OracleDataReader reader = cmd.ExecuteReader();

                        while (reader.Read())
                        {
                            employees.Add(new Employee()
                            {
                                Id = Convert.ToInt32(reader["EMPLOYEE_ID"]),
                                FirstName = reader["FIRST_NAME"].ToString(),
                                LastName = reader["LAST_NAME"].ToString(),
                                Email = reader["EMAIL"].ToString()

                            });
                        }
                        reader.Dispose();
                        return employees;
                    }
                    catch (Exception ex)
                    {
                        throw (ex);
                    }
                    finally
                    {
                        con.Close();
                    }
                }
            }
        }
    }
}
```

**Step 7: Register the classes with its interface**

Modify the Startup class as follows

**Startup.cs**

```C#
using HRM.Web.Context;
using HRM.Web.Repository;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;

namespace HRM.Web
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }


        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
        
            services.AddSingleton<IHRMDBContext, HRMDBContext>();
            services.AddScoped<IEmployeeRepository, EmployeeRepository>();
            services.AddControllers();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.UseRouting();

            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });
        }
    }
}

```

**Step 5: Create a API controller** 
Now create a controller name Employee as follows
 
![alt text](https://github.com/mahedee/Articles/blob/master/img/0120/05.png)

Now build and run the application. In the post man type the following URL and you will see the output.  

http://localhost:62331/api/employee

![alt text](https://github.com/mahedee/Articles/blob/master/img/0120/06.png)
