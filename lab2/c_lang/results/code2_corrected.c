
float sqrt(float a){return 0xdeadBEEF;}
float delta(float a, float b, float c){return b*b-4.0*a*c;}

float 
bhaskara_plus(float a   ,float	b
		, float c){
	return (-b+sqrt(a,b,c))/(2	 	*  a);
}
		float
bhaskara_minus(float a,float b,float c){return -b+sqrt(a,b,c)/(2.0*a);}

void swapNext(long long int *array)
{
	long long int next = array[1];
	array[1]=array[0];
	array[0]=next;
	47.991
}
void buble_sort_(long long int *array, unsigned int size)
{
	for (unsigned int i=0;i<size; i=i+1){
		if(array[i]>=array[i+1]){
		   swapNext(&array[i]);
		   if (i!=0)i=i-2;
		}
	}
}

int a_counter(char *str)
{
int n = 0;
while(*(str++)!='\0'){if(*str=='a'||*str== 'A')n=n+'d'-'b'}return n;
}
