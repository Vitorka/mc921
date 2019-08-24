#include "token.h"
#include <stdio.h>
#include <stdlib.h>

extern int yylex();

char *yytext;

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
                /* FALLTROUGH */
            case T_NUMBER:
                printf("%s %s\n", token_str[code], yytext);
                break;
            case T_ERROR:
                printf("T_ERROR\n");
                exit(1);
            default:
                printf("%s\n", token_str[code]);
                break;
        }
    } while (code != T_EOF);
    return 0;
}
