#!/bin/bash
set -x

sed -E -e "/TEST|test/d" $1 > tmp_code_sed.c
sed -e "s/INT\>\|CHAR\>\|FLOAT\>\|LONG\>\|VOID\>\|ENUM\>\|STRUCT\>\|UNION\>\|TYPEDEF\>\|FOR\>\|WHILE\>\|IF\>\|ELSE\>\|SWITCH\>\|CASE\>\|BREAK\>\|CONTINUE\>\|RETURN\>\|GOTO\>/\L&/g"  tmp_code_sed.c > corrected.c

rm tmp_code_sed.c


flex -i -o scanner.c scanner.l
gcc -g -o scanner_test scanner_test.c scanner.c
./scanner_test < corrected.c > tokens.txt

sed -E -n "/T_ID\>|T_STR\>|T_NUM\>/p" tokens.txt > selected.txt