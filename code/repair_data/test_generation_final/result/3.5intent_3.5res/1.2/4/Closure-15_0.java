public boolean apply(Node n) {
  if (n == null) {
    return false;
  }

  if (n.isCall() && NodeUtil.functionCallHasSideEffects(n)) {
    return true;
  }

  if (n.isNew() && NodeUtil.constructorCallHasSideEffects(n)) {
    return true;
  }

  if (n.isDelete() && NodeUtil.deleteHasSideEffects(n)) {
    return true;
  }

  for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(c) && apply(c)) {
      return true;
    }
  }
  return false;
}