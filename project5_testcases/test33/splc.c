int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);


struct DNode {
    int val;
    struct DNode *prev;
    struct DNode *next;
};

struct DNode pool[100];
int pool_idx;

int new_node(int val, struct DNode** out) {
    struct DNode *p = &pool[pool_idx];
    pool_idx = pool_idx + 1;
    
    p->val = val;
    p->prev = 0;
    p->next = 0;
    *out = p;
    return 0;
}

int insert_after(struct DNode *target, struct DNode *newNode) {
    struct DNode *nextOne = target->next;

    newNode->next = nextOne;
    if (0 != nextOne) {
        nextOne->prev = newNode;
    }

    newNode->prev = target;
    target->next = newNode;

    return 0;
}

int remove_node(struct DNode *target) {
    struct DNode *prevOne = target->prev;
    struct DNode *nextOne = target->next;

    if (prevOne) {
        prevOne->next = nextOne;
    }
    
    if (nextOne) {
        nextOne->prev = prevOne;
    }
    target->prev = 0;
    target->next = 0;
    return 0;
}

int main0() {
    pool_idx = 0;
    setseed(readint());

    struct DNode* array[100];
    int i=0;
    while(i<100) {
        new_node(getrand() % 1009, &array[i]);
        i++;
    }

    int ops = 0;
    while(ops < 100000) {
        struct DNode* ptr1 = array[getrand() % 100];
        struct DNode* ptr2 = array[getrand() % 100];
        if (ptr1 != ptr2) {
            if (getrand() % 2) {
                insert_after(ptr1, ptr2);
            } else {
                remove_node(ptr2);
            }
        }
        ops++;
    }

    i = 0;
    while(i<100) {
        int sum = 0;
        struct DNode* curr = array[i];
        int depth = 10;
        while(curr && --depth > 0) {
            sum = (sum + curr->val) % 1009;
            curr = curr->next;
        }
        curr = array[i];
        depth = 10;
        while(curr && --depth > 0) {
            sum = sum + (sum * curr->val) % 1009;
            curr = curr->prev;
        }
        writeint(sum);
        i++;
    }

    return 0;
}