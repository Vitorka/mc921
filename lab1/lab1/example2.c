int main(void)
{
    volatile int v[4];

    for (unsigned int i = 0; i < 4; i++)
    {
        v[i] = 10;
    }
    return 0;
}
