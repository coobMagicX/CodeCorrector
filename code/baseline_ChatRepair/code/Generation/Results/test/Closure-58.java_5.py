case Token.FOR:
  if (!NodeUtil.isForIn(n)) {
    computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, conditional);
  } else {
    // for(x in y) {...}
    Node lhs = n.getFirstChild();
    Node rhs = lhs.getNext();
    // Process the lhs, which could be a complex expression.
    computeGenKill(lhs, gen, kill, conditional);  // computeGenKill for the lhs expression whether a Name, Var, or more complex expression
    computeGenKill(rhs, gen, kill, conditional);  // computeGenKill for the rhs, the object being iterated over.
  }
  return;
