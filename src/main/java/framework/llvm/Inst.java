package framework.llvm;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Inst {
    public enum InstType {
        condBr,     // br i1 <cond>, label <label_true>, label <label_false>
        br,         // br label <label>
        ret,        // ret <ty> <value>

        add,        // <result> = add <ty> <op1>, <op2>
        sub,        // <result> = sub <ty> <op1>, <op2>
        mul,        // <result> = mul <ty> <op1>, <op2>
        div,        // <result> = sdiv <ty> <op1>, <op2>
        rem,        // <result> = srem <ty> <op1>, <op2>

        alloca,     // <result> = alloca <ty> [, align <alignment>]
        load,       // <result> = load <ty>, ptr <pointer>[, align <alignment>]
        store,      // store <ty> <value>, ptr <pointer>[, align <alignment>]
        gep,        // <result> = getelementptr <ty>, ptr <ptrval>{, <ty>(i32 only) <idx>}*

        icmp,       // <result> = icmp <cond> <ty> <op1>, <op2>
        call,       // <result> = call <ty> <fnptrval>(<args>)

        zext,       // <result> = zext <ty> <value> to <ty2>
    }

    public final InstType type;
    public final IRValue result;
    public final IRType ty;

    private Inst(InstType type, IRType ty, IRValue result) {
        this.type = type;
        this.ty = ty;
        this.result = result;
    }

    public static class TermInst extends Inst {
        public final IRValue condOrValue;
        public final String label1;
        public final String label2;

        private TermInst(InstType type, IRType ty, IRValue condOrValue, String label1, String label2) {
            super(type, ty, null);
            this.condOrValue = condOrValue;
            this.label1 = label1;
            this.label2 = label2;
        }

        @Override
        public String toLLVMRep() {
            return switch (type) {
                case condBr -> String.format("br i1 %s, label %%%s, label %%%s", condOrValue.llvmName(), label1, label2);
                case br -> String.format("br label %%%s", label1);
                case ret -> String.format("ret %s %s", ty.llvmName(), condOrValue.llvmName());
                default -> null;
            };
        }
    }

    public static class BinaryInst extends Inst {
        public final IRValue op1;
        public final IRValue op2;

        private BinaryInst(InstType type, IRValue result, IRType ty, IRValue op1, IRValue op2) {
            super(type, ty, result);
            this.op1 = op1;
            this.op2 = op2;
        }

        @Override
        public String toLLVMRep() {
            return switch (type) {
                case add -> String.format("%s = add %s %s, %s", result.llvmName(), ty.llvmName(), op1.llvmName(), op2.llvmName());
                case sub -> String.format("%s = sub %s %s, %s", result.llvmName(), ty.llvmName(), op1.llvmName(), op2.llvmName());
                case mul -> String.format("%s = mul %s %s, %s", result.llvmName(), ty.llvmName(), op1.llvmName(), op2.llvmName());
                case div -> String.format("%s = div %s %s, %s", result.llvmName(), ty.llvmName(), op1.llvmName(), op2.llvmName());
                case rem -> String.format("%s = rem %s %s, %s", result.llvmName(), ty.llvmName(), op1.llvmName(), op2.llvmName());
                default -> null;
            };
        }
    }

    public static class MemoryInst extends Inst {
        public final int alignment;
        public final IRValue pointer;
        public final IRValue value;
        public final List<IRValue> gepindices;

        public MemoryInst(InstType type, IRType ty, IRValue result, int alignment, IRValue pointer, IRValue value, List<IRValue> gepindices) {
            super(type, ty, result);
            this.alignment = alignment;
            this.pointer = pointer;
            this.value = value;
            this.gepindices = gepindices;
        }

        @Override
        public String toLLVMRep() {
            return switch (type) {
                case load -> String.format("%s = load %s, ptr %s, align %d", result.llvmName(), ty.llvmName(), pointer.llvmName(), alignment);
                case store -> String.format("store %s %s, ptr %s, align %d", ty.llvmName(), value.llvmName(), pointer.llvmName(), alignment);
                case gep -> String.format("%s = getelementptr %s, ptr %s, %s", result.llvmName(), ty.llvmName(), pointer.llvmName(),
                        this.gepindices.stream().map(idx -> String.format("%s %s", idx.type().llvmName(), idx.llvmName())).collect(Collectors.joining(","))
                );
                case alloca -> String.format("%s = alloca %s, align %d", result.llvmName(), ty.llvmName(), alignment);
                default -> null;
            };
        }
    }

    public static class ICmpInst extends Inst {
        public final LLVMIcmpPredicate cond;
        public final IRValue op1;
        public final IRValue op2;

        public ICmpInst(IRType ty, IRValue result, LLVMIcmpPredicate cond, IRValue op1, IRValue op2) {
            super(InstType.icmp, ty, result);
            this.cond = cond;
            this.op1 = op1;
            this.op2 = op2;
        }

        @Override
        String toLLVMRep() {
            return String.format("%s = icmp %s %s %s, %s", result.llvmName(), cond.llvmName(), ty.llvmName(), op1.llvmName(), op2.llvmName());
        }
    }

    public static class CallInst extends Inst {
        public final String funcName;
        public final List<IRValue> args;

        public CallInst(IRValue result, IRType ty, String funcName, List<IRValue> args) {
            super(InstType.call, ty, result);
            this.funcName = funcName;
            this.args = args;
        }

        @Override
        String toLLVMRep() {
            return String.format("%s = call %s @%s(%s)", result.llvmName(), ty.llvmName(), funcName,
                    args.stream().map(arg -> arg.type().llvmName() + " " + arg.llvmName()).collect(Collectors.joining(", ")));
        }
    }

    public static class ConversionInst extends Inst {
        public final IRValue result;
        public final IRType ty;
        public final IRValue value;
        public final IRType ty2;

        public ConversionInst(InstType instType, IRValue result, IRType ty, IRValue value, IRType ty2) {
            super(instType, ty, result);
            this.result = result;
            this.ty = ty;
            this.value = value;
            this.ty2 = ty2;
        }

        @Override
        String toLLVMRep() {
            return switch(type) {
                case InstType.zext ->  String.format("%s = zext %s %s to %s", result.llvmName(), ty.llvmName(), value.llvmName(), ty2.llvmName());
                default -> null;
            };
        }
    }

    abstract String toLLVMRep();

    public static Inst condBr(IRValue cond, String label1, String label2) {
        return new TermInst(InstType.condBr, null, cond, label1, label2);
    }
    public static Inst br(String label) {
        return new TermInst(InstType.br, null, null, label, null);
    }
    public static Inst ret(IRType ty, IRValue value) {
        return new TermInst(InstType.ret, ty, value, null, null);
    }

    public static Inst add(IRValue result, IRType ty, IRValue op1, IRValue op2) {
        return new BinaryInst(InstType.add, result, ty, op1, op2);
    }
    public static Inst sub(IRValue result, IRType ty, IRValue op1, IRValue op2) {
        return new BinaryInst(InstType.sub, result, ty, op1, op2);
    }
    public static Inst mul(IRValue result, IRType ty, IRValue op1, IRValue op2) {
        return new BinaryInst(InstType.mul, result, ty, op1, op2);
    }
    public static Inst div(IRValue result, IRType ty, IRValue op1, IRValue op2) {
        return new BinaryInst(InstType.div, result, ty, op1, op2);
    }
    public static Inst rem(IRValue result, IRType ty, IRValue op1, IRValue op2) {
        return new BinaryInst(InstType.rem, result, ty, op1, op2);
    }

    public static Inst alloca(IRValue result, IRType ty) {
        return new MemoryInst(InstType.alloca, ty, result, 16, null, null, null);
    }
    public static Inst load(IRValue result, IRType ty, IRValue pointer) {
        return new MemoryInst(InstType.load, ty, result, ty.getAlignment(), pointer, null, null);
    }
    public static Inst store(IRValue value, IRType ty, IRValue pointer) {
        return new MemoryInst(InstType.store, ty, null, ty.getAlignment(), pointer, value, null);
    }
    public static Inst gep(IRValue result, IRType ty, IRValue pointer, List<IRValue> indices) {
        return new MemoryInst(InstType.gep, ty, result, 0, pointer, null, indices);
    }

    public static Inst icmp(IRValue result, LLVMIcmpPredicate cond, IRType ty, IRValue op1, IRValue op2) {
        return new ICmpInst(ty, result, cond, op1, op2);
    }

    public static Inst call(IRValue result, IRType ty, String funcName, List<IRValue> args) {
        return new CallInst(result, ty, funcName, args);
    }

    public static Inst zext(IRValue result, IRType ty, IRValue value, IRType ty2) {
        return new ConversionInst(InstType.zext, result, ty, value, ty2);
    }
}
