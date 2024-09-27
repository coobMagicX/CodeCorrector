private boolean hasExceptionHandler(Node cfgNode) {
  if (cfgNode == null) {
    return false;
  }
  
  List<Node> children = cfgNode.getChildren();
  for (Node child : children) {
    if (child.getType() == NodeType.CATCH) {
      return true;
    }
    
    boolean childHasExceptionHandler = hasExceptionHandler(child);
    if (childHasExceptionHandler) {
      return true;
    }
  }
  
  return false;
}