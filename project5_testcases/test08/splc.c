int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);

int main0() {
    int array[10][20][30];

    int i = 0;
    while (i < 10) {
        int j = 0;
        while (j < 20) {
            int k = 0;
            while (k < 30) {
                array[i][j][k] = i * 100 + j / 13 + k % 20;
                k++;
            }
            j++;
        }
        i++;
    }

    int a = readint();
    int b = readint();
    int c = readint();
    writeint(array[++a][b++][c--]);
    writeint(a);
    writeint(b);
    writeint(c);

    return 0;
}