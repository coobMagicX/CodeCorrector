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

        // Unary operators are valid if the child is valid.
        case Token.NOT:
        case Token.NEG:
            return isValidDefineValue(val.getFirstChild(), defines);

        // Names are valid if and only if they are defines themselves.
        case Token.NAME:
        case Token.GETPROP:
            if (val.isQualifiedName()) {
                return defines.contains(val.getQualifiedName());
            } else if (val.getType() == Token.ADD) {
                Node left = val.getFirstChild();
                Node right = val.getLastChild();
                TernaryValue leftValue = getBooleanValue(left);
                TernaryValue rightValue = getBooleanValue(right);
                return leftValue != TernaryValue.UNKNOWN && rightValue != TernaryValue.UNKNOWN;
            } else if (val.getType() == Token.CALL) {
                Node callee = val.getFirstChild();
                if (callee.getType() == Token.NAME && callee.getString().equals("String")) {
                    Node argument = callee.getNext();
                    if (argument != null) {
                        TernaryValue argumentValue = getBooleanValue(argument);
                        return argumentValue != TernaryValue.UNKNOWN;
                    }
                }
            }
    }
    return false;
}