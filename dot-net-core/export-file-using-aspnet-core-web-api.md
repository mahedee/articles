## Export file using asp.net core web api

**Tools and Technology used**
1. Visual studio 2019
2. ASP.NET Core Web Api
2. Visual C#
3. JQuery

Step 1: Create a blank solution name "FileExpoter"  
Step 2: Add a new asp.net core project in "FileExporer" solution  

* Type the project name as "ExportApi"
* Select API Template
* Select "Configure for HTTPS"

Step 3: Add a new asp.net core MVC Project in "File Explorer" Solution

* Type the project name as "ClientApp"
* Select Template - Web Application (Model-View-Controller)

Step 4: Keep files in a folder
* Create a folder name "files"
* Keep two files.  mahedeebio.pdf and booklist.xlsx in the files folder

Step 4:  Enable CORS in ExportApi application.
* Install nuget package - Microsoft.AspNetCore.Cors
* Modify the ConfigureServices and Configure Method in Startup.cs file as follows.

```csharp
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
            services.AddControllers();

            //Enable CORS - Cross-Origin resource sharing
            services.AddCors(options =>
            {
                options.AddPolicy("CorsPolicy",
                    builder => builder.AllowAnyOrigin()
                    .AllowAnyMethod()
                    .AllowAnyHeader()
                    );
            });

            ////If you want to enable any particular IP
            //services.AddCors(c =>
            //{
            //    c.AddPolicy("AllowOrigin", options => options.WithOrigins("https://localhost:44312"));
            //});
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.UseHttpsRedirection();

            app.UseRouting();

            // Enable CORS
            // Must be in between app.UseRouting and app.UseEndpoints
            app.UseCors("CorsPolicy");


            // Another of way of Allow CORS
            //app.UseCors(options => options.AllowAnyOrigin());
            //app.UseCors(options => options.WithOrigins("https://localhost:44312"));

            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });           
        }
    }
```

Step 5: Add a controller class in ExportApi project
* Create a controller - name ExportController 
* Select Template - "API Controller - Empty"
* Update the Export Controller as follows

```csharp
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Cors;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace ExportApi.Controllers
{
    [EnableCors("CorsPolicy")]
    [Route("api/[controller]")]
    [ApiController]
    public class ExportController : ControllerBase
    {
        //Get: api/Export/GetExcel
        [HttpGet]
        [Route("GetExcel")]
        public IActionResult GetExcel()
        {
            try
            {
                return Ok(ConverExceltoB64());
            }

            catch (Exception ex)
            {

                throw (ex);
            }
        }





        //Get: api/Export/GetPDF
        [HttpGet]
        [Route("GetPDF")]
        public IActionResult GetPDF()
        {
            try
            {
                return Ok(ConvertPDFtoB64());
            }

            catch (Exception ex)
            {
                throw (ex);
            }
        }

        //Get: api/Export/GetDynamicExcel
        [HttpGet]
        [Route("GetDynamicExcel")]
        public IActionResult GetDynamicExcel()
        {
            try
            {
                return Ok(BuildeExcel());
            }
            catch (Exception ex)
            {
                throw (ex);
            }
        }


        // Convert an excel file to Base64 
        private string ConverExceltoB64()
        {
            var docBytes = System.IO.File.ReadAllBytes(System.IO.Path.GetFullPath(@"files\booklist.xlsx"));
            string docBase64 = Convert.ToBase64String(docBytes);
            return (docBase64);
        }


        // Convert a pdf file to Base64
        private string ConvertPDFtoB64()
        {
            var docBytes = System.IO.File.ReadAllBytes(System.IO.Path.GetFullPath(@"files\mahedeebio.pdf"));
            string docBase64 = Convert.ToBase64String(docBytes);
            return (docBase64);
        }


        // Create an excel on the fly and return as Base64 format
        private string BuildeExcel()
        {
            StringBuilder table = new StringBuilder();
            table.Append("<table border=`" + "1px" + "`b>");
            table.Append("<tr>");
            table.Append("<td><b><font face=Arial Narrow size=3>ID</font></b></td>");
            table.Append("<td><b><font face=Arial Narrow size=3>Name</font></b></td>");
            table.Append("<td><b><font face=Arial Narrow size=3>Designation</font></b></td>");
            table.Append("</tr>");

            foreach (var item in GetEmployeeAll())
            {
                table.Append("<tr>");
                table.Append("<td><font face=Arial Narrow size=" + "14px" + ">" + item.Id.ToString() + "</font></td>");
                table.Append("<td><font face=Arial Narrow size=" + "14px" + ">" + item.Name.ToString() + "</font></td>");
                table.Append("<td><font face=Arial Narrow size=" + "14px" + ">" + item.Designation.ToString() + "</font></td>");
                table.Append("</tr>");
            }

            table.Append("</table>");
            byte[] temp = System.Text.Encoding.UTF8.GetBytes(table.ToString());
            return System.Convert.ToBase64String(temp);

        }


        // Return list of employee
        private List<Employee> GetEmployeeAll()
        {
            List<Employee> employees = new List<Employee>
            {
                new Employee(){Id = 1, Name = "Sabrina Jahan Sara", Designation = "Software Engineer"},
                new Employee(){Id = 2, Name = "Tahiya Hasan Arisha", Designation = "Sr. Software Engineer"},
                new Employee(){Id = 3, Name = "Ishrat Jahan Nusaifa", Designation = "Software Architect"},
                new Employee(){Id = 4, Name = "Nusrat Janan", Designation = "Project Manager"}
            };

            return employees;
        }
    }


    // Employee model class
    internal class Employee
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Designation { get; set; }
    }
}

```


Step 6: Modify index.cshtml in ClientApp Project
* Modify Views->Home->Index.cshtml as follows

```html
@{
    ViewData["Title"] = "Home Page";
}

<h4>Export file using asp.net web api</h4>
<button type="button" class="btn btn-primary" id="btnExcel">Export Excel</button>
<button type="button" class="btn btn-info" id="btnPDF">Export PDF</button>
<button type="button" class="btn btn-success" id="btnDynamicExcel">Export Dynamic Excel</button>



@* Implement Scripts section *@

@section Scripts{
    <script>
        $(document).ready(function () {
            //alert('working from document ready!!');
        })

        //Button's events
        $("#btnExcel").click(function () {
            //alert("Export button clicked");
            ExcelExportar.OnExportarClick();
        });

        $("#btnPDF").click(function () {
            //alert("Export for dynamic pdf clicked");
            PDFExportar.OnExportarClick();
        });

        $("#btnDynamicExcel").click(function () {
            //alert("Export button clicked");
            DyamicExcelExportar.OnExportarClick();
        });




        //Excel exporter - start
        var ExcelExportar = {
            OnExportarClick: function () {
                //alert('click on exporter!');
                $.ajax({
                    url: "https://localhost:44386/api/export/getexcel",
                    type: 'GET',
                    dataType: 'text',
                    success: ExcelExportar.DownloadExcel,

                    failure: function (data) {
                        alert('failur: ' + data.responseText);
                    }, //End of AJAX failure function
                    error: function (request) {
                        alert('error: ' + request.responseText);
                    } //End of AJAX error function
                });
            },

            DownloadExcel: function (data) {
                //alert('download excel');
                var createA = document.createElement('a');
                createA.setAttribute('id', 'linkDownload');
                createA.setAttribute('href', 'data:application/vnd.ms-excel;base64,' + data);
                document.body.appendChild(createA);

                createA.download = 'download_latest.xls';
                var selectorHref = document.getElementById('linkDownload');
                selectorHref.click();
                selectorHref.remove();
            }
        };

        //Excel exporter - End


        //PDF exporter - start

        var PDFExportar = {
            OnExportarClick: function () {
                //alert('click on pdf exporter!');
                $.ajax({
                    url: "https://localhost:44386/api/export/getpdf",
                    type: 'GET',
                    dataType: 'text',
                    success: PDFExportar.DownloadPDF,
                    failure: function (data) {
                        alert('failur: ' + data.responseText);
                    }, //End of AJAX failure function
                    error: function (request) {
                        alert('error: ' + request.responseText);
                    } //End of AJAX error function
                });
            },

            DownloadPDF: function (data) {

                // Display in a same page with a download link

                // Embed the PDF into the HTML page and show it to the user
                var obj = document.createElement('object');
                obj.style.width = '100%';
                obj.style.height = '842pt';
                obj.type = 'application/pdf';
                obj.data = 'data:application/pdf;base64,' + data;
                document.body.appendChild(obj);

                // Insert a link that allows the user to download the PDF file
                var link = document.createElement('a');
                link.innerHTML = 'Download PDF file';
                link.download = 'file.pdf';
                link.href = 'data:application/octet-stream;base64,' + data;
                document.body.appendChild(link);


                // Display in a new window
                //var objbuilder = '';
                //objbuilder += ('<object width="100%" height="100%"      data="data:application/pdf;base64,');
                //objbuilder += (data);
                //objbuilder += ('" type="application/pdf" class="internal">');
                //objbuilder += ('<embed src="data:application/pdf;base64,');
                //objbuilder += (data);
                //objbuilder += ('" type="application/pdf" />');
                //objbuilder += ('</object>');

                //var win = window.open("", "_blank", "titlebar=yes");
                //win.document.title = "My Title";
                //win.document.write('<html><body>');
                //win.document.write(objbuilder);
                //win.document.write('</body></html>');
                //layer = jQuery(win.document);


            }
        };

        //PDF exporter - end

        var DyamicExcelExportar = {
            OnExportarClick: function () {
                $.ajax({
                    url: "https://localhost:44386/api/export/GetDynamicExcel",
                    type: 'GET',
                    dataType: 'text',
                    success: DyamicExcelExportar.DownloadExcel,
                    failure: function (data) {
                        alert('failur: ' + data.responseText);
                    }, //End of AJAX failure function
                    error: function (request) {
                        alert('error: ' + request.responseText);
                    } //End of AJAX error function
                });
            },

            DownloadExcel: function (data) {
                //alert('download excel');
                var createA = document.createElement('a');
                createA.setAttribute('id', 'linkDownload');
                createA.setAttribute('href', 'data:application/vnd.ms-excel;base64,' + data);
                document.body.appendChild(createA);

                createA.download = 'download_latest.xls';
                var selectorHref = document.getElementById('linkDownload');
                selectorHref.click();
                selectorHref.remove();
            }
        };

    </script>
}
```

Step 7: Run Multiple projects
* Run both ExportApi and ClientApp project 
* To run both multiple project right click on solution->Properties->Startup Project->Multiple Start up project, Select both project as start.
* Click the button on button of index page and you will see the output.

[Download source code](https://github.com/mahedee/code-sample/tree/master/FileExportApi/FileExporter)