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
    case Token.OR:
      Node lhs = val.getFirstChild();
      Node rhs = val.getLastChild();
      return isValidDefineValue(lhs, defines) && isValidDefineValue(rhs, defines);

    // BitNOT is valid for any child.
    case Token.BITNOT:
      return isValidDefineValue(val.getFirstChild(), defines);

    // Uniary operators are valid if the child is valid.
    case Token.NOT:
    case Token.NEG:
      return isValidDefineValue(val.getFirstChild(), defines);

    // Names are valid if and only if they are defines themselves.
    case Token.NAME:
    case Token.GETPROP:
      if (val.isQualifiedName()) {
        return defines.contains(val.getQualifiedName());
      } else {
        // Handle undefined variable in expressions like "1 + 8"
        Node firstChild = val.getFirstChild();
        if (firstChild != null && firstChild.getType() == Token.NAME) {
          return !defines.contains(firstChild.getText());
        }
      }
  }
  return false;
}