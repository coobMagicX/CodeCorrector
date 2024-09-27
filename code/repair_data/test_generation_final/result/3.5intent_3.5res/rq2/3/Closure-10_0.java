static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    return mayBeStringHelper(n);
  }
}

static boolean allResultsMatch(Node node, Predicate<Node> predicate) {
  for (Node result : node.getResults()) {
    if (!predicate.test(result)) {
      return false;
    }
  }
  return true;
}

static boolean mayBeStringHelper(Node n) {
  if (n instanceof StringNode) {
    return true;
  }
  if (n instanceof BinaryOperationNode) {
    BinaryOperationNode binaryOpNode = (BinaryOperationNode) n;
    return mayBeStringHelper(binaryOpNode.getLeft()) || mayBeStringHelper(binaryOpNode.getRight());
  }
  return false;
}

static Predicate<Node> MAY_BE_STRING_PREDICATE = node -> {
  if (node instanceof StringNode) {
    return true;
  }
  if (node instanceof BinaryOperationNode) {
    BinaryOperationNode binaryOpNode = (BinaryOperationNode) node;
    return MAY_BE_STRING_PREDICATE.test(binaryOpNode.getLeft()) || MAY_BE_STRING_PREDICATE.test(binaryOpNode.getRight());
  }
  return false;
};