//using system;
//using system.collections.generic;
//using system.linq;
//using system.text;
//using system.threading.tasks;
using System;
using System.IO;

namespace test2
{
    internal class program
    {
        static void Main(string[] args)
        {
            // Ghi File
            FileStream fs = new FileStream("trunghung1.txt", FileMode.Create, FileAccess.Write);
            StreamWriter swrite = new StreamWriter(fs);
            swrite.Write("Le Trung Hung");
            swrite.Flush();
            fs.Close();
            // Doc File
            FileStream fs1 = new FileStream("trunghung1.txt", FileMode.Create, FileAccess.Write);
            StreamReader sread  = new StreamReader(fs1);
            sread.ReadToEnd();
            fs1.Close();
        }

    }
}







