grammar Summer;

start: initial initial;


initial: 'func' ID '(' param ')' expr ';'#ExprFunc
    | 'var' ID '=' expr ';'               #ExprVar
    | initial initial                     #ExprInitial
    ;

param: param  ','  param            # ParamComma
    | ID                            # ParamId
    ;

expr: expr '+' expr        # ExprSum
    | expr '-' expr        # ExprMinus
    | expr '/' expr        # ExprDivide
    | expr '*' expr        # ExprMult
    | NUM                        # ExprNum
    | '(' expr ')'           # ExprWithParenteses
    | ID '(' elem ')'                  # ExprFuncUsage
    | ID                         # ExprID
    ;

elem: ID                            # ElemId
    | NUM                           # ElemNum
    | ID '(' elem ')'               # ElemFuncUsage
    | elem ',' elem                 # ElemComma
    ;

NUM : [0-9]+;
ADD : '+';
ID : [a-zA-Z]+[a-zA-Z0-9_]* | '_'+[a-zA-Z0-9_]+ ;
WS  : [ \t\r\n]+ -> skip;
