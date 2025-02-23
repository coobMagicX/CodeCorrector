public boolean apply(Node n) {
  
  
  if (n == null) {
    return false;
  }

  
  if ((n.isCall() && NodeUtil.functionCallHasSideEffects(n)) || 
      (n.isNew() && NodeUtil.constructorCallHasSideEffects(n))) {
    return true;
  }

  
  if (n.isDelProp()) { 
    return true;
  }

  
  if (n.isIn()) { 
    return true;
  }

  
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(child) && apply(child)) {
      return true;
