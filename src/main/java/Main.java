import framework.project2.Grader;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        {
            InputStream input = new FileInputStream("testcases/project2/correct_testcase_1.splc");
            Grader grader = new Grader(input, System.out, false);

            grader.run();
        }
        System.out.println("\n--------------------\n");
        {
            InputStream input = new FileInputStream("testcases/project2/error_testcase_1.splc");
            Grader grader = new Grader(input, System.out, true);

            grader.run();
        }
    }
}