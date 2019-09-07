#!/bin/bash
set -x
#compiling the Sum.g4 in java files
java -jar "antlr-4.7.2-complete.jar" -no-listener -visitor Summer.g4
#exporting the class path
export CLASSPATH=".:antlr-4.7.2-complete.jar:$CLASSPATH"
#compiling the .java generated from Sum.g4 with MyParser.java and AddVisitor.java
javac *.java
#feeding a string and reading the tokens
echo "(5+4+3+2+10)" | java org.antlr.v4.gui.TestRig Summer root -tokens
#feeding a string and reading tree in list style
echo "(5+4+3+2+10)" | java org.antlr.v4.gui.TestRig Summer root -tree
#feeding a string and printing a graphical tree
echo "(5+4+3+2+10)" | java org.antlr.v4.gui.TestRig Summer root -gui
#execute the implemented visitor
echo "(5+4+3+2+1)" | java MyParser
