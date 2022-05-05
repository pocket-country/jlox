package jluvlox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {

  // see notes CI p42 reegarding this global
  static boolean hadError = false;

  public static void main(String[] args) throws IOException {
    if (args.length > 1) {
      System.out.println("Usage: jlox [script]");
      System.exit(64);
    } else if (args.length == 1) {
      runFile(args[0]);
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

    // for now, just print tokens we have scanned
    for (Token token : tokens) {
      System.out.println(token);
    }
  }
  // error handling - bare bones
  static void error(int line, String message) {
    report(line, " ", message);
  }
  // helper
  private static void report(int line, String where, String message) {
    System.err.println(
      "[line " + line + "] Error" + where + ": " + message
    );
  }
}
