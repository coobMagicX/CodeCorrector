case Token.FOR:
  // Examining whether it is a simple for loop or a for-in loop
  if (!NodeUtil.isForIn(n)) {
    computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, conditional);
  } else {
    // Handling for-in loops: for(lhs in rhs) {...}
    Node lhs = n.getFirstChild();
    Node rhs = lhs.getNext();

    // If lhs is a VAR node (i.e., var x in obj), handle the variable declaration 
    if (lhs.isVar()) {
      lhs = lhs.getFirstChild().getFirstChild();  // accessing the variable name
    }

    // Recursive call to compute gen/kill sets of lhs, considering side effects or complexities like array access
    computeGenKill(lhs, gen, kill, conditional);

    // Only add to kill set if lhs is a direct variable, not required for complex expressions
    if (lhs.isName()) {
      addToSetIfLocal(lhs, kill);
    }
    
    // For any lhs expression, determine if it's a local variable possibly redefined
    addToSetIfLocal(lhs, gen);

    // Computing gen/kill for rhs of the for-in loop
    computeGenKill(rhs, gen, kill, conditional);
  }
  return;
