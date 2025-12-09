int readint();
int writeint(int out);
int setseed(int seed);
int getrand();
int assert_eq(int where, int given, int expected);

struct Color
{
    int r;
    int g;
    int b;
};

struct Pixel
{
    int x;
    int y;
    struct Color color;
};

struct Canvas
{
    int id;
    int sum;
    struct Pixel p;
};

struct Canvas c[20];

int init(int i)
{
    c[i].p.color.r = getrand()  % 100;
    c[i].p.color.g = (getrand() + 20) % 100;
    c[i].p.color.b = (getrand() * c[i].p.color.g ) % 100;
    return i;
}

int main0()
{
    setseed(readint());

    int i = 0;
    while (i < 20)
    {
        c[i].id = getrand() % 100;
        c[i].p.x = getrand() % 100;
        c[i].p.y = getrand() % 100;
        init(i);
        i++;
    }

    int i0 = 0;
    while(i0 < 40) {
        int i = i0 % 20;
        int n = (i+1) % 20;
        c[i].id++;
        c[i].sum = c[i].sum % 1009 * (++c[n].sum);
        c[i].p.x = (c[i].p.y % 1009 * c[n].p.x) % 1009;
        c[i].p.y = (c[i].p.x % 1009 * c[n].p.y) % 1009;
        c[i].p.color.r = (c[n].p.color.g + c[n].p.color.b % 1009 * (c[i].id % 1009)) % 1009;
        c[i].p.color.g = (c[n].p.color.b + c[n].p.color.r % 1009 * (c[i].id % 1009)) % 1009;
        c[i].p.color.b = (c[n].p.color.r + (c[n].p.color.g % 1009) * (c[i].id % 1009)) % 1009;
        i0++;
    }
    i = 0;
    while(i < 20) {
        writeint(c[i].id);
        writeint(c[i].sum);
        writeint(c[i].p.x);
        writeint(c[i].p.y);
        writeint(c[i].p.color.r);
        writeint(c[i].p.color.g);
        writeint(c[i].p.color.b);
        i++;
    }

    return 0;
}