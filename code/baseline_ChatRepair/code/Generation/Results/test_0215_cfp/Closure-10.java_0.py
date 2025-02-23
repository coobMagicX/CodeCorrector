static boolean mayBeString(Node n, boolean recurse) {
  if (n == null) {
    return false;
  }
  if (recurse) {
    // If recursing, handle both children of a conditional operation
    if (n.isHook()) {  // Assuming the Node uses 'isHook()' to check for conditional (? : ) expressions.
      return mayBeString(n.getFirstChild(), true) ||
             mayBeString