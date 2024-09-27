private boolean isPrototypePropertyAssign(Node assign) {
    Node n = assign.getFirstChild();
    if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
        && (n.getType() == Token.GETPROP || n.getType() == Token.GETELEM)) {
        // We want to exclude the assignment itself from the usage list
        boolean isChainedProperty =
            n.getFirstChild().getType() == Token.GETPROP || n.getFirstChild().getType() == Token.GETELEM;

        if (isChainedProperty) {
            Node child = n.getFirstChild().getFirstChild().getNext();

            if (child.getType() == Token.STRING &&
                "prototype".equals(child.getString())) {
                return true;
            }
        }
    }

    return false;
}