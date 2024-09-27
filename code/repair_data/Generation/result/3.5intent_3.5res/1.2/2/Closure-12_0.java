private boolean hasExceptionHandler(Node cfgNode) {
  for (Node child : cfgNode.getChildren()) {
    if (child.getType() == Node.Type.CATCH) {
      return true;
    }
  }
  return false;
}