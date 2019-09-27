#!/bin/bash
java -jar "antlr-4.7.2-complete.jar" -no-listener -visitor Grammar.g4
export CLASSPATH=".:antlr-4.7.2-complete.jar:$CLASSPATH"
javac *.java
#java org.antlr.v4.gui.TestRig Grammar start -gui < ~/ec/mc921/lab3/project/tests/test8.sm
java MyParser < ~/ec/mc921/lab3/project/tests/test8.sm