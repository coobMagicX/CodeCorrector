private void findAliases(NodeTraversal t) {
    Scope scope = t.getScope();
    for (Var v : scope.getVarIterable()) {
        Node n = v.getNode();
        Node parent = n.getParent();
        if (parent.isVar()) {  // Check if the parent is a 'var' statement, correct context for aliases
            if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
                String name = n.getString();
                Var aliasVar = scope.getVar(name);
                if (aliasVar != null) {
                    aliases.put(name, aliasVar);
                    String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
                    transformation.addAlias(name, qualifiedName);
                } else {
                    report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, name);  // Reports non-existing variable used as alias
                }
            } else {
                report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());  // If not a qualified name, report error
            }
        } else {
            report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());  // If parent isn't 'var', report error
        }
    }

    // Ensure the function meets specific conditions for usage in goog.scope
    Node currentNode = t.getCurrentNode();
    if (currentNode != null && currentNode.isFunction()) {
        Node funcParent = currentNode.getParent();
        // Check if the function is the child of a CALL node and that CALL node should be the child of the root script
        if (!(funcParent.isCall() && funcParent.getParent().isScript())) {
            t.report(currentNode, GOOG_SCOPE_INVALID_FUNCTION_ERROR);  // Custom error if goog.scope function is not correctly structured
        }
    }
}
