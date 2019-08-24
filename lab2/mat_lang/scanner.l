%{
/* header */
#include "token.h"
%}

%%

%{
/* blanks */
%}
[ \t\n]   ;

<<EOF>> return(T_EOF);

%{
/* keywords */
%}

var     return(T_VAR);
func    return(T_FUNC);

%{
/* symbols */
%}

"="  return(T_ASSIGN);
":"  return(T_COLON);
","  return(T_COMMA);
"("  return(T_OPEN_P);
")"  return(T_CLOSE_P);
"+"  return(T_PLUS);
"-"  return(T_MINUS);
"/"  return(T_DIV);
"*"  return(T_MULT);

%{
/* identifiers and integers */
%}

[a-zA-Z][a-zA-Z0-9]*    return(T_ID);
[0-9]+  return(T_NUMBER);

.   return(T_ERROR);

%%

int yywrap() { return 1; }
