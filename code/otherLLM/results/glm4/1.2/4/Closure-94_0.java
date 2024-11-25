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
      Node lhs = val.getFirstChild();
      Node rhs = val.getLastChild();
      boolean isLeftDefine = isValidDefineValue(lhs, defines);
      boolean isRightDefine = isValidDefineValue(rhs, defines);
      return isLeftDefine && isRightDefine;

    // Unary operators are valid if the child is valid.
    case Token.BITNOT:
    case Token.NOT:
    case Token.NEG:
      Node operand = val.getFirstChild();
      boolean isOperandDefine = isValidDefineValue(operand, defines);
      return isOperandDefine;

    // Names are valid if and only if they are defines themselves.
    case Token.NAME:
    case Token.GETPROP:
      if (val.isQualifiedName()) {
        return defines.contains(val.getQualifiedName());
      }
  }

  // Check if the value itself is defined
  if (defines.contains(val.getString())) {
    return true;
  }

  // For other types, it's not a valid define unless specified
  return false;
}