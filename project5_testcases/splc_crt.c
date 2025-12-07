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
static unsigned long long randnum = 0;
static const unsigned int LCG_a = 1103515245;
static const unsigned int LCG_c = 12345;

int setseed(int seed) {
    randnum = (unsigned long long) seed;
    return 0;
}
int getrand() {
    randnum = randnum * LCG_a + LCG_c;
    return (unsigned int)(randnum / 65536) % 32768;
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
