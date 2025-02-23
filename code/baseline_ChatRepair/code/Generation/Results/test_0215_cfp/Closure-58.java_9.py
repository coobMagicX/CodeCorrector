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
      addToSetIfLocal(lhs, kill);
      addToSetIfLocal(lhs, gen);
    } else if (NodeUtil.isName(lhs)) {
      addToSetIfLocal(lhs, kill);
      addToSetIfLocal(lhs, gen);
    } else {
      // Complex expression such as a[1] in `for (a[1] in foo)`
      computeGenKill(lhs, gen, kill, conditional);  // compute gen/kill for the expression
    }
    computeGenKill(rhs, gen, kill, conditional);
  }
  return;
