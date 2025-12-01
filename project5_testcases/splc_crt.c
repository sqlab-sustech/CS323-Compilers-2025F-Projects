#include <stdio.h>
#include <stdlib.h>

int readint() {
    int ret = 0;
    scanf("%d", &ret);
    return ret;
}
int writeint(int out) {
    printf("%d ", out);
    return 0;
}
static unsigned int randnum = 0;
int setseed(int seed) {
    randnum = (unsigned int) seed;
    return 0;
}
int getrand() {
    return rand_r(&randnum) % 100000;
}

int assert_eq(int where, int given, int expected) {
    if (given != expected) {
        fprintf(stderr, "assert_eq (%d) failed: given: %d, expected: %d\n", where, given, expected);
        exit(1);
    }
    return 0;
}

extern int main0();
int main() {
    setbuf(stdin, NULL);
    setbuf(stdout, NULL);
    main0();
    return 0;
}
