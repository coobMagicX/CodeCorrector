static boolean isValidDefineValue(Node val, Set<String> defines) {
    switch (val.getType()) {
        case Token.STRING:
        case Token.NUMBER:
        case Token.TRUE:
        case Token.FALSE:
            return true;

        // Handling additional binary operators
        case Token.BITAND:
        case Token.BITOR:
        case Token.BITXOR:
        case Token.ADD:
        case Token.SUB:
        case Token.MUL:
        case Token.DIV:
            return isValidDefineValue(val.getFirstChild(), defines) &&
                   isValidDefineValue(val.getSecondChild(), defines);

        // Unary operators are valid if the child is valid.
        case Token.NOT:
        case Token.NEG:
            return isValidDefineValue(val.getFirstChild(), defines);

        // Names are valid if and only if they are defines themselves.
        case Token.NAME:
        case Token.GETPROP:
            if (val.isQualifiedName()) {
                return defines.contains(val.getQualifiedName());
            }
            break;
    }
    return false;
}