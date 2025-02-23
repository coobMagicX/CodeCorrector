static boolean mayBeString(Node n, boolean recurse) {
  if (recurse && n.hasChildren()) {
    // Recursively check all children if recursion is requested and children exist
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
      if (mayBeString(child, true)) {
        return true;
      }
    }
    return false;
  } else {
    // Use helper without recursion for the node itself
    return mayBeStringHelper(n);
  }
}
