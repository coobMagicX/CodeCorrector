static boolean isValidDefineValue(Node val, Set<String> defines) {
  switch (val.getType()) {
    case Token.STRING:
    case Token.NUMBER:
    case Token.TRUE:
    case Token.FALSE:
      return true;

    // Binary operators are only valid if both children are valid.
    case Token.BITAND:
    case Token.BITOR:
    case Token.BITXOR:
      boolean leftValid = isValidDefineValue(val.getFirstChild(), defines);
      boolean rightValid = val.numChildren() > 1 ? isValidDefineValue(val.getChild(1), defines) : false;
      return leftValid && rightValid;

    case Token.BITNOT:
      // Binary operators are only valid if both children are valid.
      return isValidDefineValue(val.getFirstChild(), defines);

    case Token.NOT:
    case Token.NEG:
      return isValidDefineValue(val.getFirstChild(), defines);

    // Names are valid if and only if they are defines themselves.
    case Token.NAME:
    case Token.GETPROP:
      if (val.isQualifiedName()) {
        return defines.contains(val.getQualifiedName());
      }
  }
  return false;
}