int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);

int a[20][30];
int b[30][20];
int c[20][20];

int main0() {
    int s = readint();
    setseed(s);

    int i = 0;
    int j = 0;
    while (i < 20) {
        j = 0;
        while (j < 30) {
            a[i][j] = getrand() % 100;
            j = j + 1;
        }
        i = i + 1;
    }

    i = 0;
    while (i < 30) {
        j = 0;
        while (j < 20) {
            b[i][j] = getrand() % 100;
            j = j + 1;
        }
        i = i + 1;
    }

    i = 0;
    while (i < 20) { // 遍历 A 的行 (M)
        int j = 0;
        while (j < 20) { // 遍历 B 的列 (K)
            c[i][j] = 0;
            int k = 0;
            while (k < 30) { // 遍历公共维度 (N)
                c[i][j] = c[i][j] + a[i][k] * b[k][j];
                k = k + 1;
            }
            j = j + 1;
        }
        i = i + 1;
    }

    i = 0;
    while (i < 20) {
        j = 0;
        while (j < 20) {
            writeint(c[i][j]);
            j = j + 1;
        }
        i = i + 1;
    }
    return 0;
}