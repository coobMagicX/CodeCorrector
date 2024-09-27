private void findAliases(NodeTraversal t) {
    Scope scope = t.getScope();
    for (Var v : scope.getVarIterable()) {
        Node n = v.getNode();
        int type = n.getType();
        Node parent = n.getParent();
        if (parent.isVar()) {
            if (n.hasChildren()) {
                Node firstChild = n.getFirstChild();
                if (firstChild.isQualifiedName()) {
                    String name = n.getString();
                    Var aliasVar = scope.getVar(name);
                    if (aliasVar != null) {
                        aliases.put(name, aliasVar);
                        String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
                        transformation.addAlias(name, qualifiedName);
                        // Bleeding functions already get a BAD_PARAMETERS error, so just
                        // do nothing.
                        // Parameters of the scope function also get a BAD_PARAMETERS
                        // error.
                    } else {
                        // This covers the case where the aliasVar is not found within the scope,
                        // treating it as a non-alias local.
                        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
                    }
                } else {
                    // This else block handles all the other non-qualified name cases
                    // which should be reported as non-alias locals.
                    report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
                }
            } else {
                // This covers the case where a variable is declared without an initialization.
                // Also report these as non-alias locals.
                report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
            }
        } else {
            // This branch covers nodes that are not directly under a var declaration,
            // which should also be treated as non-alias locals.
            report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
        }
    }
}