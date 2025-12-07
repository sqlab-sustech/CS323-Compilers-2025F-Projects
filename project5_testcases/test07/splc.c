int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);


int main0() {
    int a = readint();
    int b = readint();
    int c = readint();
    if ((a++ >= 0 && b++ <= 10) || ++c != 10) {
        writeint(1);
    } else {
        writeint(0);
    }
    writeint(a);
    writeint(b);
    writeint(c);
    {
        int a = readint();
        int b = readint();
        int c = readint();
        if ((a++ <= 2 || --b == 30) && c++ ) {
            writeint(1);
        } else {
            writeint(0);
        }
        writeint(a);
        writeint(b);
        writeint(c);
    }
    
    return 0;
}