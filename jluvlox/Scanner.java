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
      case '!': addToken(match('=') ? BANG_EQUAL : BANG); break;
      case '=': addToken(match('=') ? EQUAL_EQUAL : EQUAL); break;
      case '<': addToken(match('=') ? LESS_EQUAL : LESS); break;
      case '>': addToken(match('=') ? GREATER_EQUAL : GREATER); break;
      case '/':          // handle comments, look for 2nd '/' and go to EOLN
        if (match('/')) {
          // just slurp it up and ignore ... could do something more intresting?
          while (peek() != '\n' && !isAtEnd()) advance();
        } else {
          addToken(SLASH);
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
      // literals will go here .. strings, numbers and keywords.  but should run!
      default:
        Lox.error(line, "Unexpected Character");
    }
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
  // add token to list note too 'signatures' to handle litreals
  private void addToken(TokenType type) {
    addToken(type, null);
  }
  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }
}
