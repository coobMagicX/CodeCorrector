public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
    // Check if current node is a function
    if (n.getType() == Token.FUNCTION) {
        JSDocInfo jsDoc = getFunctionJsDocInfo(n);
        
        // Check JSDoc to determine if we need to skip traversal
        if (jsDoc != null && (jsDoc.isInterface() || jsDoc.hasThisType() || jsDoc.isOverride())) {
            return false;
        }
                
        // Ensure traversal of constructors regardless of other annotations
        if (jsDoc != null && jsDoc.isConstructor()) {
            // Allow traversal of constructors always
            return true;
        }

        // Don't traverse functions unless in proper context
        int pType = parent.getType();
        if (!(pType == Token.BLOCK ||
              pType == Token.SCRIPT ||
              pType == Token.NAME ||
              pType == Token.ASSIGN || 
              pType == Token.STRING || 
              pType == Token.NUMBER)) {
            return false;
        }
    }

    // Prevent traversal where node parent is an assignment to prototype properties
    if (parent != null && parent.getType() == Token.ASSIGN) {
        Node lhs = parent.getFirstChild();
        Node rhs = lhs.getNext();
        boolean isTestingLeftHand = n == lhs;

        if (isTestingLeftHand && assignLhsChild == null) {
            assignLhsChild = lhs;
        } else if (!isTestingLeftHand) {
            if (NodeUtil.isGet(lhs)) {
                Node getPropNode = lhs.getFirstChild();
                if (getPropNode.getType() == Token.GETPROP &&
                    "prototype".equals(getPropNode.getLastChild().getString())) {
                    return false;
                }
            }
        }
    }

    return true;
}
