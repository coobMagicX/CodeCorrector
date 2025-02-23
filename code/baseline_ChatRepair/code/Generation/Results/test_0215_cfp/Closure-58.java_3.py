case Token.FOR:
  if (!NodeUtil.isForIn(n)) {
    computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, conditional);
  } else {
    // for(x in y) {...}
    Node lhs = n.getFirstChild();
    Node rhs = lhs.getNext();
    if (NodeUtil.isVar(lhs)) {
      // for(var x in y) {...}
      lhs = lhs.getLastChild();
    }
    // Check if lhs is a NAME node, which was the original expected node type.
    // We have to also handle cases where lhs could be an expression.
    if (NodeUtil.isName(lhs)) {
      addToSetIfLocal(lhs, kill);
      addToSetIfLocal(lhs, gen);
    } else {
      // Handle general expression in lhs of FOR-IN
      computeGenKill(lhs, gen, kill, conditional);
    }
    computeGenKill(rhs, gen, kill, conditional);
  }
  return;
