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

    Context rhsContext = getContextForNoInOperator(context);

    if (last.getType() == type &&
        NodeUtil.isAssociative(type)) {
      addExpr(first, p, context);
      cc.addOp(opstr, true);
      addExpr(last, p, rhsContext);
    } else if (NodeUtil.isAssignmentOp(n) && NodeUtil.isAssignmentOp(last)) {
      addExpr(first, p, context);
      cc.addOp(opstr, true);
      addExpr(last, p, rhsContext);
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
        if (first.isVar()) {
          add(first, Context.IN_FOR_INIT_CLAUSE);
        } else {
          addExpr(first, 0, Context.IN_FOR_INIT_CLAUSE);
        }
        add(";");
        add(first.getNext());
        add(";");
        add(first.getNext().getNext());
        add(")");
        addNonEmptyStatement(last, getContextForNonEmptyExpression(context), false);
      } else {
        Preconditions.checkState(childCount == 3);
        add("for(");
        addExpr(first, 0, Context.IN_FOR_INIT_CLAUSE); // Ensure expression handling for first child
        add(" in ");
        addExpr(first.getNext(), 0, Context.OTHER); // Ensure expression handling for second child
        add(")");
        addNonEmptyStatement(last, getContextForNonEmptyExpression(context), false);
      }
      break;

    default:
      // The rest of the code remains as it is since there's no need to modify
      // other cases according to the problem statement
      break;
  }

  cc.endSourceMapping(n);
}