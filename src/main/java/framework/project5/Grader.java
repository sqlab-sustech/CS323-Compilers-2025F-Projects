package framework.project5;

import framework.AbstractCompiler;
import framework.AbstractGrader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;

public class Grader extends AbstractGrader {

    public Grader(InputStream sourceStream, OutputStream outputStream, OutputStream irOutputStream) {
        super(sourceStream, outputStream, irOutputStream);
    }

    @Override
    public void run() throws IOException {
        final String[] classes = {
                "impl.Compiler",
                "impl.semantic.Compiler",   // idk why somebody modified the framework...
        };
        AbstractCompiler compiler = null;

        for (String clazz : classes) {
            try {
                Class<?> compilerClass = Class.forName(clazz);
                Constructor<?> constructor = compilerClass.getDeclaredConstructor(AbstractGrader.class);
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                Object instance = constructor.newInstance(this);
                compiler = (AbstractCompiler) instance;
                break;
            } catch (Exception ex) {
                continue;
            }
        }
//        AbstractCompiler compiler = new Compiler(this);
        if (compiler == null) {
            System.err.println("Compiler class not found!");
            System.exit(2);
        }
        compiler.start();
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: framework.project5.Grader <source-splc-path> <ir-output-path>");
            System.exit(1);
        }
        String inputPath = args[0];
        String outputPath = args[1];
        var grader = new Grader(Files.newInputStream(Path.of(inputPath)), System.out, Files.newOutputStream(Path.of(outputPath)));
        grader.run();
    }
}
