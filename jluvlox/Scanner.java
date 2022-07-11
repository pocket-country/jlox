package jluvlox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jluvlox.TokenType.*;

class Scanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  private int start = 0;
  private int current = 0;
  private int line = 1;

  // reserved words
  private static final Map<String, TokenType> keywords;
  static { keywords = new HashMap<>();
    keywords.put("and",   AND);
    keywords.put("class", CLASS);
    keywords.put("else",  ELSE);
    keywords.put("false", FALSE);
    keywords.put("for",   FOR);
    keywords.put("fun",   FUN);
    keywords.put("if",    IF);
    keywords.put("nil",   NIL);
    keywords.put("or",    OR);
    keywords.put("print", PRINT);
    keywords.put("return",RETURN);
    keywords.put("super", SUPER);
    keywords.put("this",  THIS);
    keywords.put("true",  TRUE);
    keywords.put("toke", TOKE);
    keywords.put("var",   VAR);
    keywords.put("while", WHILE);
  }

  Scanner(String source) {
    this.source = source;
  }

  List<Token> scanTokens() {
    while (!isAtEnd()) {
      // beginning of next lexeme.
      start = current;
      scanToken();
    }

    tokens.add(new Token(EOF, "", null, line));
    return tokens;
  }
  // helper - detect end of input
  private boolean isAtEnd() {
    return current >= source.length();
  }
  // the heart of the matter, scan one token from input stream of characters
  private void scanToken() {
    char c = advance();
    switch (c) {
      case '(': addToken(LEFT_PAREN); break;
      case ')': addToken(RIGHT_PAREN); break;
      case '{': addToken(LEFT_BRACE); break;
      case '}': addToken(RIGHT_BRACE); break;
      case ',': addToken(COMMA); break;
      case '.': addToken(DOT); break;
      case '-': addToken(MINUS); break;
      case '+': addToken(PLUS); break;
      case ';': addToken(SEMICOLON); break;
      case '*': addToken(STAR); break;
      // changed comment marker to ~>, so now handling "/" is simple:
      case '/': addToken(SLASH); break;
      case '!': addToken(match('=') ? BANG_EQUAL : BANG); break;
      case '=': addToken(match('=') ? EQUAL_EQUAL : EQUAL); break;
      case '<': addToken(match('=') ? LESS_EQUAL : LESS); break;
      case '>': addToken(match('=') ? GREATER_EQUAL : GREATER); break;
      case '~': // comment is ~>, look for > and go to EOLN
        if (match('>')) {
          // just slurp it up and ignore ... could do something more intresting?
          while (peek() != '\n' && !isAtEnd()) advance();
          String comment = source.substring(start + 2, current);
          Lox.recordComment( line, comment);
        } else {
          Lox.error(line, "Unexpected Character");
        }
        break;
      case ' ':
      case '\r':
      case '\t':
        // ignore whitespace - this is NOT python!
        break;
      case '\n': // so can count lines
        line++;
        break;
      // literals will go here .. strings, numbers and keywords.
      // but should run! ... and it does!
      case '"': string(); // quote starts a sting literal
        break;
      default:  // look for digits here - see CI p 52 for why
        if (isDigit(c)) {
          number();
        } else if (isAlpha(c)) {
          identifier();
        } else {
          Lox.error(line, "Unexpected Character");
        }
    }
  }
  // literal processing helpers
  private void string() {
    while (peek() != '"' && !isAtEnd()) {
      if (peek() == '\n') line++;
      advance();
    }
    if (isAtEnd()) {
      Lox.error(line, "Unterminated string.");
      return;
    }
    advance(); //the closing "
    //get the actual value -- trim quotes
    String value = source.substring(start + 1, current - 1);
    addToken(STRING, value);
  }
  private void number() {
    while (isDigit(peek())) advance();
    if (peek() =='.' && isDigit(peekNext())) {
      advance(); // eat the . & get mantissa
      while (isDigit(peek())) advance();
    }
    // using  java's internals to  parse into double value.
    addToken(NUMBER, Double.parseDouble(source.substring(start,current)));
  }
  private void identifier() {
    while (isAlphaNumeric(peek())) advance();
    String text = source.substring(start,current);
    TokenType type = keywords.get(text);
    if (type == null) type = IDENTIFIER;
    addToken(type);
  }
  // Helpers to move position, lookahead
  private char advance() {
    return source.charAt(current++);
  }
  private boolean match(char expected) {
    if (isAtEnd()) return false;
    if (source.charAt(current) != expected) return false;

    current++;
    return true;
  }
  private char peek() {
    if (isAtEnd()) return '\0';
    return source.charAt(current);
  }
  private char peekNext() {
    if (current + 1 >= source.length()) return '\0';
    return source.charAt(current + 1);
  }
  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }
  private boolean isAlpha(char c) {
    return  (c >= 'a' && c <= 'z') ||
            (c >= 'A' && c <= 'Z') ||
             c == '_';
  }
  private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }
  // add token to list note too 'signatures' to handle litreals
  private void addToken(TokenType type) {
    addToken(type, null);
  }
  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }
}
