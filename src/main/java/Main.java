import framework.AbstractGrader;
import framework.project5.Grader;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        {
            InputStream input = new FileInputStream("project5_testcases/test03/splc.c");
//            OutputStream output = new FileOutputStream("testcases/project3/err_05.txt");
            OutputStream output = System.out;
            AbstractGrader grader = new Grader(input, output, System.out);
            grader.run();
        }
    }
}