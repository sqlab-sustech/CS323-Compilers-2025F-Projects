int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);

struct str2 {
    int a;
    int b;
    int c;
};

struct str1 {
    int qwq;
    struct str2 array[10];
};

int main0() {
    struct str1 s;

    int seed = readint();
    setseed(seed);

    int i = 0;
    while (i < 10) {
        s.array[i].a = getrand();
        s.array[i].b = getrand();
        s.array[i].c = getrand();
        i++;
    }

    i = 0;
    while (i < 5) {
        int which = getrand() % 3;
        s.qwq = 0;
        int j = 0;
        while (j < 10) {
            if (which == 0)
                s.qwq = s.qwq + s.array[i].a;
            if (which == 1)
                s.qwq = s.qwq + s.array[i].b;
            if (which == 2)
                s.qwq = s.qwq + s.array[i].c;
            which = (which + 1) % 3;
            j++;
        }
        i++;
    }
    return 0;
}
