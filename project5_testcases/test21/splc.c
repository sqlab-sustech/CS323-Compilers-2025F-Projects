int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);

struct str1 {
    int qwq;
    int array[10][20];
};

struct str1 global;

int dont_change_me(struct str1 s) {
    s.qwq = getrand();
    int  i=0;
    while (i < 10) {
        int j=0;
        while(j < 20) {
            s.array[i][j++] = getrand();
        }
        i++;
    }
    return 0;
}

int main0() {
    int seed = readint();
    setseed(seed);

    global.qwq = readint();
    int i=0;
    while(i < 10) {
        int j=0;
        while(j < 20) {
            global.array[i][j++] = getrand();
        }
        i++;
    }

    dont_change_me(global);

    writeint(global.qwq);
    i=0;
    while (i < 10) {
        int j=0;
        while(j < 20) {
            writeint(global.array[i][j++]);
        }
        i++;
    }
    
    return 0;
}
