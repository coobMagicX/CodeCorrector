static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
      if (mayBeString(child, true)) {
        return true;
      }
    }
    return false;
  } else {
    return mayBeStringHelper(n);
  }
}

static boolean mayBeStringHelper(Node n) {
  switch (n.getType()) {
    case Token.STRING:
      return true;
    case Token.NAME:
      // Depending on the context, you might have additional logic to determine if a NAME can