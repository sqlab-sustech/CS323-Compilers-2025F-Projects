int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);

int main0() {
    int seed = readint();
    setseed(seed);

    int a[10];
    int i;
    i = 0;
    while(i < 10) {
        a[i++] = getrand();
    }
    
    i = 0;
    while(i < 9) {
        a[i] = a[0] >= a[i + 1] && a[i] < 3000;
        i++;
    }
    i = 0;
    while(i < 10) {
        writeint(a[i++]);
    }
    

    return 0;
}