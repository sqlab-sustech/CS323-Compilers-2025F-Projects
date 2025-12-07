int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);

int global_var;

int func0(int q) {
    global_var = global_var * q;
    return global_var + 4;
}

int main0() {
    global_var = readint();
    if (readint() >= 20) {
        global_var= (+global_var);
    } else {
        global_var = -global_var;
    }
    int res = func0(readint());
    global_var = global_var + res;
    writeint(global_var);
    return 0;
}