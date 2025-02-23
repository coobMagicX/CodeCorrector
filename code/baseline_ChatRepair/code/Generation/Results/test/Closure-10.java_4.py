static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
      if (mayBeString(child, true)) {
        return true;
      }
   