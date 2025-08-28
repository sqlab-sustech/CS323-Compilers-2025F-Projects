public class AnExampleClass {
    public final String name;

    public AnExampleClass(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "I am an example class, " +
                "name='" + name + '\'';
    }
}
