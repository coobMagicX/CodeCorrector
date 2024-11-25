static boolean isValidDefineValue(Node val, Set<String> defines) {
  switch (val.getType()) {
    case Token.STRING:
    case Token.NUMBER:
    case Token.TRUE:
    case Token.FALSE:
      return true;

    // Binary operators are only valid if both children are correctly validated.
    case Token.BITAND:
    case Token.BITNOT:
    case Token.BITOR:
    case Token.BITXOR:
      return isValidDefineValue(val.getFirstChild(), defines) && 
             isValidDefineValue(val.getChild(1), defines);

    // Unary operators are valid if the child is correctly validated.
    case Token.NOT:
    case Token.NEG:
      return isValidDefineValue(val.getFirstChild(), defines);

    // Names are valid if they are defines themselves or contain a define.
    case Token.NAME:
    case Token.GETPROP:
      if (val.isQualifiedName()) {
        String qualifiedName = val.getQualifiedName();
        // Check if the name itself is a define
        if (defines.contains(qualifiedName)) return true;
        // Recursively check if any part of the qualified name is a define
        for (String part : qualifiedName.split("\\.")) {
          if (defines.contains(part)) return true;
        }
      } else {
        // If it's not a qualified name, just check if the name itself is a define
        if (defines.contains(val.getString())) return true;
      }
  }
  return false;
}