package framework.llvm;

import org.antlr.v4.runtime.misc.Pair;

import java.util.*;
import java.util.stream.Collectors;


public class FunctionBuilder {
    private final IRBuilder rootBuilder;

    private final String funcName;
    private final IRType returnType;
    private final List<Pair<String, IRType>> arguments;
    private final Map<String, IRValue> argumentAddresses;
    private final Map<String, BasicBlockBuilder> blocks;
    private final BasicBlockBuilder rootBlock = new BasicBlockBuilder(this, "entry", true);

    FunctionBuilder(IRBuilder rootBuilder, String funcName, IRType returnType, List<Pair<String, IRType>> args) {
        this.funcName = funcName;
        this.rootBuilder = rootBuilder;
        this.returnType = returnType;
        this.arguments = args.stream().toList();
        this.argumentAddresses = args.stream().collect(Collectors.toMap(pair -> pair.a, pair -> {
            var addr = this.rootBlock.alloca(pair.b, pair.a + ".addr");
            this.rootBlock.store(addr, pair.b, new IRValue(pair.a, pair.b));
            return addr;
        }));
        this.blocks = new HashMap<>();
        this.blocks.put(this.rootBlock.llvmLabelName(), this.rootBlock);
    }

    public IRValue param(int idx) {
        return this.argumentAddresses.get(this.arguments.get(idx).a);
    }

    public IRValue param(String name) {
        return this.argumentAddresses.get(name);
    }

    public IRType getReturnType() {
        return this.returnType;
    }

    public BasicBlockBuilder rootBlock() {
        return this.rootBlock;
    }

    public BasicBlockBuilder newBasicBlock(String name) {
        var bbb = new BasicBlockBuilder(this, validateLabelName(name), false);
        this.blocks.put(bbb.llvmLabelName(), bbb);
        return bbb;
    }

    // one of the hardest tasks in Computer Science: Naming things

    private final Map<String, Integer> usedLabels = new HashMap<>();
    private final Map<String, Integer> usedIdents = new HashMap<>();

    public String validateLabelName(String labelName) {
        if (labelName == null)
            labelName = "_L";
        else labelName += ".";
        this.usedLabels.compute(labelName, (k, v) -> v == null ? 0 : v + 1);
        return labelName + this.usedLabels.get(labelName);
    }

    public String validateIdent(String ident) {
        if (ident == null)
            ident = "tmp_"; // unnamed temporaries are numbered sequentially。 so just make them named.
        else ident += ".";
        this.usedIdents.compute(ident, (k, v) -> v == null ? 0 : v + 1);
        return ident + this.usedIdents.get(ident);
    }

    public String getName() {
        return this.funcName;
    }

    public String print() {
        var sb = new StringBuilder();
        // "#0" means attributes #0
        sb.append(String.format("define %s @%s (%s) #0 {", returnType.llvmName(), funcName,
                arguments.stream().map(arg -> String.format("%s %s", arg.b.llvmName(), "%" + arg.a))
                        .collect(Collectors.joining(", "))
        ));
        sb.append("\n");

        Queue<BasicBlockBuilder> queue = new LinkedList<>();
        Set<BasicBlockBuilder> visited = new HashSet<>();
        queue.offer(rootBlock);
        while (!queue.isEmpty()) {
            var bbb = queue.poll();
            if (visited.contains(bbb))
                continue;
            visited.add(bbb);
            if (!bbb.hasTerminated())
                throw new RuntimeException("Every basic block must be terminated.");
            sb.append(bbb.print());
            if (!bbb.instructions.isEmpty()) {
                var lastIns = bbb.instructions.getLast();
                if (lastIns instanceof Inst.TermInst termInst) {
                    if (termInst.type.equals(Inst.InstType.condBr)) {
                        queue.offer(this.blocks.get(termInst.label1));
                        queue.offer(this.blocks.get(termInst.label2));
                    } else if (termInst.type.equals(Inst.InstType.br)) {
                        queue.offer(this.blocks.get(termInst.label1));
                    }
                }
            }
        }
        sb.append("}\n");
        return sb.toString();
    }
}
