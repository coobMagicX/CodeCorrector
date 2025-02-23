public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
    if (n.getType() == Token.FUNCTION) {
        JSDocInfo jsDoc = getFunctionJsDocInfo(n);
        // Only prevent traversal into functions that are constructors or interfaces, or have @this or @override,
        // but only if these annotations are problematic in context.
        if (jsDoc != null) {
            if ((jsDoc.isConstructor() || jsDoc.isInterface()) &&
                 (jsDoc.hasThisType() || jsDoc.isOverride())) {
                // Proceed only if jsDoc belongs to a part of assignment to allow safe "this" usage.
                Node grandparent = parent.getParent();
                boolean isSafeThisContext = grandparent != null &&
                                            (grandparent.getType() == Token.ASSIGN ||
                                             grandparent.getType() == Token.GETPROP);
                
                if (!isSafeThisContext) {
                    return false;
                }
            }
        }

        int pType = parent.getType();
        // Avoid generally disallowed function contexts
        if (!(pType == Token.BLOCK ||
              pType == Token.SCRIPT ||
              pType == Token.NAME ||
              pType == Token.ASSIGN ||
              pType == Token.STRING ||
              pType == Token.NUMBER)) {
            return false;
        }
    }

    // Traversal control for the right-hand side of assignments
    if (parent != null && parent.getType() == Token.ASSIGN && n == parent.getFirstChild().getNext()) {
        Node lhs = parent.getFirstChild();
        if (NodeUtil.isGet(lhs)) {
            Node target = lhs.getLastChild();
            // Do not traverse when assigning to a prototype's properties or deeper properties.
            if ("prototype".equals(target.getString()) ||
                (target.getType() == Token.GETPROP && "prototype".equals(target.getFirstChild().getString()))) {
                return false;
            }
        }
    }

    return true;
}
