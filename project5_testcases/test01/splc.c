int readint();
int writeint(int out);
int setseed(int seed);
int getrand();

int main0() {
    int a = readint();
    int b = readint();
    int res = a + b;
    if (res > 1000) {
        res = - res;
    } else {
        if (res < -2000) 
            res = res * 2;
    }
    writeint(res);
    writeint(a + b);
    writeint(a - b);
    writeint(a * b);
    writeint(a / b);
    writeint(-a);
    writeint(+a);
    writeint(a >= b);
    writeint(a < b);
    writeint(a <= b);
    writeint(a != b);
    writeint(a == b);
    return 0;
}