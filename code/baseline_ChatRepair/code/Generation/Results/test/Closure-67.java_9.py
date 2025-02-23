private boolean isPrototypePropertyAssign(Node assign) {
    Node n = assign.getFirstChild();
    if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
        && n.getType() == Token.GETPROP) {
        // Verify that we are dealing with a property access where the property owner itself is 'prototype'
        Node owner = n.getFirstChild();
        if (owner.getType() == Token.GETPROP) {
            Node grandChild = owner.getFirstChild();
            Node property = owner.getNext();
            
            if (grandChild != null && property != null 
                && property.getType() == Token.STRING
                && property.getString().equals("prototype")) {
                return true;
            }
        }
    }
    return false;
}
