int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);

int ptr_test(int **ptr) {
    int local;
    *ptr = &local;
    return 0;
}

struct s0 {
    int qwq;
    int arr[10];
};
int main0() {
    int *ptr;
    ptr_test(&ptr);
    *ptr = 114514;

    struct s0 s;
    struct s0 *p = &s;

    p[1].qwq = 1233;
    
    return 0;
}
