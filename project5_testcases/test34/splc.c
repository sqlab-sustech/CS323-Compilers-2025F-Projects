int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);

int swap(int *a, int *b) {
    int tmp = *a;
    *a = *b;
    *b = tmp;
    return 0;
}

int quick_sort(int *arr, int n) {
    if (n <= 1) {
        return 0;
    }

    int *pivot_ptr = arr + n - 1;
    int pivot_val = *pivot_ptr;

    int i = 0;
    int j = 0;
    
    while (j < n - 1) {
        int *curr = arr + j;
        
        if (*curr < pivot_val) {
            swap(arr + i, arr + j);
            i = i + 1;
        }
        j = j + 1;
    }
    swap(arr + i, arr + n - 1);

    quick_sort(arr, i);
    quick_sort(arr + i + 1, n - i - 1);

    return 0;
}

int array[1000];
int main0() {
    setseed(readint());
    int i=0;
    while(i<1000) {
        array[i++] = getrand();
    }
    quick_sort(&array[0], 1000);
    i=0;
    while(i<1000) {
        writeint(array[i++]);
    }

    return 0;
}