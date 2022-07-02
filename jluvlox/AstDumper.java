package jluvlox;

// a mashup of the ASTPrinter class & a diagrammer from
// https://www.baeldung.com/java-print-binary-tree-diagram

class AstDumper implements Expr.Visitor<String> {

  // string builder to hold dump "image" - a single string
  // that appears as lines when we write to stdout b/c newlines
  private final StringBuilder dumpSB;
  // internal copy of expression tree (root node)
  // might get confusing as everything is named expr we will see
  private final Expr expr;

  // constructor
  AstDumper (Expr expr) {
    this.expr = expr;
    dumpSB = new StringBuilder();
  }
  String dump(Expr expr) {
    // does this return a string or a string builder?
    // if string builder, does top level need to stringify?
    return expr.accept(this);
  }
  // note unlike the example, we are not going to check for
  // null child notes ... we as this is a parse tree the leaf
  // nodes are literals, without _any_ children

  // and, as we are not inserting paranthesis to indicate
  // tree structure (the diagram does that) we ditch the
  // parenthesis function from ASTprinter
  @Override
  public StringBuilder visitBinaryExpr(Expr.Binary expr) {
    return parenthesize(expr.operator.lexeme, expr.left, expr.right);

}

  @Override
  public String visitGroupingExpr(Expr.Grouping expr) {
    return parenthesize("group", expr.expression);
  }

  @Override
  public String visitLiteralExpr(Expr.Literal expr) {
    if (expr.value == null) return "nil";
    return expr.value.toString();
  }

  @Override
  public String visitUnaryExpr(Expr.Unary expr) {
    return parenthesize(expr.operator.lexeme, expr.right);
  }

  private String dumpNodes(TokeyType name, String padding, Expr... exprs) {
    //sb.append("\n");
    dumpSB.append("\n")
    // next line starts with padding
    sb.append(padding);
    // this is the diagram character that points to node ascii rep of value
    sb.append(pointer);
    //sb.append(node.getValue());
    dumpSB.append(expr.operator.type)  // this is a enum, need to stringify?StringBuilder builder = new StringBuilder();

  builder.append("(").append(name);
  for (Expr expr : exprs) {
    builder.append(" ");
    builder.append(expr.accept(this));
  }
  builder.append(")");

  return builder.toString();
 }
 // main to test, comment out when want to use PP
 //public static void main(String[] args) {
 //  Expr expression = new Expr.Binary(
 //      new Expr.Unary(
 //          new Token(TokenType.MINUS, "-", null, 1),
 //          new Expr.Literal(123)),
 //      new Token(TokenType.STAR, "*", null, 1),
 //      new Expr.Grouping(
 //          new Expr.Literal(45.67)));
 //
 //  System.out.println(new AstPrinter().print(expression));
 //}
}
