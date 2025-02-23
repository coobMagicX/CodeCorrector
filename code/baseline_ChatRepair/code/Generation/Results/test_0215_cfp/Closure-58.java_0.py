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
    // Handling cases where lhs might be a more complex expression like 'a[1]'
    if (NodeUtil.isName(lhs) || NodeUtil.isGetProp(lhs) || NodeUtil.isArrayAccess(lhs)) {
      computeGenKill(lhs, gen, kill, conditional);
      addToSetIfLocal(lhs, kill);
      addToSetIfLocal(lhs, gen);
    } else {
      addToSetIfLocal(lhs, kill);
      addToSetIfLocal(lhs, gen);
    }
    computeGenKill(rhs, gen, kill, conditional);
  }
  return;
