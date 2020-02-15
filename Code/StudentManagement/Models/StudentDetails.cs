using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace StudentManagement.Models
{
    public class StudentDetail
    {
        
        
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int ID { get; set; }

        [Required]
        [Column(TypeName="nvarchar(100)")]
        public string  Name { get; set; }

        [Required]
        [Column(TypeName = "varchar(11)")]
        public string Phone_Number { get; set; }

        [Required]
        [Column(TypeName = "varchar(10)")]
        public string Birth_Date { get; set; }


    }
}