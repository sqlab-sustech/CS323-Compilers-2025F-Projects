int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);

struct student {
    int sid;
    int value;
};

struct student s[100];

int main0() {
    int seed = readint();
    setseed(seed);

    int i=0;
    while(i < 100) {
        s[0].sid = i;
        s[0].value = getrand();
        i++;
    }

    i = 0;
    while(i < 5) {
        int id = readint();
        int j = 0;
        while (j < 100)
        {
            if (s[j].sid == id) {
                writeint(s[j].value);
                j = 100;    // break
            }
            j++;
        }
        i++;
    }
    return 0;
}
