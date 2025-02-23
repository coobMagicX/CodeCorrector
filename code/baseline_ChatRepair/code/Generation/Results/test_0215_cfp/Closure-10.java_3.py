static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    while (n != null) {
      if (mayBeStringHelper(n)) {
        return true;
      }
      n = n.getFirstChild();
    }
    return false;
  } else {
    return mayBeStringHelper(n);
  }
}
