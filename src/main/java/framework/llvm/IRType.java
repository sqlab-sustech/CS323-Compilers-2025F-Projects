package framework.llvm;

public class IRType {
    private enum Type {
        int1,
        int32,
        structure,
        pointer,
        array,
    }
    private final Type type;
    private final String structureName;

    private final IRType arrayInner;
    private final int arraySize;

    // basic type constructor
    private IRType(Type type, String structureName, IRType arrayInner, int arraySize) {
        this.type = type;
        this.structureName = structureName;
        this.arrayInner = arrayInner;
        this.arraySize = arraySize;
    }

    public boolean typeEquals(IRType valueType) {
        if (!valueType.type.equals(this.type)) return false;
        if (valueType.type.equals(Type.pointer) || valueType.type.equals(Type.int1) || valueType.type.equals(Type.int32)) return true;
        if (valueType.type.equals(Type.structure)) return valueType.structureName.equals(this.structureName);
        if (valueType.type.equals(Type.array)) return valueType.arrayInner.typeEquals(this.arrayInner) && valueType.arraySize == this.arraySize;
        throw new RuntimeException("should not happen!");
    }

    public boolean isPointer() {
        return this.type.equals(Type.pointer);
    }

    public boolean isStructure() {
        return this.type.equals(Type.structure);
    }

    public boolean isArray() {
        return this.type.equals(Type.array);
    }

    public boolean isInteger() {
        return this.type.equals(Type.int32);
    }
    public boolean isBoolean() {
        return this.type.equals(Type.int1);
    }

    public String structureName() {
        assert this.isStructure();
        return this.structureName;
    }

    public IRType arrayNext() {
        return this.arrayInner;
    }

    private static final IRType INSTANCE_BOOL = new IRType(Type.int1, null, null, 0);
    private static final IRType INSTANCE_INT32 = new IRType(Type.int32, null, null, 0);
    private static final IRType INSTANCE_PTR = new IRType(Type.pointer, null, null, 0);

    public static IRType bool() {
        return INSTANCE_BOOL;
    }
    public static IRType int32() {
        return INSTANCE_INT32;
    }
    public static IRType pointer() {
        return INSTANCE_PTR;
    }
    public static IRType structure(String name) {
        return new  IRType(Type.structure, name, null, 0);
    }
    public static IRType array(IRType base, int... arraySize) {
        IRType next = base;
        for  (int i = arraySize.length - 1; i >= 0; i--) {
            int size = arraySize[i];
            next = new IRType(Type.array, null, next, size);
        }
        return next;
    }

    public int getAlignment() {
        return 8;
    }

    public String llvmName() {
        return switch (this.type) {
            case int1 -> "i1";
            case int32 -> "i32";
            case pointer -> "ptr";
            case structure -> "%struct." + structureName;
            case array -> String.format("[%d x %s]", arraySize, arrayInner.llvmName());
        };
    }

}
