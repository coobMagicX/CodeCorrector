public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
    if (n.getType() == Token.FUNCTION) {
        JSDocInfo jsDoc = getFunctionJsDocInfo(n);
        if (jsDoc != null &&
            (jsDoc.isConstructor() ||
             jsDoc.isInterface() ||
             jsDoc.hasThisType() ||
             jsDoc.isOverride())) {
            return false;
        }

        int pType = parent.getType();
        if (!(pType == Token.BLOCK ||
              pType == Token.SCRIPT ||
              pType == Token.NAME ||
              pType == Token.ASSIGN ||
              pType == Token.STRING ||
              pType == Token.NUMBER)) {
            return false;
        }

        // Check for @lends annotation explicitly in object literals
        if (pType == Token.OBJECTLIT && jsDoc != null && jsDoc.getLendsName() != null) {
            return true;
        }
    }

    if (parent != null && parent.getType() == Token.ASSIGN) {
        Node lhs = parent.getFirstChild();
        Node rhs = lhs.getNext();

        if (n == lhs) {
            if (assignLhsChild == null) {
                assignLhsChild = lhs;
            }
        } else {
            // Check if assignment is to a prototype and handle @lends annotation
            if (NodeUtil.isGet(lhs)) {
                if (lhs.getType() == Token.GETPROP &&
                    lhs.getLastChild().getString().equals("prototype")) {
                    JSDocInfo jsDoc = getFunctionJsDocInfo(rhs);
                    if (jsDoc != null && jsDoc.getLendsName() != null) {
                        return true;
                    }
                    return false;
                }
                Node llhs = lhs.getFirstChild();
                if (llhs.getType() == Token.GETPROP &&
                    llhs.getLastChild().getString().equals("prototype")) {
                    JSDocInfo jsDoc = getFunctionJsDocInfo(rhs);
                    if (jsDoc != null && jsDoc.getLendsName() != null) {
                        return true;
                    }
                    return false;
                }
            }
        }
    }

    return true;
}