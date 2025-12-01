package framework.llvm;

import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BasicBlockBuilder {
    private final FunctionBuilder fb;
    private final String name;
    final List<Inst> instructions = new ArrayList<>();
    private final List<Pair<IRValue, IRType>> stackAllocas;

    BasicBlockBuilder(FunctionBuilder fb, String name, boolean isRootBlock) {
        this.fb = fb;
        this.name = name;
        if (isRootBlock)
            stackAllocas = new ArrayList<>();
        else
            stackAllocas = null;
    }

    // whether this basic block has been terminated.
    private boolean terminated = false;

    public boolean hasTerminated() {
        return terminated;
    }

    // Checkers
    private void assertNotTerminated() {
        if (this.terminated)
            throw new RuntimeException("Cannot append instructions to a terminated BasicBlock");
    }

    private void _assert(boolean value) {
        if (!value)
            throw new RuntimeException("Assertion failed");
    }

    public String llvmLabelName() {
        return name;
    }

    public String print() {
        return llvmLabelName() + ":\n" + Stream.concat(
                        (stackAllocas != null ? stackAllocas.stream().map(pair -> Inst.alloca(pair.a, pair.b)) : Stream.of()),
                        instructions.stream()
                ).map(Inst::toLLVMRep)
                .collect(Collectors.joining("\n  ", "  ", "\n"));
    }

// Instructions Generator ...

    // control flow
    public void condBr(IRValue cond, BasicBlockBuilder trueBlock, BasicBlockBuilder falseBlock) {
        _assert(cond.type().typeEquals(IRType.bool()));
        assertNotTerminated();
        instructions.add(Inst.condBr(cond, trueBlock.llvmLabelName(), falseBlock.llvmLabelName()));
        this.terminated = true;
    }

    public void ret(IRValue value) {
        _assert(value.type().typeEquals(fb.getReturnType()));
        assertNotTerminated();
        instructions.add(Inst.ret(fb.getReturnType(), value));
        this.terminated = true;
    }

    public void br(BasicBlockBuilder target) {
        assertNotTerminated();
        instructions.add(Inst.br(target.llvmLabelName()));
        this.terminated = true;
    }

    // arithmetic
    public IRValue add(IRValue lhs, IRValue rhs, String name) {
        name = fb.validateIdent(name);
        _assert(lhs.type().typeEquals(rhs.type()));
        assertNotTerminated();
        IRValue result = new IRValue(name, lhs.type());
        instructions.add(Inst.add(result, result.type(), lhs, rhs));
        return result;
    }


    public IRValue sub(IRValue lhs, IRValue rhs, String name) {
        name = fb.validateIdent(name);
        _assert(lhs.type().typeEquals(rhs.type()));
        assertNotTerminated();
        IRValue result = new IRValue(name, lhs.type());
        instructions.add(Inst.sub(result, result.type(), lhs, rhs));
        return result;
    }


    public IRValue mul(IRValue lhs, IRValue rhs, String name) {
        name = fb.validateIdent(name);
        _assert(lhs.type().typeEquals(rhs.type()));
        assertNotTerminated();
        IRValue result = new IRValue(name, lhs.type());
        instructions.add(Inst.mul(result, result.type(), lhs, rhs));
        return result;
    }

    public IRValue div(IRValue lhs, IRValue rhs, String name) {
        name = fb.validateIdent(name);
        _assert(lhs.type().typeEquals(rhs.type()));
        assertNotTerminated();
        IRValue result = new IRValue(name, lhs.type());
        instructions.add(Inst.div(result, result.type(), lhs, rhs));
        return result;
    }

    public IRValue rem(IRValue lhs, IRValue rhs, String name) {
        name = fb.validateIdent(name);
        _assert(lhs.type().typeEquals(rhs.type()));
        assertNotTerminated();
        IRValue result = new IRValue(name, lhs.type());
        instructions.add(Inst.rem(result, result.type(), lhs, rhs));
        return result;
    }

    // memory access & addressing

    public IRValue gep(IRValue ptr, IRType ty, IRValue index0, String name) {
        name = fb.validateIdent(name);
        assertNotTerminated();
        _assert(ptr.type().isPointer());
        _assert(index0.type().isInteger());
        IRValue result = new IRValue(name, IRType.pointer());
        instructions.add(Inst.gep(result, ty, ptr, List.of(index0)));
        return result;
    }

    public IRValue gep(IRValue ptr, IRType ty, int index0, IRValue index1, String name) {
        name = fb.validateIdent(name);
        assertNotTerminated();
        _assert(ptr.type().isPointer());
        _assert(index1.type().isInteger());
        IRValue result = new IRValue(name, IRType.pointer());
        instructions.add(Inst.gep(result, ty, ptr, List.of(IRValue.consti32(index0), index1)));
        return result;
    }


    public IRValue alloca(IRType type, String name) {
        if (stackAllocas != null) {
            IRValue v = new IRValue(name, IRType.pointer());
            stackAllocas.add(new Pair<>(v, type));
            return v;
        } else {
            return fb.rootBlock().alloca(type, name);
        }
    }

    public IRValue load(IRValue ptr, IRType ty, String name) {
        name = fb.validateIdent(name);
        assertNotTerminated();
        _assert(ptr.type().isPointer());
        IRValue result = new IRValue(name, ty);
        instructions.add(Inst.load(result, ty, ptr));
        return result;
    }


    public void store(IRValue ptr, IRType ty, IRValue value) {
        assertNotTerminated();
        _assert(ptr.type().isPointer());
        instructions.add(Inst.store(value, ty, ptr));
    }

    // conditional

    public IRValue icmp(IRValue lhs, LLVMIcmpPredicate op, IRValue rhs, String name) {
        name = fb.validateIdent(name);
        _assert(lhs.type().typeEquals(rhs.type()));
        assertNotTerminated();
        IRValue result = new IRValue(name, IRType.bool());
        instructions.add(Inst.icmp(result, op, lhs.type(), lhs, rhs));
        return result;
    }

    public IRValue zext(IRValue value, IRType target, String name) {
        name = fb.validateIdent(name);
        _assert(value.type().isBoolean());
        assertNotTerminated();
        IRValue result = new IRValue(name, target);
        instructions.add(Inst.zext(result, value.type(), value, target));
        return result;
    }

    public IRValue call(IRType funcRetTy, String funcName, List<IRValue> args, String resultName) {
        resultName = fb.validateIdent(resultName);
        assertNotTerminated();
        IRValue result = new IRValue(resultName, funcRetTy);
        instructions.add(Inst.call(result, funcRetTy, funcName, args));
        return result;
    }
}
