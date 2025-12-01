int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);

struct str1 {
    int qwq;
    int quq;
};

int func(struct str1 s0) {
    s0.qwq = 123;
    return s0.quq;
}

int func2(struct str1 *s0) {
    s0->qwq = 123;
    return s0->quq;
}

int main0() {
    struct str1 local;
    local.qwq = 1;
    local.quq = 3;
    assert_eq(1, func(local), 3);
    assert_eq(2, local.qwq, 1);

    local.qwq = 1;
    local.quq = 3;
    int ret = func2(&local);
    assert_eq(3, ret, 3);
    assert_eq(4, local.qwq, 123);
    return 0;
}
