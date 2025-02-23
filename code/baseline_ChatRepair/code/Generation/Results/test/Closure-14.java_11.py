  Node parent = node.getParent();
  if (parent == null || parent.isFunction() || node.isReturn() ||
      (cfa != null && node == cfa.root)) {
    return null; // Here we might need to return a symbolic return node, not null
  }
