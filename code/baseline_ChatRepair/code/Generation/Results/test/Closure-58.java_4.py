case Token.FOR:
  if (!NodeUtil.isForIn(n)) {
    computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, conditional);
  } else {
    // Handling the 'for-in' loop
    Node lhs = n.getFirstChild();
    Node rhs = lhs.getNext();
    if (NodeUtil.isVar(lhs)) {
      // Handle 'for(var x in y)' syntax
      lhs = lhs.getFirstChild().getLastChild(); // get the initializer part of 'var x'
    }
    // Recursively compute GEN/KILL sets for lhs expression which now correctly handles complex expressions
    computeGenKill(lhs, gen, kill, conditional);
    // Compute GEN/KILL sets for rhs expression
    computeGenKill(rhs, gen, kill, conditional);
  }
  return;
