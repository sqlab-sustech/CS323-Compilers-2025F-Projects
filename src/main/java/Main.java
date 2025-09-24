import framework.project1.Grader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("testcases/project1/case1.splc");
        Grader grader1 = new Grader(fis);
        grader1.run();

//        System.out.println("---");
//
//        Grader grader = new Grader("1 + 2");
//        grader.run();
    }
}
