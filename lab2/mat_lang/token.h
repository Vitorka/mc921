#ifndef TOKEN_HEADER
#define TOKEN_HEADER

enum token_id
{
    T_VAR,      // "var"
    T_ASSIGN,   // "="
    T_FUNC,     // "func"
    T_COLON,    // ":"
    T_COMMA,    // ","
    T_OPEN_P,   // "("
    T_CLOSE_P,  // ")"
    T_PLUS,     // "+"
    T_MINUS,    // "-"
    T_DIV,      // "/"
    T_MULT,     // "*"
    T_ID,       // [a-zA-Z][a-zA-Z0-9]*
    T_NUMBER,   // [0-9]+
    T_EOF,
    T_ERROR,
};

static const char *token_str[] =
{
    [T_VAR] = "T_VAR",
    [T_ASSIGN] = "T_ASSIGN",
    [T_FUNC] = "T_FUNC",
    [T_COLON] = "T_COLON",
    [T_COMMA] = "T_COMMA",
    [T_OPEN_P] = "T_OPEN_P",
    [T_CLOSE_P] = "T_CLOSE_P",
    [T_PLUS] = "T_PLUS",
    [T_MINUS] = "T_MINUS",
    [T_DIV] = "T_DIV",
    [T_MULT] = "T_MULT",
    [T_ID] = "T_ID",
    [T_NUMBER] = "T_NUMBER",
    [T_EOF] = "T_EOF",
    [T_ERROR] = "T_ERROR",
};

#endif /* TOKEN_HEADER */
