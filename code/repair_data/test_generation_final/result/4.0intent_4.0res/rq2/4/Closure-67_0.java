private boolean isPrototypePropertyAssign(Node assign) {
    Node n = assign.getFirstChild();
    if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)) {
        while (n.getType() == Token.GETPROP || n.getType() == Token.GETELEM) {
            Node propertyNode = n.getLastChild();
            if (propertyNode.getType() == Token.STRING && propertyNode.getString().equals("prototype")) {
                return true;
            }
            n = n.getFirstChild();
        }
    }

    return false;
}