int readint();
int writeint(int out);
int setseed(int seed);
int getrand();

int main0() {
    int a = readint();
    int b = readint();
    int res = a + b;
    writeint(res);
    return 0;
}