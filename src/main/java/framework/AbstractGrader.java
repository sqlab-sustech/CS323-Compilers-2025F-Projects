package framework;

import framework.project3.Project3SemanticError;
import framework.project4.Project4Exception;
import framework.project4.Project4SemanticError;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGrader {
    private final InputStream sourceStream;
    private final PrintStream writer;
    private final List<Project3SemanticError> errors = new ArrayList<>();
    private final boolean isJudgeMode;

    public AbstractGrader(InputStream sourceStream, OutputStream outputStream) {
        this.sourceStream = sourceStream;
        this.writer = new PrintStream(outputStream);
        this.isJudgeMode = System.getenv("COMPILER_JUDGER") != null;
    }

    public AbstractGrader(InputStream sourceStream) {
        this(sourceStream, System.out);
    }

    public InputStream getSourceStream() {
        return sourceStream;
    }

    public abstract void run() throws IOException;

    public void reportSemanticError(Project3SemanticError semanticError) {
        this.errors.add(semanticError);
        this.writer.println(semanticError.printBase());
        this.writer.flush();
        this.onError(semanticError);
        System.exit(0);
    }

    public void reportSemanticError(Project4SemanticError error) {
        this.writer.println(error.message);
        this.writer.flush();
    }

    public void reportSemanticError(Project4Exception exception) {
        reportSemanticError(exception.error);
    }

    protected void onError(Project3SemanticError semanticError) {

    }

    public void print(String message) {
        this.writer.print(message);
        this.writer.flush();
    }

    /**
     * You may utilize this function to print your customized output (more detailed message)
     * , to implement an auto-test framework by yourself (like regression test).
     * These log will not show in Judger mode, which is controlled by the environment.
     */
    public void debugLog(String log) {
        if (!isJudgeMode) {
            this.writer.println(log);
            this.writer.flush();
        }
    }
}