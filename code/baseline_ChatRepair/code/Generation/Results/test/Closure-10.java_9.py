static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    return anyChildMayBeString(n);
  } else {
    return mayBeStringHelper(n);
  }
}

static boolean mayBeStringHelper(Node n) {
  return n.isString() || (n.isName() && "string".equals(n.getString()));
}

static boolean anyChildMayBeString(Node n) {
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    if (mayBeString(child, true)) {
      return true;
   