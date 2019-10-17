#include <stdio.h>
#include <stdlib.h>

extern void initializer();
extern int sm_main;

int main() {
    initializer();
    printf("%d\n", sm_main);
    return 0;
}