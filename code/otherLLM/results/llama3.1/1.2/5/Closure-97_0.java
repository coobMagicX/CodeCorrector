private Node tryFoldShift(Node n, Node left, Node right) {
  if (left.getType() == Token.NUMBER &&
      right.getType() == Token.NUMBER) {

    double result;
    double lval = left.getDouble();
    double rval = right.getDouble();

    // check ranges.  We do not do anything that would clip the double to
    // a 32-bit range, since the user likely does not intend that.
    if (!(lval >= Integer.MIN_VALUE && lval <= Integer.MAX_VALUE)) {
      error(BITWISE_OPERAND_OUT_OF_RANGE, left);
      return n;
    }

    // only the lower 5 bits are used when shifting, so don't do anything
    // if the shift amount is outside [0,32)
    if (!(rval >= 0 && rval < 32)) {
      error(SHIFT_AMOUNT_OUT_OF_BOUNDS, right);
      return n;
    }

    long lvalLong = (long) lval;
    int rvalInt = (int) rval;

    // Handle integer overflow for left-hand operand
    if ((lval & ~0x1L) != 0 || ((lval >> 31) & 1) == 1 && rvalInt >= 32) {
      result = lvalLong >> rvalInt;
    } else {
      long lvalLongSigned = (lval < 0 ? -((long) Math.abs(lval)) + 1 : (long) Math.abs(lval));
      result = (rvalInt >= 32 ? ((lvalLongSigned & 0xFFFFFFFFL) >> rvalInt) | ~0x1L << (64 - rvalInt)
          : lvalLongSigned >>> rvalInt);
    }

    // Convert the numbers to ints
    int lvalInt = (int) lval;
    if (lvalInt != lval) {
      error(FRACTIONAL_BITWISE_OPERAND, left);
      return n;
    }
    int rvalIntUnsigned = (int) rval;
    if (rvalIntUnsigned != rval) {
      error(FRACTIONAL_BITWISE_OPERAND, right);
      return n;
    }

    switch (n.getType()) {
      case Token.LSH:
        result = lvalInt << rvalIntUnsigned;
        break;
      case Token.RSH:
        result = lvalInt >> rvalIntUnsigned;
        break;
      case Token.URSH:
        // JavaScript handles zero shifts on signed numbers differently than
        // Java as an Java int can not represent the unsigned 32-bit number
        // where JavaScript can so use a long here.
        result = lvalInt >>> rvalIntUnsigned;
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