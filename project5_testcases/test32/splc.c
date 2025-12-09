int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);

struct Node
{
    int id;
    int val[10];
    struct Node *ptr;
};

struct Node data_nodes[100];
struct Node *ptr_array[50][10];



int happy_compiler(struct Node *(*ptr)[10]) {
    int sum = 0;
    int i=0;
    while(i<1) {
        int j=0;
        while(j<10) {
            sum = (sum + ptr[i][j]->id * ptr[i][j]->val[j] % 1009) % 1009;
            if (3 <= j && j <= 9) {
                int depth = 20;
                struct Node*curr = ptr[i][j]->ptr;
                while(curr && --depth > 0) {
                    sum = sum + sum * curr->val[j] % 1009;
                    curr = curr->ptr;
                }
            }
            j++;
        }
        i++;
    }
    return sum;
}

int main0()
{
    setseed(readint());

    int i = 0;
    while (i < 100)
    {
        struct Node *p = &data_nodes[i];
        p->id = i + 1;
        int k=0;
        while (k<10)
        {
            p->val[k++] = getrand() % 1007;
        }
        p->ptr = 0;
        i++;
    }

    i = 0;
    while (i < 500)
    {
        ptr_array[i / 10][i % 10] = &data_nodes[getrand() % 100];
        ptr_array[i / 10][i % 10]->ptr = &data_nodes[getrand() % 100];
        i++;
    }

    writeint(happy_compiler(&ptr_array[0]));
    writeint(happy_compiler(&ptr_array[getrand() % 20]));
    writeint(happy_compiler(&ptr_array[getrand() % 10]));
    writeint(happy_compiler(&ptr_array[getrand() % 24]));

    return 0;
}