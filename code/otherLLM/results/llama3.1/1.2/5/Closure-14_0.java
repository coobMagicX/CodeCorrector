private static Node computeFallThrough(Node node) {
  if (node == null || !node.isTry()) {
    return null;
  }

  if (!NodeUtil.hasFinally(node)) {
    // If we are coming out of the TRY block...
    if (node.getFirstChild() == null) {
      Preconditions.checkState(false, "No child for try");
    }
    return computeFollowNode(null, node.getLastChild(), null);
  } else { 
    if (cfa != null && cfa.root == node){
      // If we are coming out of the FINALLY block...
      Node finallySibling = node.getNext();
      while (finallySibling != null && finallySibling.isFinally()) {
        finallySibling = finallySibling.getNext();
      }
      return computeFollowNode(null, finallySibling, null);
    } else {
      // If we are coming out of the CATCH or TRY block...
      if (!cfa.hasChild(node)) {
        return null;
      }

      Node catchBlock = cfa.getCatchBlock(node);
      if (catchBlock != null && cfa.hasFinally(catchBlock)){
        return computeFollowNode(null, catchBlock.getLastChild(), null);
      } else {
        // If we are coming out of the TRY or CATCH block...
        return computeFollowNode(null, node.getLastChild(), null);
      }
    }
  }
}

private static Node computeFallThrough(Node node, ControlFlowAnalysis cfa) {
  if (cfa == null || cfa.root != node){
    return computeFallThrough(node);
  }

  // If we are coming out of the FINALLY block...
  Preconditions.checkNotNull(cfa.finallyMap.get(node));
  for (Node finallyNode : cfa.finallyMap.get(node)) {
    cfa.createEdge(null, Branch.UNCOND, finallyNode);
  }
  return computeFallThrough(node.getNext(), cfa);
}