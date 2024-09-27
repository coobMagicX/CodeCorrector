private boolean isPrototypePropertyAssign(Node assign) {
    Node n = assign.getFirstChild();
    if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
        && n.getType() == Token.GETPROP
        ) {
        Node propertyNode = n.getSecondChild(); // Get the property part of the node

        if (propertyNode.getType() == Token.STRING && 
            propertyNode.getString().equals("prototype")) {
            return true; // Direct property access checks
        }

        // Handling chained properties or properties accessed as strings
        if (n.getFirstChild().getType() == Token.GETPROP) {
            Node child = n.getFirstChild().getSecondChild(); // Access second child to check for 'prototype'

            if (child.getType() == Token.STRING &&
                child.getString().equals("prototype")) {
                return true;
            }
        }
    }

    return false;
}