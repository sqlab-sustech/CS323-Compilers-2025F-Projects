int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);


int main0() {
    int array[10];

    int i=readint();
    array[i] = 114514;
    
    return 0;
}
