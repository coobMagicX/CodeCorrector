private void findAliases(NodeTraversal t) {
    Scope scope = t.getScope();
    for (Var v : scope.getVarIterable()) {
        Node n = v.getNode();
        int type = n.getType();
        Node parent = n.getParent();
        if (parent.isVar()) {
            if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
                String name = n.getFirstChild().getQualifiedName();
                Var aliasVar = scope.getVar(name);
                if (aliasVar != null) {
                    String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
                    aliases.put(name, aliasVar);
                    transformation.addAlias(name, qualifiedName);
                } else {
                    report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getFirstChild().getString());
                }
            } else {
                // This else block catches cases where the child node is not qualified
                report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
            }
        }
    }
}