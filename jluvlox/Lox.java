package jluvlox;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
  private static final Interpreter interpreter = new Interpreter();
  // see notes p42 regarding this global
  static boolean hadError = false;
  // p 107
  static boolean hadRuntimeError = false;
  // my mod!  Comments go to file!
  static boolean recordComments = false;
  static PrintWriter commentFile;

  public static void main(String[] args) throws IOException {
    if (args.length > 1) {
      System.out.println("Usage: Lox [script]");
      System.exit(64);
    } else if (args.length == 1) {
      enableComment();
      runFile(args[0]);
      commentFile.close();
    } else {
      runPrompt();
    }
  }
  // two wrappers to provide two methods of running lox code
  private static void runFile(String path) throws IOException {
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    run( new String(bytes, Charset.defaultCharset()));
    // indicate error on exit code
    if (hadError) System.exit(65);
    if (hadError) System.exit(70);
  }
  private static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);

    for (;;) {
      System.out.print("l> ");
      String line = reader.readLine();
      if (line == null) break;
      run(line);
      hadError = false;
    }
    System.out.println("Bye Bye Baby!");
  }
  // and the actual run method itself
  private static void run(String source) {
    Scanner scanner = new Scanner(source);
    List<Token> tokens = scanner.scanTokens();
    Parser parser = new Parser(tokens);
    List<Stmt> statements = parser.parse();
    // stop if error
    if (hadError) return;
    // only thing to do with this is print it as we don't have an interpreter yet
    //System.out.println(new AstPrint2().print(expression));
    //interpreter.interpret(expression);
    interpreter.interpret(statements);
  }
  // error handling
  // - bare bones p41
  static void error(int line, String message) {
    report(line, " ", message);
  }
  // - parser (note different signature)
  static void error(Token token, String message) {
    if (token.type == TokenType.EOF) {
      report(token.line, "at end", message);
    } else {
      report(token.line, "at '" + token.lexeme + "'", message);
    }
  }
  // runtime p107
  static void runtimeError(RuntimeError error) {
    System.err.println(error.getMessage() +
      "\n[line " + error.token.line + "]" );
    hadRuntimeError = true;
  }
  // helper for reporting errors
  private static void report(int line, String where, String message) {
    System.err.println(
      "[line " + line + "] Error " + where + ": " + message
    );
    hadError = true;
  }
  // and for stashing away comments in a file ... my little wrinkle
  private static void enableComment() throws IOException {
    // ToDo better naming of comment file.
    // ToDo figure out appending for interactive comment gathering ...
    // not that that will ever  be used, who comments a REPL loop?
    //commentFile = new PrintWriter("comments.txt");
    commentFile = new PrintWriter("comments.txt");
    recordComments = true;
  }
  static void recordComment(int line, String comment) {
    commentFile.println("[line " + line + "]" + comment);
  }
}
