static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    return mayBeStringHelper(n);
  }
}

static boolean mayBeStringHelper(Node n) {
  return !isNumericResult(n) && !isBooleanResult(n)
      && !isUndefined(n) && !n.isNull();
}

static boolean allResultsMatch(Node n, Predicate<Node> p) {
  switch (n.getType()) {
    case Token.ASSIGN:
    case Token.COMMA:
      return allResultsMatch(n.getLastChild(), p);
    case Token.AND:
    case Token.OR:
      return allResultsMatch(n.getFirstChild(), p)
          && allResultsMatch(n.getLastChild(), p);
    case Token.HOOK:
      return allResultsMatch(n.getFirstChild().getNext(), p)
          && allResultsMatch(n.getLastChild(), p);
    default:
      return p.apply(n);
  }
}

static boolean anyResultsMatch(Node n, Predicate<Node> p) {
  switch (n.getType()) {
    case Token.ASSIGN:
    case Token.COMMA:
      return anyResultsMatch(n.getLastChild(), p);
    case Token.AND:
    case Token.OR:
      return anyResultsMatch(n.getFirstChild(), p)
          || anyResultsMatch(n.getLastChild(), p);
    case Token.HOOK:
      return anyResultsMatch(n.getFirstChild().getNext(), p)
          || anyResultsMatch(n.getLastChild(), p);
    default:
      return p.apply(n);
  }
}