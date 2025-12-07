int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);

int fib(int n) {
    if (n == 0) {
        return 0;
    }
    if (n == 1) {
        return 1;
    }
    
    return fib(n - 1) + fib(n - 2);
}

int main0() {
    int i = readint();
    writeint(fib(i));
    return 0;
}