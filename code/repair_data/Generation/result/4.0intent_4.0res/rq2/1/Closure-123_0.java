void add(Node n, Context context) {
  if (!cc.continueProcessing()) {
    return;
  }

  int type = n.getType();
  String opstr = NodeUtil.opToStr(type);
  int childCount = n.getChildCount();
  Node first = n.getFirstChild();
  Node last = n.getLastChild();

  // Handle all binary operators
  if (opstr != null && first != last) {
    Preconditions.checkState(
        childCount == 2,
        "Bad binary operator \"%s\": expected 2 arguments but got %s",
        opstr, childCount);
    int p = NodeUtil.precedence(type);

    // For right-hand-side of operations, only pass context if it's
    // the IN_FOR_INIT_CLAUSE one.
    Context rhsContext = getContextForNoInOperator(context);

    // Handle associativity.
    // e.g. if the parse tree is a * (b * c),
    // we can simply generate a * b * c.
    if (last.getType() == type &&
        NodeUtil.isAssociative(type)) {
      addExpr(first, p, context);
      cc.addOp(opstr, true);
      addExpr(last, p, rhsContext);
    } else if (NodeUtil.isAssignmentOp(n) && NodeUtil.isAssignmentOp(last.getFirstChild())) {
      // Assignments are the only right-associative binary operators
      addExpr(first, p, context);
      cc.addOp(opstr, true);
      addExpr(last.getFirstChild(), p, rhsContext);
    } else {
      unrollBinaryOperator(n, type, opstr, context, rhsContext, p, p + 1);
    }
    return;
  }

  cc.startSourceMapping(n);

  switch (type) {
    case Token.FOR:
      if (childCount == 4) {
        add("for(");
        if (first.isVar() || first.isIn()) {
          add(first, Context.IN_FOR_INIT_CLAUSE);
        } else {
          addExpr(first, 0, Context.IN_FOR_INIT_CLAUSE);
        }
        add(";");
        add(first.getNext());
        add(";");
        add(first.getNext().getNext());
        add(")");
        addNonEmptyStatement(
            last, getContextForNonEmptyExpression(context), false);
      } else {
        Preconditions.checkState(childCount == 3);
        add("for(");
        add(first);
        add("in");
        add(first.getNext());
        add(")");
        addNonEmptyStatement(
            last, getContextForNonEmptyExpression(context), false);
      }
      break;
    // Existing switch case implementations...
  }
  cc.endSourceMapping(n);
}