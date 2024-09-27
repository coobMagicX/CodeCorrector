private boolean hasExceptionHandler(Node cfgNode) {
  for (Node child : cfgNode.children()) {
    if (child instanceof CatchNode) {
      return true;
    }
  }
  return false;
}