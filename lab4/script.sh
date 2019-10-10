#!/bin/bash
java -jar "antlr-4.7.2-complete.jar" -no-listener -visitor Grammar.g4
export CLASSPATH=".:antlr-4.7.2-complete.jar:$CLASSPATH"
javac *.java
java org.antlr.v4.gui.TestRig Grammar root -gui < tests/test$1.sm
