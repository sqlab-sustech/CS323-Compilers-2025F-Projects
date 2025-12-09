int assert_eq(int where, int given, int expected);

int x;

int func_change_global() {
    x = 100;
    return x;
}

int main0() {
    x = 5;
    assert_eq(1, x, 5);

    func_change_global();
    assert_eq(2, x, 100);

    int i = 0;
    while (i < 1) {
        int x = 20;
        assert_eq(3, x, 20);
        
        x = x + 5;
        assert_eq(4, x, 25);
        i = i + 1;
    }

    assert_eq(5, x, 100);

    return 0;
}