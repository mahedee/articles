# Implementation of CQRS Pattern in a ASP.NET Web Application
## Introduction
CQRS means Command Query Responsibility Segregation. It is not an entire architecture. CQRS is just a small pattern. The main idea behind CQS is: “A method should either change state of an object, or return a result, but not both". In other words, asking the question should not change the answer.
Implementing CQRS in any application can maximize its performance, scalability and security.
## Problem 
 In traditional architecture, the same data model is used to query and update a database. That's simple and works well for basic CRUD operations. In more complex applications, however, this approach can become unwieldy. For example, on the read side, the application may perform many different queries, returning data transfer objects (DTOs) with different shapes. Object mapping can become complicated. On the write side, the model may implement complex validation and business logic. As a result, you can end up with an overly complex model that does too much.

 Read and write workloads are often asymmetrical, with very different performance and scale requirements.

 ## Solution
 My version in CQRS, I have used two different services for read and write, using Read service to read data, and Write service to update data. Separate database are used for Read and Write database  and  both database has kept in sync using a message broker,RabbitMQ.
 
 ### Design

 ![IMAGE](../img/CQRS1.png)


As you can see, Read site and Write site are separate services. Both sites consist of different application and both have different databases. In the Write site, this service is only used for writting and has no return value from database and updated data get transfered to the Queue through sender. Again in Read Site, the service is only used for reading data and always returns required result. For auto syncing, the receiver should be in listening mood. When receiver figured any data out in queue, the read database updates through receiver.
 # Get Started
 ## Part:1 Write Service
First of all create an ASP.Net Core Api project for write name that project "WriteSite".
  
![IMAGE]()

Now create a new folder for Model class, name that folder "Students",in Students folder create a new model class named "Student.cs".

Paste the following code in Student.cs class:
```C#
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.ComponentModel.DataAnnotations;

namespace WriteSite.Students
{
    public class Student
    {
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }

    }
}

```
Again create another model class in Students folder and name that class "StudentDto.cs".

Paste the following code in the StudentDto.cs class:
 ```C#
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WriteSite.Students
{
    public class StudentDto
    {

        public string Name { get; set; }
        public string Email { get; set; }


    }
}
 ```
 Now create a new folder, name it "Command".In Command folder create a new Context class, "CommandContext.cs".

Paste the following code in CommandContext.cs class:
```C#
using Microsoft.EntityFrameworkCore;
using WriteSite.Students;

namespace WriteSite.Command
{
    public class CommandContext : DbContext
    {
        public CommandContext(DbContextOptions<CommandContext> options) : base(options) { }
        public DbSet<Student> Students1 { get; set; }

    }
}

```
Open appsettings.json
Add the following code

```C#
 {
   ....
     "AllowedHosts": "*",
  "ConnectionStrings": {
    "CommandContext": "Server=(localdb)\\MSSQLLocalDB;Database=CommandContext;Trusted_Connection=True;MultipleActiveResultSets=true"
  }
 }
```

Inside the Controller, create a controller class and name that class "WriteStudentController.cs".

Paste the folloowing code in  WriteStudentController.cs class:
```C#
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using WriteSite.Command;

using WriteSite.Students;




namespace WriteSite.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class WriteStudentController : ControllerBase
    {
        private readonly ICommandS _commands;
        public WriteStudentController(ICommandS commands)
        {

            _commands = commands ?? throw new ArgumentNullException(nameof(commands));


        }

        [HttpPost]
        public void RegisterStudent([FromBody] StudentDto value)
        {

            _commands.RegisterStudent(value.Name, value.Email);

        }
    }
}

```
this class only consists post method.

In Command folder, create a interface class and name that class "ICommandS.cs".
Paste the following code in ICommandS.cs class.
```C#

using WriteSite.Students;

namespace WriteSite.Command
{
    public interface ICommandS
    {
        Student RegisterStudent(string name, string email);
    }
}
```
For Implimenting interface class method, create a new class in Command folder and name that class "RegStudent.cs".
Paste the following code in RegStudent.cs class
```C#
using Newtonsoft.Json;
using RabbitMQ.Client;
using WriteSite.Send;
using WriteSite.Students;

namespace WriteSite.Command
{
    public class RegStudent : ICommandS
    {
        private readonly CommandContext _context1;
        public RegStudent(CommandContext commandContext)
        {
            _context1 = commandContext;
        }

        public Student RegisterStudent(string name, string email)
        {
            var newStudent = new Student() { Name = name, Email = email };
            var message = JsonConvert.SerializeObject(newStudent);
            Sender.EventGenerator(message);
            _context1.Students1.Add(newStudent);


            _context1.SaveChanges();

            return newStudent;
        }
    }
}
```
Here in RegisterStudent() Method , the updated data is converted into message. Then the message  passes to  a Sender class.
```C#
 
            var message = JsonConvert.SerializeObject(newStudent);
            Sender.EventGenerator(message);
            
    
```

now create a new class in a new foloder named "Sender" and name that class "Sender.cs".
```C#
using RabbitMQ.Client;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WriteSite.Send
{
    public class Sender
    {
        public static void EventGenerator(string message)
        {

            var factory = new ConnectionFactory() { HostName = "localhost" };

            using (var connection = factory.CreateConnection())
            {
                using (var channel = connection.CreateModel())
                {
                  
                    var body = Encoding.UTF8.GetBytes(message);
                    channel.ExchangeDeclare("studentExchange1", ExchangeType.Direct);
                    channel.QueueDeclare("student1", false, false, false, null);
                    channel.QueueBind("student1", "studentExchange1", "", null);
                    channel.BasicPublish("studentExchange1", "", null, body);
                }
            }


        }
    }
}
```
 the Sender.cs class is responsible for sending message to the Queue (Rabbit.MQ) and Queue will send the message to the corresponding receiver.

 Back to the RegStudent.cs class, after passing the message to a message broker, the Write Database will also be updated.
 ```C#
 _context1.Students1.Add(newStudent);


            _context1.SaveChanges();

            return newStudent;
 ```

Open startup.cs class, Add the following code:
```C#
 public void ConfigureServices(IServiceCollection services)
        {
            services.AddControllers();
            services.AddScoped<ICommandS, RegStudent>();
            services.AddDbContext<CommandContext>(option => option.UseSqlServer(Configuration.GetConnectionString("CommandContext")));


        }
```
For creating a database table based on Student.cs model class, adding Migration is needed. 

Open Nuget Package Manager ->Package Manager Console 
and Paste the command in PMC
```
Enable-Migrations
add-migration
CommandMigration
Update-Database
```


## Part : 2 Read Service

 First of all create an ASP.Net Core Api project for write name that project "ReadSite".
  
![IMAGE]()

Now create a new folder for Model class and name that folder "Students". In Students folder create a new model class named "Student.cs".

Paste the following code in Student.cs class:
```C#
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.ComponentModel.DataAnnotations;

namespace ReadSite.Students
{
    public class Student
    {
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }

    }
}

```

Now create a new folder, name it "Query". In Query folder create a new Context class, "QueryContext.cs".

Paste the following code in QueryContext.cs class:
```C#
using Microsoft.EntityFrameworkCore;
using ReadSite.Students;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ReadSite.Query
{
    public class QueryContext : DbContext
    {

        public QueryContext(DbContextOptions<QueryContext> options) : base(options)
        {
        }

        public DbSet<Student> Students { get; set; }
    }
    
}

```
Open appsettings.json
Add the following code

```C#
 {
   ....
   "ConnectionStrings": {
    "QueryContext": "Server=(localdb)\\MSSQLLocalDB;Database=QueryContext;Trusted_Connection=True;MultipleActiveResultSets=true"

 }
```
Inside the Controller, create a new controller class and name that class "ReadStudentController.cs".

Paste the folloowing code in ReadStudentController.cs class:
```C#
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using ReadSite.Query;

namespace ReadSite.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class ReadStudentController : ControllerBase
    {
        private readonly IQueries _queries;
        private readonly IUpdateEvent _updateEvent;


        public ReadStudentController(IQueries queries, IUpdateEvent updateEvent)
        {
            _queries = queries ?? throw new ArgumentNullException(nameof(queries));
          
            _updateEvent = updateEvent ?? throw new ArgumentNullException(nameof(updateEvent));

        }

        // GET api/values
        [HttpGet]
        public async Task<ActionResult<IEnumerable<object>>> Get()
        {
            var students = Receiver.ReceiveEvent();
            if (students.Count > 0)
            {
                _updateEvent.updatestudent(students);
            }
            return (await _queries.GetAllStudent()).ToList();
        }
    }
}

```
Before returning the required result of Get Method, ReadStudentControllerClass.cs checks the Receiver if Receiver has any data then it will go to Receiver class and update Read database.

In Query folder create a interface class name that class "IQueries.cs".
Paste the following code in IQueries.cs class.
```C#
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ReadSite.Query
{
    public interface IQueries
    {
        Task<IEnumerable<object>> GetAllStudent();


    }
}

```

For Implimenting interface class method, create a new class in Query folder and name that class "GetStudent.cs".
Paste the following code in GetStudent.cs class
```C#
using Dapper;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Threading.Tasks;
using ReadSite.Students;

namespace ReadSite.Query
{
    public class GetStudent : IQueries
    {
        private readonly string _connectionString;
        public GetStudent(string connectionString)
        {
            if (string.IsNullOrEmpty(connectionString))
            {
                throw new ArgumentException("message", nameof(connectionString));
            }


            _connectionString = connectionString;

        }
        public async Task<IEnumerable<object>> GetAllStudent()
        {
            using (var conn = new SqlConnection(_connectionString))
            {
                conn.Open();


                return await conn.QueryAsync<object>("SELECT * FROM dbo.Students;");
            }
        }
    }

}

```

Create a new Folder named "Receiver". In Receiver folder create a new class and name that class "UpdateEvent.cs".
Paste the following code in UpdateEvent.cs class.
```C#
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ReadSite.Students;

namespace ReadSite.Query
{
    public interface IUpdateEvent
    {
        void updatestudent(List<Student> students);
    }
    public class UpdateEvent : IUpdateEvent
    {
        private readonly QueryContext _context1;
        public UpdateEvent(QueryContext commandContext)
        {
            _context1 = commandContext;
        }

        public void updatestudent(List<Student> students)
        {
            foreach (var student in students)
            {
                _context1.Add(student);
            }


            _context1.SaveChanges();
        }
    }
}

```
In Receiver folder, create a new class named "Receiver.cs"

Paste the following code:
```C#
using RabbitMQ.Client;
using System;
using System.Text;
using ReadSite.Students;
using ReadSite.Query;
using RabbitMQ.Client.Events;
using Newtonsoft.Json;
using System.Collections.Generic;

class Receiver
{

    public static List<Student> ReceiveEvent()
    {

        List<Student> students = new List<Student>();
        var factory = new ConnectionFactory() { HostName = "localhost" };
        using (var connection = factory.CreateConnection())
        using (var channel = connection.CreateModel())
        {
           
          
            channel.ExchangeDeclare("studentExchange1", ExchangeType.Direct);
            channel.QueueDeclare("student1", false, false, false, null);
            channel.QueueBind("student1", "studentExchange1", "", null);

            bool noAck = true;
            var response = channel.QueueDeclarePassive("student1");
            for (int i = 0; i < response.MessageCount; i++)
            {
                BasicGetResult result = channel.BasicGet("student1", noAck);
                if (result != null)
                {
                    var body = result.Body;
                    var message = Encoding.UTF8.GetString(body);
                    var student = JsonConvert.DeserializeObject<Student>(message);
                    students.Add(student);
                }
            }
            return students;
        }
    }
}

```
Here the Receiver.cs receive the message from Queue and write it to the Read database.


Open startup.cs class, Add the following code:
```C#
 public void ConfigureServices(IServiceCollection services)
        {
            services.AddControllers();
            services.AddScoped<IQueries, GetStudent>(ctor => new GetStudent(Configuration.GetConnectionString("QueryContext")));
          
            services.AddScoped<IUpdateEvent, UpdateEvent>();


            services.AddDbContext<QueryContext>(option => option.UseSqlServer(Configuration.GetConnectionString("QueryContext")));
       


        }
```
For creating a database table based on Student.cs model class, adding Migration is needed. 

Open Nuget Package Manager ->Package Manager Console 
and Paste the command in PMC
```
Enable-Migrations
add-migration
CommandMigration
Update-Database
```

> Full Project Link
[Implementing CQRS in ASP.Net Core](https://github.com/mahedee/Articles/blob/master/Code/CQRS_ASPDOTNET_CORE.zip)
