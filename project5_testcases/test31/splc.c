int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);

int swap(int *a, int *b) {
    int temp = *a;
    *a = *b;
    *b = temp;
    return 0;
}

struct sum {
    int s1;
    int s2;
    int s3;
};

int sum_array(int *arr, int n, struct sum *sm) {
    int sum = 0;
    int i = 0;
    int *arr2 = arr;
    while (i < n) {
        sm->s1 = sm->s1 + arr[i];
        sm->s2 = sm->s2 + *(arr + i);
        sm->s3 = sm->s3 + *(arr2++);
        i = i + 1;
    }
    return sum;
}

int array[100];

int main0() {
    setseed(readint());
    int i=0;
    while(i<100) {
        array[i++] = getrand() % 1009;
    }
    int *parr = &array[0];

    struct sum s0;
    s0.s1 = 0;
    s0.s2 = 0;
    s0.s3 = 0;
    sum_array(parr, 100, &s0);
    
    swap(&s0.s1, &(&s0)->s2);
    swap(&s0.s2, &(&s0)[0].s3);

    writeint(s0.s1);
    writeint((&s0)->s2);
    writeint(s0.s3);

    return 0;
}