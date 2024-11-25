private boolean isPrototypePropertyAssign(Node assign) {
    Node n = assign.getFirstChild();
    if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign) &&
        n.getType() == Token.GETPROP) {

        // Check for chained GETPROP to determine if it targets 'prototype'
        Node chain = n;
        while (chain != null) {
            if (chain.getType() != Token.GETPROP) {
                break; // Not a chained property assignment
            }
            chain = chain.getFirstChild();
        }

        if (chain == null) { // No more GETPROP, not a chained property assignment
            return false;
        }

        // Check if the last chained property is 'prototype'
        Node prototypeCheck = chain.getFirstChild();
        if (prototypeCheck != null && prototypeCheck.getType() == Token.STRING &&
            prototypeCheck.getString().equals("prototype")) {
            return true;
        }
    }

    return false;
}