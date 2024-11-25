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
        case Token.EQ:
        case Token.NE:
        case Token.LT:
        case Token.GT:
        case Token.LE:
        case Token.GE:
        case Token.SHL:
        case Token.SHR:
        case Token.USHR:
            if (!nodeTypeMayHaveSideEffects(val.getFirstChild(), null)) {
                return false;
            }
            if (val.getType() != Token.BITAND && val.getType() != Token.BITOR && val.getType() != Token.BITXOR) {
                if (!nodeTypeMayHaveSideEffects(val.getLastChild(), null)) {
                    return false;
                }
            }

        // Uniary operators are valid if the child is valid.
        case Token.NOT:
        case Token.NEG:
            return nodeTypeMayHaveSideEffects(val.getFirstChild(), null);

        // Names are valid if and only if they are defines themselves.
        case Token.NAME:
        case Token.GETPROP:
            if (val.isQualifiedName()) {
                return defines.contains(val.getQualifiedName());
            }
    }
    return false;
}