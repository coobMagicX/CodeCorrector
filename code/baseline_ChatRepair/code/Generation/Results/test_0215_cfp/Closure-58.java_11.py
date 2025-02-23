case Token.FOR:
  Node lhs = n.getFirstChild();
  Node rhs = lhs.getNext();
  if (NodeUtil.isForIn(n)) {
    // Special handling for `for..in` loops.
    if (NodeUtil.isVar(lhs)) {
      // Simple variable declaration, e.g., `for (var x in y)`
      Node varName = lhs.getFirstChild(); // Get the variable node inside 'var'
      addToSetIfLocal(varName, kill); // OK to kill this as its scope is just this loop
      addToSetIfLocal(varName, gen);
    } else {
      // This is an expression or a more complex statement, e.g., `for (a[1] in foo)`
      // We should not mark `a` killed since `a[1]` is more about accessing a property than replacing `a`.
      computeGenKill(lhs, gen, kill, conditional); // compute effects for this expression
    }
    computeGenKill(rhs, gen, kill, conditional); // compute the effects for the rhs of the loop
  } else {
    // Regular `for` loop.
    computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, conditional);
  }
  return;
