package com.SaravananInterpeter.Jnox;

public enum TokenType {
    // Single Character tokens.
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    //one or two character tokens
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // Literals.
    IDENTIFIER, STRING, NUMBER,

    // Keywords
    AND, OR, CLASS, FUN, ELSE, IF, FALSE, TRUE, FOR,
    PRINT, RETURN, SUPER, THIS,  VAR, WHILE, NIL,

    EOF
}
