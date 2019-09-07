grammar Summer;

root : '(' expr '+' expr ')';
expr: expr '+' expr # ExprSum
    | NUM           # ExprNum
    ;
NUM : [0-9]+;
ADD : '+';
