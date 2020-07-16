## Implementation of swagger in a asp.net core web api using in memory database

Here, an application name School Management System is developed using asp.net core web api and in memory database. Swagger is implemented in this application.

**Tools and Technology used**
1. Visual studio 2019
2. Visual C#
3. ASP.NET Core Web Api
4. Entity Framework Core
5. In memory database

**Step 1: Create an asp.net core web api project**

* Type the project name as "SMS" aks School Management System.
* Select API Template
* Select "Configure for HTTPS"

**Step 2: Install in memory database provider for entity framework core**  
* Install nuget package ```Microsoft.EntityFrameworkCore.InMemory``` in the project   

**Step 3: Create model classes** 
* Create two model classes name Student and Teacher as follows  

```csharp
    public class Student
    {
        public int Id { get; set; }
        public string Class { get; set; }
        public string RollNo { get; set; }
        public string FullName { get; set; }
        public string FathersName { get; set; }
        public string MothersName { get; set; }
    }

```

```csharp
    public class Teacher
    {
        public int Id { get; set; }
        public string TeacherId { get; set; }
        public string FullName { get; set; }
        public string FathersName { get; set; }
        public string MothersName { get; set; }
    }
```

**Step 4: Add dbcontext class**  
* Add a db context class as follows
```csharp 
    public class SMSContext : DbContext
    {
        public SMSContext(DbContextOptions<SMSContext> options)
            : base(options)
        {
        }

        public DbSet<Student> Users { get; set; }

        public DbSet<Teacher> Posts { get; set; }
    }
```  

* Configure in memory database in the ConfigureService method of Startup class as follows. 

```csharp
    public void ConfigureServices(IServiceCollection services)
    {
        services.AddDbContext<SMSContext>(opt => opt.UseInMemoryDatabase("SMSContext"));
    }
```
**Step 5: Add Api Controller**  
* Add two api controller StudentController and TeacherController as follows
```csharp
    [Route("api/[controller]")]
    [ApiController]
    public class StudentsController : ControllerBase
    {
        private readonly SMSContext _context;

        public StudentsController(SMSContext context)
        {
            _context = context;
        }

        // GET: api/Students
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Student>>> GetUsers()
        {
            return await _context.Users.ToListAsync();
        }

        // GET: api/Students/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Student>> GetStudent(int id)
        {
            var student = await _context.Users.FindAsync(id);

            if (student == null)
            {
                return NotFound();
            }

            return student;
        }

        // PUT: api/Students/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [HttpPut("{id}")]
        public async Task<IActionResult> PutStudent(int id, Student student)
        {
            if (id != student.Id)
            {
                return BadRequest();
            }

            _context.Entry(student).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!StudentExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/Students
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [HttpPost]
        public async Task<ActionResult<Student>> PostStudent(Student student)
        {
            _context.Users.Add(student);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetStudent", new { id = student.Id }, student);
        }

        // DELETE: api/Students/5
        [HttpDelete("{id}")]
        public async Task<ActionResult<Student>> DeleteStudent(int id)
        {
            var student = await _context.Users.FindAsync(id);
            if (student == null)
            {
                return NotFound();
            }

            _context.Users.Remove(student);
            await _context.SaveChangesAsync();

            return student;
        }

        private bool StudentExists(int id)
        {
            return _context.Users.Any(e => e.Id == id);
        }
    }
```
```csharp
    [Route("api/[controller]")]
    [ApiController]
    public class TeachersController : ControllerBase
    {
        private readonly SMSContext _context;

        public TeachersController(SMSContext context)
        {
            _context = context;
        }

        // GET: api/Teachers
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Teacher>>> GetPosts()
        {
            return await _context.Posts.ToListAsync();
        }

        // GET: api/Teachers/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Teacher>> GetTeacher(int id)
        {
            var teacher = await _context.Posts.FindAsync(id);

            if (teacher == null)
            {
                return NotFound();
            }

            return teacher;
        }

        // PUT: api/Teachers/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [HttpPut("{id}")]
        public async Task<IActionResult> PutTeacher(int id, Teacher teacher)
        {
            if (id != teacher.Id)
            {
                return BadRequest();
            }

            _context.Entry(teacher).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!TeacherExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/Teachers
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [HttpPost]
        public async Task<ActionResult<Teacher>> PostTeacher(Teacher teacher)
        {
            _context.Posts.Add(teacher);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetTeacher", new { id = teacher.Id }, teacher);
        }

        // DELETE: api/Teachers/5
        [HttpDelete("{id}")]
        public async Task<ActionResult<Teacher>> DeleteTeacher(int id)
        {
            var teacher = await _context.Posts.FindAsync(id);
            if (teacher == null)
            {
                return NotFound();
            }

            _context.Posts.Remove(teacher);
            await _context.SaveChangesAsync();

            return teacher;
        }

        private bool TeacherExists(int id)
        {
            return _context.Posts.Any(e => e.Id == id);
        }
    }
```

**Step 6: Install Swagger**  
* Install nuget package ```Swashbuckle.AspNetCore```

**Step 6: Add and configure Swagger middleware**
* Add the Swagger generator to the services collection in the ConfigureServices method of startup class as follows.  
```csharp
    // This method gets called by the runtime. Use this method to add services to the container.
    public void ConfigureServices(IServiceCollection services)

        services.AddControllers();

        // Register the Swagger generator, defining 1 or more Swagger documents
        services.AddSwaggerGen();
    }
```

* In the Startup.Configure method, enable the middleware for serving the generated JSON document and the Swagger UI:

```csharp
public void Configure(IApplicationBuilder app)
{
    // Enable middleware to serve generated Swagger as a JSON endpoint.
    app.UseSwagger();

    // Enable middleware to serve swagger-ui (HTML, JS, CSS, etc.),
    // specifying the Swagger JSON endpoint.
    app.UseSwaggerUI(c =>
    {
        c.SwaggerEndpoint("/swagger/v1/swagger.json", "My API V1");
    });

    app.UseRouting();
    app.UseEndpoints(endpoints =>
    {
        endpoints.MapControllers();
    });
}

```

**Step 4: Run the application and Check**  
* Run the application 
* Browse ```https://<localhost>:<port>/swagger/index.html```


[Download source code](https://github.com/mahedee/code-sample/tree/master/SwaggerInMemory/SMS.Web)