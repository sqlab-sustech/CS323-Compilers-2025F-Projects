package framework.project4;

import framework.lang.Type;
import generated.Splc.SplcLexer;
import generated.Splc.SplcParser;
import org.antlr.v4.runtime.Token;

import java.util.Arrays;

public class Project4SemanticError {
    public final String message;

    public void throwException() {
        throw new Project4Exception(this);
    }

    public Project4SemanticError(SplcParser.ExpressionContext ctx, String errorClass, String... message) {
        this.message = String.format("Line %d: %s: ", ctx.getStart().getLine(), errorClass) + String.join("", message);
    }

    /***
     * Identifier is not a variable.
     */
    public static Project4SemanticError identifierNotVariable(SplcParser.ExpressionContext ctx, String ident) {
        return new Project4SemanticError(ctx, "badIdent", "identifier is not a variable: ", ident);
    }

    /***
     * Identifier is not a function.
     */
    public static Project4SemanticError identifierNotFunction(SplcParser.ExpressionContext ctx, String ident) {
        return new Project4SemanticError(ctx, "badIdent","identifier is not a function: ", ident);
    }

    /**
     * Use this function when:
     *  you expect such expression to have integer type, pointer type, or structure type, but it isn't.
     * @param ctx
     * @param actualTy the type of the given expression
     * @return
     */
    public static Project4SemanticError unexpectedType(SplcParser.ExpressionContext ctx, Type actualTy) {
        return new Project4SemanticError(ctx, "Unexpected Type", actualTy.prettyPrint());
    }

    /***
     * Use this function when:
     *  For these following binary operations, the type of lhs cannot match with the type of rhs, according to our document.
     *      - Binary PLUS/MINUS
     *      - Equality(==) / Unequality(!=)
     *      - Assignment (=)
     * @param ctx
     * @param token
     * @param lhs The type of the left-hand expression
     * @param rhs The type of the right-hand expression
     * @return
     */
    public static Project4SemanticError unmatchedTypeForBinaryOP(SplcParser.ExpressionContext ctx, Token token, Type lhs, Type rhs) {
        final int[] allowList = {
                SplcLexer.MINUS, SplcLexer.PLUS,    // Binary + / -
                SplcLexer.EQ, SplcLexer.NEQ,        // Equality checker
                SplcLexer.ASSIGN                    // Assignment
        };
        if (Arrays.stream(allowList).noneMatch(id -> id == token.getType()))
            throw new RuntimeException("You should not use this function to report error.");
        return new Project4SemanticError(ctx, "Unexpected Type", "for operator " + token.getText() +", lhs: " + lhs.prettyPrint() + ", rhs: " + rhs.prettyPrint());
    }

    public static Project4SemanticError badParamType(SplcParser.ExpressionContext ctx, int ithParam) {
        return new Project4SemanticError(ctx,"badCall", ithParam + "-th param mismatch");
    }

    public static Project4SemanticError badParamCount(SplcParser.ExpressionContext ctx, int requires, int given) {
        return new Project4SemanticError(ctx,"badCall", "param count mismatch, requires: " + requires + ", given: " + given);
    }

    public static Project4SemanticError badMember(SplcParser.ExpressionContext ctx, Type structTy, String memberName) {
        return new Project4SemanticError(ctx,"Struct", "'" + memberName + "' is not a member of " + structTy.prettyPrint());
    }

    public static Project4SemanticError lvalueRequired(SplcParser.ExpressionContext ctx) {
        return new Project4SemanticError(ctx,"lvalue", "lvalue is required.");
    }

}
