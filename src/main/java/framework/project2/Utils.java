package framework.project2;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Tree;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.*;

public class Utils {
    public static void printTree(Tree t, Parser parser) {
        printTree(System.out, t, parser);
    }

    public static void printTree(PrintStream writer, Tree t, Parser parser) {
        String[] ruleNames = parser != null ? parser.getRuleNames() : null;
        List<String> ruleNamesList = ruleNames != null ? Arrays.asList(ruleNames) : null;

        writer.println(printTreeRecursive(t, ruleNamesList, 0));
    }

    private static String printTreeRecursive(Tree t, List<String> ruleNames, int padding) {
        final int paddingIncrement = 2;
        String s = getNodeText(t, ruleNames);
        if (t.getChildCount() == 0) {
            throw new RuntimeException("qwq");
        } else {
            // the global buf must start with \n, no padding.
            StringBuilder buf = new StringBuilder();
            buf.append(" ".repeat(padding));
            buf.append("(").append(s);

            // if a Non-Terminal has all children terminal: print them in one line
            boolean allTerminal = true;
            for (int i = 0; i < t.getChildCount(); i++) {
                if (t.getChild(i).getChildCount() != 0) allTerminal = false;
            }
            if (allTerminal) {
                for (int i = 0; i < t.getChildCount(); i++) {
                    buf.append(' ').append(getNodeText(t.getChild(i), ruleNames));
                }
                buf.append(")\n");
                return buf.toString();
            }

            buf.append("\n");
            boolean hasLF = true;
            for (int i = 0; i < t.getChildCount(); ++i) {
                Tree child = t.getChild(i);
                if (child.getChildCount() == 0) {
                    if (hasLF)
                        buf.append(" ".repeat(padding + paddingIncrement));
                    // child is a terminal
                    buf.append(getNodeText(child, ruleNames));
                    buf.append(' ');
                    hasLF = false;
                } else {
                    if (!hasLF) {
                        buf.append("\n");
                    }
                    buf.append(printTreeRecursive(child, ruleNames, padding + paddingIncrement));
                    if (i + 1 < t.getChildCount() && t.getChild(i + 1).getChildCount() == 0) {
                        // we just finished a non-t, the next one is a terminal.
                        // erase the last \n, and append a ' '
                        buf.deleteCharAt(buf.length() - 1);
                        buf.append(' ');
                        hasLF = false;
                    } else if (i == t.getChildCount() - 1) {
                        // we are the last one.
                        buf.deleteCharAt(buf.length() - 1);
                        hasLF = false;
                    } else {
                        hasLF = true;
                    }
                }
            }
            buf.append("\n");
            buf.append(" ".repeat(padding)).append(")\n");

            return buf.toString();
        }
    }

    public static String getNodeText(Tree t, Parser recog) {
        String[] ruleNames = recog != null ? recog.getRuleNames() : null;
        List<String> ruleNamesList = ruleNames != null ? Arrays.asList(ruleNames) : null;
        return getNodeText(t, ruleNamesList);
    }

    private static String getNodeText(Tree t, List<String> ruleNames) {
        if (ruleNames != null) {
            if (t instanceof RuleContext) {
                int ruleIndex = ((RuleContext) t).getRuleContext().getRuleIndex();
                String ruleName = (String) ruleNames.get(ruleIndex);
                int altNumber = ((RuleContext) t).getAltNumber();
                if (altNumber != 0) {
                    return ruleName + ":" + altNumber;
                }

                return ruleName;
            }

            if (t instanceof ErrorNode) {
                return t.toString();
            }

            if (t instanceof TerminalNode) {
                Token symbol = ((TerminalNode) t).getSymbol();
                if (symbol != null) {
                    return symbol.getText();
                }
            }
        }

        Object payload = t.getPayload();
        return payload instanceof Token ? ((Token) payload).getText() : t.getPayload().toString();
    }

}
