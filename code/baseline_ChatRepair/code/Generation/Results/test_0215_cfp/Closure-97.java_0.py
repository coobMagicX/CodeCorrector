long lvalInt = (int) lval;
long rvalInt = (int) rval;

switch (n.getType()) {
  case Token.LSH:
    result = lvalInt << rvalInt;
    break;
  case Token.RSH:
    result = lvalInt >> rvalInt;
    break;
  case Token.URSH:
    // Use a long to accommodate 32-bit unsigned right shift.
    result = (lvalInt & 0xFFFFFFFFL) >>> rvalInt;
    break;
  default:
    throw new AssertionError("Unknown shift operator: " + Node.tokenToName(n.getType()));
}
