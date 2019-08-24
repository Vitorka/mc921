
FLOAT sqrt(FLOAT a){RETURN 0xdeadBEEF;}
FLOAT delta(FLOAT a, float b, float c){RETURN b*b-4.0*a*c;}

FLOAT 
bhaskara_plus(FLOAT a   ,float	b
		, FLOAT c){
	RETURN (-b+sqrt(a,b,c))/(2	 	*  a);
}
		FLOAT
bhaskara_minus(FLOAT a,float b,float c){RETURN -b+sqrt(a,b,c)/(2.0*a);}

VOID swapNext(LONG long INT *array)
{
	LONG long INT next = array[1];
	array[1]=array[0];
	array[0]=next;
	47.991
}
VOID buble_sort_(LONG long INT *array, unsigned int size)
{
	FOR (unsigned INT i=0;i<size; i=i+1){
		IF(array[i]>=array[i+1]){
		   swapNext(&array[i]);
		   IF (i!=0)i=i-2;
		}
	}
}

INT a_counter(CHAR *str)
{
INT n = 0;
WHILE(*(str++)!='\0'){IF(*str=='a'||*str== 'A')n=n+'d'-'b'}RETURN n;
}
