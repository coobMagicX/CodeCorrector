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
    long rvalLong = (long) rval; // Convert to long to avoid overflow for unsigned shifts
    if (!(rvalLong >= 0 && rvalLong < 64)) { // Change range from 32 to 64 to handle unsigned shifts correctly
      error(SHIFT_AMOUNT_OUT_OF_BOUNDS, right);
      return n;
    }

    // Convert the numbers to ints
    int lvalInt = (int) lval;
    if (lvalInt != lval) {
      error(FRACTIONAL_BITWISE_OPERAND, left);
      return n;
    }

    long rvalLongCast = rvalLong; // Cast to long to preserve value for unsigned shifts
    if (rvalLongCast == 0 && lval >= 0 && lval < Math.pow(2, 31)) { // Handle zero shift on signed numbers correctly
      result = lval;
    } else {
      int rvalInt = (int) rvalLongCast;
      if (rvalInt != rvalLongCast) {
        error(FRACTIONAL_BITWISE_OPERAND, right);
        return n;
      }

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
          result = lvalInt >>> rvalInt;
          break;
        default:
          throw new AssertionError("Unknown shift operator: " +
              Node.tokenToName(n.getType()));
      }
    }

    Node newNumber = Node.newNumber(result);
    n.getParent().replaceChild(n, newNumber);
    reportCodeChange();

    return newNumber;
  }

  return n;
}