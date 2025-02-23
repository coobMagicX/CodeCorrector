public boolean apply(Node n) {
  
  
  if (n == null) {
    return false;
  }

  
  boolean hasSideEffects = (n.isCall() && NodeUtil.functionCallHasSideEffects(n)) ||
                           (n.isNew() && NodeUtil.constructorCallHasSideEffects(n));

  
  if (n.isDelProp()) {
    hasSideEffects = true;
  }

  
  if (hasSideEffects) {
    return true;
  }

  
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(child) && apply(child)) {
      return true;
    }
  }
  return false;
}
