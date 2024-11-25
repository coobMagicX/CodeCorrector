private boolean isPrototypePropertyAssign(Node assign) {
    Node n = assign.getFirstChild();
    if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
        && n.getType() == Token.GETPROP) {

        // Check for chained GETPROP
        while (n != null && n.getType() == Token.GETPROP) {
            n = n.getNextSibling();

            // If we reach a STRING node, check its content
            if (n.getType() == Token.STRING && "prototype".equals(n.getString())) {
                return true; // Direct match with 'prototype'
            }
        }

        // Reset 'n' to the start of chained GETPROP for further checks
        n = assign.getFirstChild();

        // Check for a direct prototype assignment in the first level of chaining
        if (n.getType() == Token.GETPROP && "prototype".equals(n.getNextSibling().getString())) {
            return true;
        }
    }

    return false;
}