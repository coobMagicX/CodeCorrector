static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    return mayBeStringHelper(n);
  }
}

static boolean allResultsMatch(Node n, Predicate<Node> predicate) {
  // implementation of allResultsMatch method goes here
}

static boolean mayBeStringHelper(Node n) {
  // implementation of mayBeStringHelper method goes here
}

static Predicate<Node> MAY_BE_STRING_PREDICATE = new Predicate<Node>() {
  @Override
  public boolean test(Node n) {
    // implementation of test method goes here
  }
};