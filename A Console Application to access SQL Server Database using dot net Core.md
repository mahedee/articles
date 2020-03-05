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

#### Insert some data in Employee table
```SQL

INSERT INTO [Employee] (EmpCode, EmpFullName, Designation) 
VALUES ('S001', 'Md. Mahedee Hasan', 'Manager')

INSERT INTO [Employee] (EmpCode, EmpFullName, Designation) 
VALUES ('S002', 'Khaleda Islam', 'Senior Software Engineer')

INSERT INTO [Employee] (EmpCode, EmpFullName, Designation) 
VALUES ('S003', 'Tahiya Hasan Arisha', 'Software Engineer')

INSERT INTO [Employee] (EmpCode, EmpFullName, Designation) 
VALUES ('S004', 'Taiful Islam Leon', 'Project Manager')

````
### Step 3: Install NuGet packages
Install the following Nuget Packages
Right-click on Project and select "Manage NuGet Packages".
Click on the "Browse" tab and search for the following packages.

* Microsoft.Extensions.Configuration;
* Microsoft.Extensions.Configuration.FileExtensions;
* Microsoft.Extensions.Configuration.Json;
* System.Data.SqlClient.

### Step 4: Add appsettings.json file
Add appsettings.json file in the project as configuration file

![alt text](https://github.com/mahedee/Articles/blob/master/img/DBCore-04.png) 

Add the followig code in the appsettings.json file. This is actually the connection string for the application

*Appsettings.json*

```JSON
{
  "ConnectionStrings": {
    "Default": "Data Source=(LocalDB)\\MSSQLLocalDB;AttachDbFilename=D:\\Temp\\DataAccessConsole\\DataAccessConsole\\Data\\HRMDB.mdf;Integrated Security=True"
  }
}

```
This step is very important. We need copy appsettings.json to Directory where the application will run like below.

![alt text](https://github.com/mahedee/Articles/blob/master/img/DBCore-05.png) 

### Step 5: Create Model, DAL, BLL and config class to access data

#### Create AppConfig Class to get connection string.

*AppConfig.cs*

```C#
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace DataAccessConsole
{
    public static class AppConfig
    {
        private static IConfiguration _iconfiguration;
        static AppConfig()
        {
            GetAppSettingsFile();
        }

        public static void GetAppSettingsFile()
        {

            var builder = new ConfigurationBuilder()
                                 .SetBasePath(Directory.GetCurrentDirectory())
                                 .AddJsonFile("appsettings.json", optional: false, reloadOnChange: true);
            _iconfiguration = builder.Build();

            //return _iconfiguration;
        }

        public static string GetConnectionString()
        {
            return _iconfiguration.GetConnectionString("Default");
        }
    }
}
```

#### Create a Model class name Employee to access data of Employee table
*Employee.cs*

```C#
using System;
using System.Collections.Generic;
using System.Text;

namespace DataAccessConsole
{
    public class Employee
    {
        public int Id { get; set; }
        public string EmpCode { get; set; }
        public string FullName { get; set; }
        public string Designation { get; set; }

    }
}

```

### Create EmployeeDAL to access data from database.

*EmployeeDAL.cs*

```C#

using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;

namespace DataAccessConsole
{
    public class EmployeeDAL
    {
        public string connectionString;
        public EmployeeDAL()
        {
            connectionString = AppConfig.GetConnectionString();
        }

        public List<Employee> GetAllEmployee()
        {
            List<Employee> employeesList = new List<Employee>();
            try
            {
                using (SqlConnection con = new SqlConnection(connectionString))
                {
                    SqlCommand cmd = new SqlCommand();
                    cmd.Connection = con;
                    cmd.CommandType = CommandType.Text;
                    cmd.CommandText = "SELECT [Id], [EmpCode], [EmpFullName], [Designation] FROM EMPLOYEE";

                    con.Open();
                    SqlDataReader rdr = cmd.ExecuteReader();

                    while (rdr.Read())
                    {
                        employeesList.Add(new Employee
                        {
                            Id = Convert.ToInt32(rdr[0]),
                            EmpCode = rdr[1].ToString(),
                            FullName = rdr[2].ToString(),
                            Designation = rdr[3].ToString()
                        });
                    }
                }
            }
            catch(Exception exp)
            {
                throw exp;
            }

            return employeesList;
        }
    }
}
```

#### Create EmployeeBLL class. Business logic should write here.
*EmployeeBLL.cs*

```C#
using System.Collections.Generic;

namespace DataAccessConsole
{
    public class EmployeeBLL
    {
        public List<Employee> GetAllEmployee()
        {
            return new EmployeeDAL().GetAllEmployee();
        }
    }
}
```

#### Now modify Program Class as follows to display data 
*Program.cs*

```C#
using System;
using System.Collections.Generic;

namespace DataAccessConsole
{
    class Program
    {
        static void Main(string[] args)
        {
            List<Employee> employeeList = new EmployeeBLL().GetAllEmployee();

            employeeList.ForEach(item =>
            {
                Console.WriteLine("Code: " + item.EmpCode + " Full Name: " + item.FullName 
                    + " Designation: " + item.Designation);
            });
            Console.ReadKey();
        }
    }

}
```

### Output: Now run the application and you will see the following output.
![alt text](https://github.com/mahedee/Articles/blob/master/img/DBCore-06.png) 
 

