static boolean mayBeString(Node n, boolean recurse) {
  if (n == null) {
    return false;
  }
  if (recurse) {
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
      if (mayBeString(child, true)) {
        return true;
      }
    }
   