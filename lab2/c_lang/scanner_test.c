#include "token.h"
#include <stdio.h>
#include <stdlib.h>

extern int yylex();

char *yytext;
extern int lines;

int yyerror(char *s) {
    printf("ERROR yy\n");
    (void)s;
    return 0;
}

int main() {
    char format[] = "%s %s\n";
    enum token_id code;

    do {
        code = yylex();
        switch(code) {
            case T_ID:
                printf("%s %s\n", token_name[code], yytext);
                break;
            case T_NUM:
                printf("%s %s\n", token_name[code], yytext);
                break;
            case T_STR:
                printf("%s %s\n", token_name[code], yytext);
                break;
            case T_ERROR:
                printf("T_ERROR line %d\n", lines);
                exit(1);
            default:
                printf("%s %s\n", token_name[code], yytext);
                break;
        }
    } while (code != T_EOF);
    printf("lines:%d\n", lines);
    return 0;
}
