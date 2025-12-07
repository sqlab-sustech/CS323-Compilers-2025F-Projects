int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);

int main0() {
    int inputs[6];
    int i=0;
    while(i<6) {
        inputs[i++] = readint();
    }
    if (inputs[1] == 1234) {
        writeint(1);
        if (inputs[2] == 1234) {
            writeint(2);
        } else {
            writeint(3);
            if (inputs[3] == 999) {
                return 0;
            } else {
                writeint(4);
            }
            writeint(5);
        }
        writeint(6);
        if (inputs[4] == 4396) {
            writeint(7);
        } else {
            return 0;
        }
        writeint(8);
    } else {
        while(inputs[5]--) {
            writeint(9);
            if (inputs[5] == 10)
                return 0;
        }
        writeint(10);
    }
    writeint(11);
    return 0;
}