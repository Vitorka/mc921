grammar Grammar;

root: component                             #RootComponent
    ;

component: var                              #ComponentVar
    | func                                  #ComponentFunc
    | component component                   #ComponentComponent
    ;

var: 'var' ID '=' expr ';'
    ;

func: 'func' ID '(' params ')' expr ';'
    ;

expr: expr ADD prior                         #ExprSum
    | expr MINUS prior                       #ExprMinus
    | prior                                  #ExprPrior
    ;

prior: prior MULT terminal                  #PriorMult
    | prior DIV terminal                    #PriorDiv
    | terminal                              #PriorTerminal
    ;

terminal: ID '(' exprparams ')'             #TerminalFunc
    | NUM                                   #TerminalNum
    | ID                                    #TerminalID
    | '(' expr ')'                          #TerminalParen
    ;

params: params ',' params                   #VariousParams
    | ID                                    #ParamsID
    | NUM                                   #ParamsNUM
    ;

exprparams: exprparams ',' exprparams       #VariousExprParams
    | ID                                    #ExprParamsID
    | NUM                                   #ExprParamsNUM
    ;

NUM : [0-9]+;
ADD : '+';
MINUS: '-';
DIV: '/';
MULT: '*';
ID : [a-zA-Z]+[a-zA-Z0-9_]* | '_'+[a-zA-Z0-9_]+ ;
WS  : [ \t\r\n]+ -> skip;