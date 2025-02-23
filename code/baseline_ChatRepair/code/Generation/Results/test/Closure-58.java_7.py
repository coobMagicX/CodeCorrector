case Token.FOR:
  if (!NodeUtil.isForIn(n)) {
    computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, conditional);
  } else {
    // for(x in y) {...}
    Node lhs = n.getFirstChild();
    Node rhs = lhs.getNext();
    if (NodeUtil.isVar(lhs)) {
      // It's a var statement; let's get the first child of that var which would be the variable name node
      lhs = lhs.getFirstChild();
    }
    // We need to handle the lhs as it might not be a simple variable (e.g., var x or x, but could be x[1] or similar)
    computeGenKill(lhs, gen, kill, conditional);  // Properly handle any lhs
    addToSetIfLocal(lhs, kill);
    addToSetIfLocal(lhs, gen);
    computeGenKill(rhs, gen, kill, conditional);  // compute rhs of the for-in
  }
  return;
