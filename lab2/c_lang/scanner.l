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

"int"      return(T_INT);
"char"     return(T_CHAR);
"float"    return(T_FLOAT);
"long"     return(T_LONG);
"void"     return(T_VOID);
"enum"     return(T_ENUM);
"struct"   return(T_STRUCT);
"union"    return(T_UNION);
"typedef"  return(T_TYPEDEF);

"for"      return(T_FOR);
"while"    return(T_WHILE);
"if"       return(T_IF);
"else"     return(T_ELSE);
"switch"   return(T_SWITCH);
"case"     return(T_CASE);
"break"    return(T_BREAK);
"continue" return(T_CONTINUE);
"return"   return(T_RETURN);
"goto"     return(T_GOTO);

%{
/* symbols */
%}

"+"        return(T_PLUS);
"-"        return(T_MINUS);
"*"        return(T_ASTERISK);
"&"        return(T_AMPERSAND);
"/"        return(T_DIV);
"%"        return(T_REMINDER);
"("        return(T_OPEN_P);
")"        return(T_CLOSE_P);
"{"        return(T_OPEN_B);
"}"        return(T_CLOSE_B);
"["        return(T_OPEN_K);
"]"        return(T_CLOSE_K);
":"        return(T_COLON);
";"        return(T_SEMI_COLON);
"="        return(T_ASSIGN);
"!"        return(T_NOT);
"."        return(T_DOT);
","        return(T_COMMA);

"=="       return(T_EQUAL);
"!="       return(T_DIFFERENT);
"&&"       return(T_AND);
"||"       return(T_OR);
">"        return(T_GREATER);
">="       return(T_G_OR_E);
"<"        return(T_SMALLER);
"<="       return(T_S_OR_E);

%{
/* identifiers, numbers and strings */
%}

[a-zA-Z][a-zA-Z0-9]*    return(T_ID);
[0-9]+  return(T_NUM);
[a-zA-Z0-9]*    return(T_STR);

.   return(T_ERROR);

%%

int yywrap() { return 1; }
