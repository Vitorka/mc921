#!/bin/bash
set -x

sed -E -e "/TEST|test/d" code1.c > tmp_code_sed.c
sed -e "s/INT\>\|CHAR\>\|FLOAT\>\|LONG\>\|VOID\>\|ENUM\>\|STRUCT\>\|UNION\>\|TYPEDEF\>\|FOR\>\|WHILE\>\|IF\>\|ELSE\>\|SWITCH\>\|CASE\>\|BREAK\>\|CONTINUE\>\|RETURN\>\|GOTO\>/\L&/g"  tmp_code_sed.c > code1_sed.c

sed -E -e "/TEST|test/d" code2.c > tmp_code_sed.c
sed -e "s/INT\>\|CHAR\>\|FLOAT\>\|LONG\>\|VOID\>\|ENUM\>\|STRUCT\>\|UNION\>\|TYPEDEF\>\|FOR\>\|WHILE\>\|IF\>\|ELSE\>\|SWITCH\>\|CASE\>\|BREAK\>\|CONTINUE\>\|RETURN\>\|GOTO\>/\L&/g"  tmp_code_sed.c > code2_sed.c

sed -E -e "/TEST|test/d" code3.c > tmp_code_sed.c
sed -e "s/INT\>\|CHAR\>\|FLOAT\>\|LONG\>\|VOID\>\|ENUM\>\|STRUCT\>\|UNION\>\|TYPEDEF\>\|FOR\>\|WHILE\>\|IF\>\|ELSE\>\|SWITCH\>\|CASE\>\|BREAK\>\|CONTINUE\>\|RETURN\>\|GOTO\>/\L&/g"  tmp_code_sed.c > code3_sed.c

sed -E -e "/TEST|test/d" code4.c > tmp_code_sed.c
sed -e "s/INT\>\|CHAR\>\|FLOAT\>\|LONG\>\|VOID\>\|ENUM\>\|STRUCT\>\|UNION\>\|TYPEDEF\>\|FOR\>\|WHILE\>\|IF\>\|ELSE\>\|SWITCH\>\|CASE\>\|BREAK\>\|CONTINUE\>\|RETURN\>\|GOTO\>/\L&/g"  tmp_code_sed.c > code4_sed.c

rm tmp_code_sed.c


flex -i -o scanner.c scanner.l
gcc -g -o scanner_test scanner_test.c scanner.c
./scanner_test < $1
