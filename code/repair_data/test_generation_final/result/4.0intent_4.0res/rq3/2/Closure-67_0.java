private boolean isPrototypePropertyAssign(Node assign) {
    Node n = assign.getFirstChild();
    if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
        && n.getType() == Token.GETPROP
        ) {
        Node child = n.getFirstChild();
        if (child != null && child.getType() == Token.GETPROP) {
            Node grandchild = child.getFirstChild();
            if (grandchild != null && grandchild.getNext() != null) {
                Node nextNode = grandchild.getNext();
                if (nextNode.getType() == Token.STRING && "prototype".equals(nextNode.getString())) {
                    return true;
                }
            }
        }
    }

    return false;
}