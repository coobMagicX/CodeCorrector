case Token.FOR:
  if (!NodeUtil.isForIn(n)) {
    computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, conditional);
  } else {
    // for(x in y) {...}
    Node lhs = n.getFirstChild();
    Node rhs = lhs.getNext();
    if (NodeUtil.isVar(lhs)) {
      // for(var x in y) {...}
      lhs = lhs.getFirstChild(); // the variable node under 'var'
      for (Node varChild = lhs; varChild != null; varChild = varChild.getNext()) {
        if (varChild.hasChildren()) {
          computeGenKill(varChild.getFirstChild(), gen, kill, conditional);
        }
        addToSetIfLocal(varChild, kill); // kill the variable itself
      }   
    } else {
      // When lhs is not a simple variable declaration like in `for(a[1] in foo)`
      // We still perform read operation computations on lhs since variables/member expr might be referred.
      computeGenKill(lhs, gen, kill, conditional);  
    }
    
    computeGenKill(rhs, gen, kill, conditional);
  }
  return;
