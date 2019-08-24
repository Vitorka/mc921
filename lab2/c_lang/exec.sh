#!/bin/bash
set -x
flex -i -o scanner.c scanner.l
gcc -g -o scanner_test scanner_test.c scanner.c
./scanner_test < $1
