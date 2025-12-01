package framework.llvm;

public class IRValue {
    private final String name;
    private final IRType type;
    private final String constValue;

    public IRValue(String name, IRType type) {
        this.name = name;
        this.type = type;
        this.constValue = null;
    }

    private IRValue(IRType type, String constValue) {
        this.constValue = constValue;
        this.name = "";
        this.type = type;
    }

    public String name() {
        return name;
    }

    public IRType type() {
        return type;
    }

    public static IRValue constNull() {
        return new IRValue(IRType.pointer(), "null");
    }

    public static IRValue consti32(int value) {
        return new IRValue(IRType.int32(), String.valueOf(value));
    }

    public static IRValue constTrue() {
        return new IRValue(IRType.bool(), "true");
    }

    public static IRValue constFalse() {
        return new IRValue(IRType.bool(), "false");
    }

    public String llvmName() {
        if (constValue !=null) return constValue;
        if (name.startsWith("%")) return name;
        if (name.startsWith("@")) return name;
        else return "%" + name;
    }
}
