static boolean mayBeStringHelper(Node n) {
  if (n.getType() == Token.TERNARY) {
    // Handle ternary operator
    Node condition = n.getFirstChild();
    Node trueBranch = n.getMiddleChild();
    Node falseBranch = n.getLastChild();

    TernaryValue conditionType = getImpureBooleanValue(condition);
    boolean mayBeStringCondition = conditionType.isTrue() || conditionType.isFalse();

    boolean mayBeStringTrueBranch = mayBeStringHelper(trueBranch);
    boolean mayBeStringFalseBranch = mayBeStringHelper(falseBranch);

    // If the condition is a string, then the whole expression could be a string
    if (mayBeStringCondition) {
      return true;
    }

    // If the true branch is a string and the false branch is not,
    // then the whole expression could be a string
    if (mayBeStringTrueBranch && !mayBeStringFalseBranch) {
      return true;
    }

    // If both branches are strings or neither is, then the whole
    // expression is also a string
    return mayBeStringTrueBranch == mayBeStringFalseBranch;
  } else if (n.getType() == Token.BINARY || n.getType() == Token.UNARY) {
    // Correctly evaluating expression types for binary and unary operations
    Node left = n.getFirstChild();
    Node right = n.getLastChild();

    boolean mayBeStringLeft = mayBeStringHelper(left);
    boolean mayBeStringRight = mayBeStringHelper(right);

    // If either operand is a string, then the whole expression could be a string
    return mayBeStringLeft || mayBeStringRight;
  } else if (n.getType() == Token.CONCAT) {
    // Correctly evaluating expression types for concatenation operations
    Node left = n.getFirstChild();
    Node right = n.getLastChild();

    boolean mayBeStringLeft = mayBeStringHelper(left);
    boolean mayBeStringRight = mayBeStringHelper(right);

    return mayBeStringLeft && mayBeStringRight;
  } else {
    // For other types of nodes, just return whether the node itself is a string
    return n.getType() == Token.STRING || n.getType() == Token.LITERAL;
  }
}