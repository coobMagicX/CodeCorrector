case Token.FOR:
  if (!NodeUtil.isForIn(n)) {
    computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, conditional);
  } else {
    // for(x in y) {...}
    Node lhs = n.getFirstChild();
    Node rhs = lhs.getNext();
    if (NodeUtil.isVar(lhs)) {
      // for(var x in y) {...}
      lhs = lhs.getLastChild().getLastChild(); // Get the initializer of the var statement
    }
    computeGenKill(lhs, gen, kill, conditional);  // Handle more complex lhs like a[1]
    computeGenKill(rhs, gen, kill, conditional);  // Continue to handle the right-hand side expression
  }
  return;
