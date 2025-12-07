int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);

int array[1000];

int select_max(int from, int to) {
    int max_idx = from;
    int i = from + 1;
    
    // 遍历指定区间
    while (i <= to) {
        if (array[i] > array[max_idx]) {
            max_idx = i;
        }
        i = i + 1;
    }
    return max_idx;
}

int main0() {
    int seed = readint();
    setseed(seed);
    int n = readint();
    int i = 0;
    while (i<n) {
        array[i] = getrand();
        ++i;
    }

    int j = n - 1;
    while (j > 0) {
        // 找出 0 到 j 范围内最大值的下标
        int max_pos = select_max(0, j);
        
        // 交换 array[max_pos] 和 array[j]
        // 将最大的数放到当前的最后面
        int temp = array[j];
        array[j] = array[max_pos];
        array[max_pos] = temp;
        
        j = j - 1;
    }

    i = 0;
    while (i < n - 1) {
        int prev = array[i];
        int next = array[i + 1];
        int less = prev <= next;
        assert_eq(i, less, 1);
        writeint(prev);
        i = i + 1;
        
    }
    return 0;
}