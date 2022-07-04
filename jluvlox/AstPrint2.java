package jluvlox;

class AstPrint2 implements Expr.Visitor<Void> {

  private final StringBuilder exprSB;
  private int nodeLevel;

  public AstPrint2() {
    exprSB = new StringBuilder();
    // this is to keep track of how deeply we are "indented"
    // when printing, which is basically how deep the node is in the Ast
    // There is a ton of thinking behind this ...
    // Driven mainly by the fact taht the visit*() methods take a parameter
    // of type Expr, so we can't pass a string padding variable in to so as
    // to create a local copy.  So this is a global variable
    // Which means we have to truncate it when we
    // exit the level.  Which means we can't just return a properly formatted
    // string, as we have to be able to truncate/decrement after we build the
    // string/return from the recursive calls.  .... aarg i hate these big
    // blocks of comments that never seem to make sense ... this belongs in
    // the development notebook.
    nodeLevel = 0;
  }
  String print(Expr expr) {
    expr.accept(this);
    return exprSB.toString();
  }

  @Override
  public Void visitAssignExpr(Expr.Assign expr) {
    nodeLevel++;
    makeline("Assign -> " + expr.name.type.name());
    expr.value.accept(this);
    nodeLevel--;
    return null;
  }
  @Override
  public Void visitBinaryExpr(Expr.Binary expr) {
    nodeLevel++;
    makeline(expr.operator.type.name());
    expr.left.accept(this);
    expr.right.accept(this);
    nodeLevel--;
    return null;
  }

  @Override
  public Void visitGroupingExpr(Expr.Grouping expr) {
    nodeLevel++;
    makeline("group");
    expr.expression.accept(this);
    nodeLevel--;
    return null;
  }

  @Override
  public Void visitLiteralExpr(Expr.Literal expr) {
    nodeLevel++;
    if (expr.value == null) {
      makeline("nil");
    } else {
      makeline(expr.value.toString());
    }
    nodeLevel--;
    return null;
  }

  @Override
  public Void visitVariableExpr(Expr.Variable expr) {
    nodeLevel++;
    makeline(expr.name.type.name());
    nodeLevel--;
    return null;

  }
  @Override
  public Void visitUnaryExpr(Expr.Unary expr) {
    nodeLevel++;
    makeline(expr.operator.type.name());
    expr.right.accept(this);
    nodeLevel--;
    return null;
  }

  //private void pad(String padchr) {
  //  padding.append(padchr);
  //}
  private void makeline(String name) {
    StringBuilder padding = new StringBuilder();
    for (int i = 0; i < nodeLevel; i++) {
      // ToDo: enhance with line drawing characters
      padding.append("  ");
    }
    exprSB.append(padding.toString()).append(String.valueOf(nodeLevel)).append(":").append(name).append("\n");
    // not using (Expr expr : exprs) syntax so can tell when 1st arg
    //for (int i = 0; i < exprs.length; i++) {
    //  builder.append("\n");
    //  if (i == 0 ) { pad(">"); }
    //  builder.append(exprs[i].accept(this));
    //}
    //builder.append(")");

  //return builder.toString();
 }
}
