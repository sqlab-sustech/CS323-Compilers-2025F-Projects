package framework.llvm;

import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IRBuilder {
    private final Map<String, List<IRType>> typedefs = new LinkedHashMap<>();
    private final Map<String, Pair<IRType, IRValue>> globalDefs = new LinkedHashMap<>();
    private final List<FunctionBuilder> functions = new ArrayList<>();
    private final Map<String, Pair<IRType, List<IRType>>> funcDecls = new LinkedHashMap<>();

    public void defineStructure(String name, List<IRType> elements) {
        if(this.typedefs.containsKey(name))
            throw new RuntimeException("Structure already defined");
        this.typedefs.put(name, elements);
    }

    public IRValue defineGlobalVar(String name, IRType ty) {
        if(this.globalDefs.containsKey(name))
            throw new RuntimeException("GlobalVar already defined");
        IRValue v = new IRValue("@" + name, IRType.pointer());
        this.globalDefs.put(name, new Pair<>(ty, v));
        return v;
    }

    public IRValue global(String name) {
        return this.globalDefs.get(name).b;
    }

    public void declareFunction(String name, IRType retTy, List<Pair<String, IRType>> args) {
        funcDecls.put(name, new Pair<>(retTy, args.stream().map(arg -> arg.b).toList()));
    }

    public FunctionBuilder defineFunction(String name, IRType retTy, List<Pair<String, IRType>> args) {
        var fb = new FunctionBuilder(this, name, retTy, args);
        this.functions.add(fb);
        return fb;
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        funcDecls.forEach((name, args) -> {
            sb.append(String.format("declare %s @%s (%s)", args.a.llvmName(), name,
                    args.b.stream().map(IRType::llvmName).collect(Collectors.joining(", ")))
            ).append("\n");
        });
        sb.append("\n");
        typedefs.forEach((name, types) -> {
            sb.append(String.format("%%struct.%s = type { %s }", name, types.stream().map(IRType::llvmName).collect(Collectors.joining(", "))))
                    .append("\n");
        });
        sb.append("\n");
        globalDefs.forEach((name, origTy_PtrValue) -> {
            sb.append(String.format("%s = global %s zeroinitializer", origTy_PtrValue.b.llvmName(), origTy_PtrValue.a.llvmName()))
                    .append("\n");
        });
        sb.append("\n");
        functions.forEach(fb -> {
            sb.append(fb.print());
        });
        sb.append("\nattributes #0 = { sanitize_address }\n");
        return sb.toString();
    }
}
