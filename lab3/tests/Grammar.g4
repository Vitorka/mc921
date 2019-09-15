grammar Grammar;

start: begin #StartBegin
    | begin begin #StartBeginBegin
    ;

begin: var                   #BeginVar
    | func                   #BeginFunc
    | begin begin            #BeginBegin
    ;

var: 'var' ID '=' expr ';'
    ;

func: 'func' ID '(' params ')' expr ';'
    ;

expr: expr ADD expr     #ExprSum
    | expr MINUS expr   #ExprMinus
    | expr DIV expr     #ExprDiv
    | expr MULT expr    #ExprMult
    | '(' expr ')'      #ExprParen
    | ID '(' params ')' #ExprFunc
    | NUM               #ExprNum
    | ID                #ExprID
    ;

params: params ',' params #VariousParams
    | ID                  #ParamsID
    | NUM                 #ParamsNUM
    ;

NUM : [0-9]+;
ADD : '+';
MINUS: '-';
DIV: '/';
MULT: '*';
ID : [a-zA-Z]+[a-zA-Z0-9_]* | '_'+[a-zA-Z0-9_]+ ;
WS  : [ \t\r\n]+ -> skip;