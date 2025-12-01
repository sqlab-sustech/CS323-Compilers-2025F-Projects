package framework.llvm;

public enum LLVMIcmpPredicate {
    Equals      ("eq"),
    NotEquals   ("ne"),
    UnsignedGT  ("ugt"),
    UnsignedGE  ("uge"),
    UnsignedLT  ("ult"),
    UnsignedLE  ("ule"),
    SignedGT    ("sgt"),
    SignedGE    ("sge"),
    SignedLT    ("slt"),
    SignedLE    ("sle"),
    ;

    final String llvmName;

    public String llvmName() {
        return llvmName;
    }

    LLVMIcmpPredicate(String llvmName) {
        this.llvmName = llvmName;
    }
}
