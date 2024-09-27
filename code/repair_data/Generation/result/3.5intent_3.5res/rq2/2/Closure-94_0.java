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

    // Uniary operators are valid if the child is valid.
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

static TernaryValue getBooleanValue(Node n) {
  switch (n.getType()) {
    case Token.STRING:
      return TernaryValue.forBoolean(n.getString().length() > 0);

    case Token.NUMBER:
      return TernaryValue.forBoolean(n.getDouble() != 0);

    case Token.NULL:
    case Token.FALSE:
    case Token.VOID:
      return TernaryValue.FALSE;

    case Token.NAME:
      String name = n.getString();
      if ("undefined".equals(name)
          || "NaN".equals(name)) {
        // We assume here that programs don't change the value of the keyword
        // undefined to something other than the value undefined.
        return TernaryValue.FALSE;
      } else if ("Infinity".equals(name)) {
        return TernaryValue.TRUE;
      }
      break;

    case Token.TRUE:
    case Token.ARRAYLIT:
    case Token.OBJECTLIT:
    case Token.REGEXP:
      return TernaryValue.TRUE;
  }

  return TernaryValue.UNKNOWN;
}

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

    // Uniary operators are valid if the child is valid.
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

boolean isValidDefineValue(Node val, Set<String> defines) {
  TernaryValue ternaryValue = getBooleanValue(val);
  return ternaryValue == TernaryValue.TRUE || (ternaryValue == TernaryValue.UNKNOWN && val.isQualifiedName() && defines.contains(val.getQualifiedName()));
}