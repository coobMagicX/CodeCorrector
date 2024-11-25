private boolean isPrototypePropertyAssign(Node assign) {
    Node n = assign.getFirstChild();
    if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign)
        && n.getType() == Token.GETPROP) {

        // Check for chained properties by following the chain of GETPROP tokens
        while (n != null && n.getType() == Token.GETPROP) {
            n = n.getFirstChild();
            if (n == null || n.getType() != Token.STRING) {
                break; // No more chained properties or not a string token, exit the loop
            }
        }

        // If the last node in the chain is "prototype", return true
        if (n != null && n.getString().equals("prototype")) {
            return true;
        }
    }

    return false;
}