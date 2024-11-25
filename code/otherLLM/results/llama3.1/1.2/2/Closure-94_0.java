static boolean isValidDefineValue(Node val, Set<String> defines) {
  switch (val.getType()) {
    case Token.STRING:
    case Token.NUMBER:
    case Token.TRUE:
    case Token.FALSE:
      return true;

    // Binary operators are only valid if both children are valid and have the same value.
    case Token.BITAND:
    case Token.BITNOT:
    case Token.BITOR:
    case Token.BITXOR:
      Node left = val.getFirstChild();
      Node right = val.getChild(1);
      return isValidDefineValue(left, defines) && isValidDefineValue(right, defines)
        && (left.getStringValue() == null ? left.getQualifiedName().equals(right.getStringValue()) : left.getStringValue().equals(right.getStringValue()));

    // Uniary operators are valid if the child is valid and has a specific value.
    case Token.NOT:
      Node child = val.getFirstChild();
      return isValidDefineValue(child, defines) && (child.getType() == Token.TRUE ? true : false);

    case Token.NEG:
      Node child = val.getChild(0);
      return isValidDefineValue(child, defines) && (child.getType() == Token.NUMBER && child.getDoubleValue() < 0);

    // Names are valid if and only if they are defines themselves.
    case Token.NAME:
    case Token.GETPROP:
      if (val.isQualifiedName()) {
        return defines.contains(val.getQualifiedName());
      }
  }
  return false;
}