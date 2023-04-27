package com.SaravananInterpeter.Jnox;

public class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;

    // break down input streams into a series of tokens.

    Token(TokenType type, String lexeme, Object literal, int line){
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }
    // returns a string represetation of the Token object.
    public String toString(){
        return type + " " + lexeme + " " + literal;
    }
}
