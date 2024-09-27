private void findAliases(NodeTraversal t) {
    Scope scope = t.getScope();
    for (Var v : scope.getVarIterable()) {
        Node n = v.getNode();
        int type = n.getType();
        Node parent = n.getParent();
        if (parent.isVar()) {
            if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
                String name = n.getString();
                Var aliasVar = scope.getVar(name);
                if (aliasVar != null) {
                    String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
                    if (qualifiedName != null) {
                        aliases.put(name, aliasVar);
                        transformation.addAlias(name, qualifiedName);
                    } else {
                        // Report as non-alias if no qualified name is found
                        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, name);
                    }
                } else {
                    // Report as non-alias if no alias variable is found
                    report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, name);
                }
            } else if (type == Token.FUNCTION) {
                // Ignore function declarations as they are handled differently
            } else {
                // Report non-alias variables including functions which are not handled
                report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
            }
        }
    }
}