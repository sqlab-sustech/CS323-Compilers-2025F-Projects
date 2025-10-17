package framework.project2;

import java.io.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;

import generated.Splc.SplcBaseVisitor;
import generated.Splc.SplcLexer;
import generated.Splc.SplcParser;

import impl.project2.ConstExprVisitor;
import impl.project2.Project2ErrorListener;

public class Grader {
    private final InputStream sourceStream;
    private final PrintStream writer;

    private final boolean checkError;

    public Grader(InputStream sourceStream, OutputStream outputStream, boolean checkError) {
        this.sourceStream = sourceStream;
        this.writer = new PrintStream(outputStream);
        this.checkError = checkError;
    }

    public Grader(InputStream sourceStream, boolean checkError) {
        this(sourceStream, System.out, checkError);
    }

    public PrintStream getWriter() {
        return writer;
    }

    public void run() throws IOException {
        CharStream input = CharStreams.fromStream(sourceStream);
        SplcLexer lexer = new SplcLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SplcParser parser = new SplcParser(tokens);

        if (checkError) {
            parser.removeErrorListeners();
            parser.addErrorListener(new Project2ErrorListener(this));
        }

        ParseTree tree;

        if (checkError) {
            try {
                tree = parser.program();
            } catch (ParseCancellationException e) {
                return;
            }
        } else {
            // let ANTLR print all error
            tree = parser.program();
        }

        this.writer.println("Parse Tree:");
        Utils.printTree(this.writer, tree, parser);

        if (!checkError) {
            this.writer.println("constexpr:");
            new SplcBaseVisitor<Void>() {
                @Override
                // Attention: DO NOT MODIFY THIS FILE
                // Make your Splc.g4 compatible to this file.
                public Void visitVarDecStmt(SplcParser.VarDecStmtContext ctx) {
                    SplcParser.ExpressionContext expression = ctx.expression();
                    if (expression == null)
                        return null;
                    Integer value = new ConstExprVisitor().visit(expression);
                    if (value == null) {
                        writer.println("not a constexpr.");
                    } else {
                        writer.println(value);
                    }
                    return null;
                }
            }.visit(tree);
        }
    }
}