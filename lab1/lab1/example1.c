#include <stdio.h>

static void function1(volatile int *i)
{
    *i += 1;
}

static void function2(volatile int *i)
{
    *i += 2;
}

int main(void)
{
    volatile int i;

    scanf("%d", &i);

    function1(&i);
    function2(&i);

    printf("%d\n", i);

    return 0;
}
