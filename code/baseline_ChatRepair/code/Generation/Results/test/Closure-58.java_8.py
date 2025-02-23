case Token.FOR:
  if (!NodeUtil.isForIn(n)) {
    computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, conditional);
  } else {
    // It's a for-in loop: for(x in y)
    Node lhs = n.getFirstChild();
    Node rhs = lhs.getNext();
    // Left-hand side (lhs) might be a var node or a more complex expression
    if (NodeUtil.isVar(lhs)) {
      // It's a var statement, like `for (var x in y)`
      lhs = lhs.getFirstFirstChild(); // The variable node itself.
    }

    computeGenKill(rhs, gen, kill, conditional); // First handle the right-hand side (rhs)

    // Analyze the left-hand side; if it's more complex, ensure proper handling
    computeGenKill(lhs, gen, kill, conditional);
    addToSetIfLocal(lhs, kill);
    addToSetIfLocal(lhs, gen); // The lhs of a for-in is always assigned to, so we need to handle it for genesis and killing
  }
  return;
