static boolean isValidDefineValue(Node val, Set<String> defines) {
  switch (val.getType()) {
    case Token.STRING:
    case Token.NUMBER:
    case Token.TRUE:
    case Token.FALSE:
      return true;

    // Binary operators are only valid if both children are defined and valid.
    case Token.BITAND:
    case Token.BITOR:
    case Token.BITXOR:
      Node left = val.getFirstChild();
      Node right = val.getLastChild();
      if (isValidDefineValue(left, defines) && isValidDefineValue(right, defines)) {
        return true;
      }
      return false;

    // Unary operators are valid if the child is defined and valid.
    case Token.BITNOT:
    case Token.NOT:
    case Token.NEG:
      Node child = val.getFirstChild();
      if (child != null && isValidDefineValue(child, defines)) {
        return true;
      }
      return false;

    // Names are valid if and only if they are defined themselves.
    case Token.NAME:
    case Token.GETPROP:
      String name = val.isQualifiedName() ? val.getQualifiedName() : val.getString();
      if (defines.contains(name)) {
        return true;
      }
  }

  return false;
}