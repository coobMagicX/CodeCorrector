private boolean isPrototypePropertyAssign(Node assign) {
    Node n = assign.getFirstChild();
    if (n != null && n.isGetProp() && NodeUtil.isAssignmentOp(assign)) {
        Node getPropChain = n;
        while getPropChain.isGetProp() {
            Node property = getPropChain.getSecondChild();
            if (property.isString() && "prototype".equals(property.getString())) {
                return true;
            }
            getPropChain = getPropChain.getFirstChild();
        }
    }
    return false;
}
