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
            Node left = val.getLeftChild();
            Node right = val.getRightChild();

            if (left == null || right == null) {
                return false;
            }

            boolean leftValid = isValidDefineValue(left, defines);
            boolean rightValid = isValidDefineValue(right, defines);

            return leftValid && rightValid;

        case Token.BITNOT:
            Node operand = val.getFirstChild();

            if (operand == null) {
                return false;
            }
            // Note: BITNOT does not require the operand to be defined
            return true; // Assuming BITNOT is valid regardless of operand

        case Token.NOT:
        case Token.NEG:
            Node child = val.getFirstChild();
            return child != null && isValidDefineValue(child, defines);

        // Names are valid if and only if they are defines themselves.
        case Token.NAME:
        case Token.GETPROP:
            if (val.isQualifiedName()) {
                return defines.contains(val.getQualifiedName());
            }
    }
    return false;
}