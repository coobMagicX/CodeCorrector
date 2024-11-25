private Node tryFoldShift(Node n, Node left, Node right) {
  if (left.getType() == Token.NUMBER &&
      right.getType() == Token.NUMBER) {

    double result;
    double lval = left.getDouble();
    double rval = right.getDouble();

    // Add input validation: Verify that the input nodes are indeed integers
    if (!(lval >= Integer.MIN_VALUE && lval <= Integer.MAX_VALUE)) {
      error(BITWISE_OPERAND_OUT_OF_RANGE, left);
      return n;
    }
    if (!(rval >= 0 && rval < 32)) {
      error(SHIFT_AMOUNT_OUT_OF_BOUNDS, right);
      return n;
    }

    // Validate shift amount: Check that the shift amount is an integer
    long rvalInt = (long) rval;
    if (rvalInt != rval) {
      error(FRACTIONAL_SHIFT_AMOUNT, right);
      return n;
    }

    int lvalInt = (int) lval;

    switch (n.getType()) {
      case Token.LSH:
        result = lvalInt << rvalInt;
        break;
      case Token.RSH:
        result = lvalInt >> rvalInt;
        break;
      case Token.URSH:
        // JavaScript handles zero shifts on signed numbers differently than
        // Java as an Java int can not represent the unsigned 32-bit number
        // where JavaScript can so use a long here.
        result = (int) ((long) lval << (rvalInt >>> 31));
        break;
      default:
        throw new AssertionError("Unknown shift operator: " +
            Node.tokenToName(n.getType()));
    }

    Node newNumber = Node.newNumber(result);
    n.getParent().replaceChild(n, newNumber);
    reportCodeChange();

    return newNumber;
  }

  return n;
}