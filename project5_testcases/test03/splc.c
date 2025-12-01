int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);


int main0() {
    int i = 0;
    int *p = &i;
    **(&p) = 123;
    int checker = 0;
    
    assert_eq(1, i, 123);
    if (i == 123 || (checker = 123)) {
        i = 456;
    }
    assert_eq(2, i, 456);
    assert_eq(3, checker, 0);

    int value = i == 123 || (checker = 123);
    assert_eq(4, checker, 123);
    assert_eq(5, value, 1);
    
    return 0;
}
