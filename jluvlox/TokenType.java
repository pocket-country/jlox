package jluvlox;

enum TokenType {
  // Single-character scanTokens.
  LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
  COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

  // One or two character tokens.
  // ToDo use SAS style Le, Ge Eq ... for comparison ops
  // this would go in the scanner ...
  BANG, BANG_EQUAL, EQUAL, EQUAL_EQUAL,
  GREATER, GREATER_EQUAL, LESS, LESS_EQUAL,

  // Literals.
  IDENTIFIER, STRING, NUMBER,

  // Keywords.
  AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
  PRINT, RETURN, SUPER, THIS, TOKE, TRUE, VAR, WHILE,

  EOF
}
