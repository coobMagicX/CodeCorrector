public boolean apply(Node n) {
  
  
  if (n == null) {
    return false;
  }

  
  if ((n.isCall() && NodeUtil.functionCallHasSideEffects(n)) || 
      (n.isNew() && NodeUtil.constructorCallHasSideEffects(n)) ||
      (n.isDelProp())) {  
    return true;
  }

  
  for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(c) && apply(c)) {
      return true;
    }
  }
  return false;
}
