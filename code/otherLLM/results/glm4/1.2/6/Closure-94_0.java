static boolean isValidDefineValue(Node val, Set<String> defines) {
  switch (val.getType()) {
    case Token.STRING:
    case Token.NUMBER:
    case Token.TRUE:
    case Token.FALSE:
      return true;

    // Binary operators are valid if both children are valid.
    case Token.BITAND:
    case Token.BITOR:
    case Token.BITXOR:
      Node leftChild = val.getFirstChild();
      Node rightChild = val.getLastChild();
      return isValidDefineValue(leftChild, defines) && isValidDefineValue(rightChild, defines);

    // Unary operators are valid if the child is valid.
    case Token.BITNOT:
    case Token.NEG:
    case Token.NOT:
      return isValidDefineValue(val.getFirstChild(), defines);

    // Names are valid if and only if they are defined themselves.
    case Token.NAME:
    case Token.GETPROP:
      if (val.isQualifiedName()) {
        return defines.contains(val.getQualifiedName());
      }
  }
  return false;
}