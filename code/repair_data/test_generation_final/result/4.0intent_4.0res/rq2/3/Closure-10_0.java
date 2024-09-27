static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    return allResultsMatch(n, node -> {
      // Check if the node itself matches the MAY_BE_STRING_PREDICATE or if it is a ternary operation
      // where any of the operands might be a string.
      if (MAY_BE_STRING_PREDICATE.test(node)) {
        return true;
      } else if (node.isTernary()) {
        Node firstOperand = node.getFirstChild();
        Node secondOperand = firstOperand.getNext();
        Node thirdOperand = secondOperand.getNext();
        return MAY_BE_STRING_PREDICATE.test(firstOperand) || 
               MAY_BE_STRING_PREDICATE.test(secondOperand) || 
               MAY_BE_STRING_PREDICATE.test(thirdOperand);
      }
      return false;
    });
  } else {
    return mayBeStringHelper(n);
  }
}