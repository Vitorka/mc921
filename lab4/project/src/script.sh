#!/bin/bash
java -jar "antlr-4.7.2-complete.jar" -no-listener -visitor Grammar.g4
export CLASSPATH=".:antlr-4.7.2-complete.jar:$CLASSPATH"
javac *.java
java MyParser < tests/test$1.sm > arquivo.ll
llc arquivo.ll
gcc main.c arquivo.s -o a
./a > out.txt
diff out.txt tests/test$1.res
