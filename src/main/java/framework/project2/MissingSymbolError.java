package framework.project2;

public record MissingSymbolError(String symbolName, int lineno) {

    @Override
    public String toString() {
        return String.format("Missing Symbol '%s' at Line %d", symbolName, lineno);
    }
}
