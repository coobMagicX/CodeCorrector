static boolean isValidDefineValue(Node val, Set<String> defines) {
  switch (val.getType()) {
    case Token.STRING:
    case Token.NUMBER:
    case Token.TRUE:
    case Token.FALSE:
      return true;

    // Handling for binary expressions
    case Token.ADD:
    case Token.SUB:
    case Token.MUL:
    case Token.DIV:
    case Token.MOD:
      // Binary operators are valid if both children are valid.
      Node firstChild = val.getFirstChild();
      Node secondChild = firstChild.getNext(); // Accessing the second child
      return isValidDefineValue(firstChild, defines) && 
             isValidDefineValue(secondChild, defines);

    // Handling for bitwise binary expressions
    case Token.BITAND:
    case Token.BITOR:
    case Token.BITXOR:
      firstChild = val.getFirstChild();
      secondChild = firstChild.getNext(); // Accessing the second child
      return isValidDefineValue(firstChild, defines) && 
             isValidDefineValue(secondChild, defines);

    // Handling for unary expressions
    case Token.NOT:
    case Token.NEG:
    case Token.BITNOT:
      // Unary operators are valid if the child is valid.
      return isValidDefineValue(val.getFirstChild(), defines);

    // Handling for names and properties
    case Token.NAME:
    case Token.GETPROP:
      if (val.isQualifiedName()) {
        return defines.contains(val.getQualifiedName());
      }
      break;
  }
  return false;
}
