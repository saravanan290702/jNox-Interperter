package com.SaravananInterpeter.Jnox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.SaravananInterpeter.Jnox.TokenType.*;
//import static com.SaravananInterpreter.Jnox.TokenType.EOF;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String,TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and",    AND);
        keywords.put("class",  CLASS);
        keywords.put("else",   ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for",    FOR);
        keywords.put("fun",    FUN);
        keywords.put("if",     IF);
        keywords.put("nil",    NIL);
        keywords.put("or",     OR);
        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
        keywords.put("super",  SUPER);
        keywords.put("this",   THIS);
        keywords.put("true",   TRUE);
        keywords.put("var",    VAR);
        keywords.put("while",  WHILE);
    }

    Scanner(String source) {
        this.source = source;
    }

    //Store the raw source code as a simple string.
    List<Token> scanTokens() {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }
    /* // this is a comment
     (( )){}  //grouping stuff
     !*+-/=<> <= ==  //operators */
    private void scanToken() {
        char c = advance();
        switch (c){
            // Single Character tokens.
            case '(':addToken(LEFT_PAREN); break;
            case ')':addToken(RIGHT_PAREN); break;
            case '{':addToken(LEFT_BRACE); break;
            case '}':addToken(RIGHT_BRACE); break;
            case ',':addToken(COMMA); break;
            case '.':addToken(DOT); break;
            case '-':addToken(MINUS); break;
            case '+':addToken(PLUS); break;
            case '*':addToken(STAR); break;
            case ';':addToken(SEMICOLON); break;
            // Operators
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if (match('/')) {
                    // A comment goes until the end of the line.
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else if (match('*')) {
                    // Handle block comment
                    comment();
                } else {
                    addToken(SLASH);
                }
                break;

            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;

            case '\n':
                line++;
                break;
            // String
            case '"': string(); break;
            default:
                if(isDigit(c))
                {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    jnox.error(line,"Unexpected character.");
                }
                break;

        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(start,current);
        TokenType type = keywords.get(text);
        if(type == null) type = IDENTIFIER;
        addToken(type);
    }

    // Adding /* */ comment support.
    private void comment(){
        while (peek() != '*' || peekNext() != '/'){
            if(isAtEnd()){
                jnox.error(line,"Unterminated comment.");
                return;
            }
            if(peek() == '\n') line++;
            advance();
        }

        // consume the closing */
        advance();
        advance();
    }

    // It scans the source code character by character until it encounters a closing double quote (")
    // constructs a new STRING token with the value of the string literal.
    private void string(){
        //runs as long as the next character is not " and the lexer has not consumed all characters in code
        while (peek() !='"' && !isAtEnd()){
            //  checks if the current character is a newline character
            //  and increments the line variable if it is.
            if(peek() == '\n') line++;
            advance();
        }

        if(isAtEnd()){
            jnox.error(line,"Undermined String");
            return;
        }
        // The closing (").
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start+1,current-1);
        addToken(STRING, value);
    }


    //now we convert the string/character to number by parsing
    private void number(){
        while (isDigit(peek())) advance();

        // look for a fractional part.
        if(peek() == '.' && isDigit(peekNext())){
            // consume the '.'
            advance();
            while (isDigit(peek())) advance();
        }
        addToken(NUMBER,Double.parseDouble(source.substring(start,current)));
    }

    // ------------------------------------------------HELPERS---------------------------------------------------------- //
    // Checks whether the lexer has consumed all characters in the source code.
    private boolean isAtEnd() {
        return current >= source.length();
    }

    // consumes the next character in the source file and returns it
    private char advance(){
        return source.charAt(current++);
    }

    //output of the input advance(), it takes the text of the current lexeme and creates a new token
    private void addToken(TokenType type){
        addToken(type,null);
    }
    // Overload to handle tokens with literals values soon
    private void addToken(TokenType type, Object literal){
        String text = source.substring(start,current);
        tokens.add(new Token(type, text, literal, line));
    }

    // checks whether the next character in the source code matches the specified character, and advances the lexer if it does.
     private boolean match(char expected){
        if(isAtEnd()) return false;
        if(source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    // looks ahead one character in the source code without consuming it.
    private char peek(){
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }

    // Checks where the character is a number or not
    private boolean isDigit(char c){
        return c>= '0' && c<= '9';
    }

    // for checking beyond '.'
    private char peekNext() {
        if(current+1>=source.length()) return '\0';
        return source.charAt(current + 1);
    }

    // to check if its alphabets or not caps or small letter
    private boolean isAlpha(char c){
        return (c>='a' && c>='z') || (c>='A' && c>='Z') || c == '_';
    }
    // to check if they are alphanumerics
    private boolean isAlphaNumeric(char c){
        return isAlpha(c) || isDigit(c);
    }
}
