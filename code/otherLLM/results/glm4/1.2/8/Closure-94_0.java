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
    case Token.AND:
    case Token.OR: {
      TernaryValue lhs = getExpressionBooleanValue(val.getFirstChild());
      TernaryValue rhs = getExpressionBooleanValue(val.getLastChild());
      return lhs != null && rhs != null; // Assuming the TernaryValue objects are non-null when valid
    }

    case Token.BITNOT:
    case Token.NOT:
    case Token.NEG: {
      TernaryValue child = getExpressionBooleanValue(val.getFirstChild());
      return child != null;
    }

    // Names are valid if and only if they are defines themselves.
    case Token.NAME:
    case Token.GETPROP:
      if (val.isQualifiedName()) {
        String name = val.getQualifiedName();
        return defines.contains(name);
      }
  }
  return false;
}