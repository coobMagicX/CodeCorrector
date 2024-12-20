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
            return isValidDefineValue(val.getFirstChild(), defines) && isValidDefineValue(val.getSecondChild(), defines);

        case Token.BITNOT:
        case Token.NOT:
        case Token.NEG:
            return isValidDefineValue(val.getFirstChild(), defines);

        // Names are valid if and only if they are defined themselves.
        case Token.NAME:
        case Token.GETPROP:
            String name = val.isQualifiedName() ? val.getQualifiedName() : val.getString();
            return defines.contains(name);
    }
    return false;
}