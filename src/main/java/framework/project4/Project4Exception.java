package framework.project4;

public class Project4Exception extends RuntimeException {
    public final Project4SemanticError error;

    public Project4Exception(Project4SemanticError error) {
        this.error = error;
    }
}
