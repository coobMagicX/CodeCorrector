public boolean apply(Node n) {
  
  
  if (n == null) {
    return false;
  }

  
  if ((n.isCall() && NodeUtil.functionCallHasSideEffects(n)) 
      || (n.isNew() && NodeUtil.constructorCallHasSideEffects(n))) {
    return true;
  }

  
  if (n.isDelProp()) {
    return true;
  }

  
  Node child = n.getFirstChild();
  while (child != null) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(child) && apply(child)) {
      return true;
    }
    child = child.getNext();
  }

  return false;
}
