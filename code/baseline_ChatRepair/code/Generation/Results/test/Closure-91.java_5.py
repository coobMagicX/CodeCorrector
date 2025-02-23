public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
    // Checking for function nodes
    if (n.getType() == Token.FUNCTION) {
        JSDocInfo jsDoc = getFunctionJsDocInfo(n);
        if (jsDoc != null) {
            // If node has specific annotations, decide based on additional context
            if (jsDoc.isConstructor() || jsDoc.isInterface() || jsDoc.hasThisType() || jsDoc.isOverride()) {
                // We only want to skip traversal if this is a non-constructor or dangerous usage of 'this'
                if (!jsDoc.isConstructor()) {
                    return false;
                }
            }
        }

        // Check the parent type to filter out non-standard function declarations/statements
        int pType = parent.getType();
        if (!(pType == Token.BLOCK || pType == Token.SCRIPT ||
              pType == Token.NAME || pType == Token.ASSIGN ||
              pType == Token.STRING || pType == Token.NUMBER)) {
            return false;
        }
    }

    if (parent != null && parent.getType() == Token.ASSIGN) {
        Node lhs = parent.getFirstChild();
        Node rhs = lhs.getNext();
        if (n == lhs) {
            return true;  // Always traverse LHS
        } else {
            // Only traverse RHS if not assigning to a prototype property.
            if (NodeUtil.isGet(lhs)) {
                Node lastChild = lhs.getLastChild();
                // Do not traverse if explicitly setting to the prototype or its properties
                if (lastChild.getString().equals("prototype") ||
                    (lastChild.hasChildren() && lastChild.getFirstChild().getString().equals("prototype"))) {
                    return false;
                }
            }
        }
    }

    return true;
}
