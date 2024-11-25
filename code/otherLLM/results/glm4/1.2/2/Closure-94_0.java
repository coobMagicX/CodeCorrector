static boolean isValidDefineValue(Node val, Set<String> defines) {
  switch (val.getType()) {
    case Token.STRING:
    case Token.NUMBER:
    case Token.TRUE:
    case Token.FALSE:
      return true;

    // Binary operators are only valid if both children are valid.
    case Token.BITAND:
    case Token.BITNOT:
    case Token.BITOR:
    case Token.BITXOR:
      if (containsCall(val)) {
        Node firstChild = val.getFirstChild();
        Node secondChild = firstChild.getNextSibling();
        return isValidDefineValue(firstChild, defines) && isValidDefineValue(secondChild, defines);
      }
      break;

    // Uniary operators are valid if the child is valid.
    case Token.NOT:
    case Token.NEG:
      return isValidDefineValue(val.getFirstChild(), defines);

    // Names are valid if and only if they are defines themselves.
    case Token.NAME:
    case Token.GETPROP:
      if (val.isQualifiedName()) {
        String qualifiedName = val.getQualifiedName();
        return defines.contains(qualifiedName);
      }
      break;

    default:
      // Additional cases to handle other tokens can be added here.
  }
  return false;
}