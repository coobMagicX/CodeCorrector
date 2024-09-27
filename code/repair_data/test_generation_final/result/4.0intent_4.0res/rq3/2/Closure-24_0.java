private void findAliases(NodeTraversal t) {
    Scope scope = t.getScope();
    for (Var v : scope.getVarIterable()) {
        Node n = v.getNode();
        int type = n.getType();
        Node parent = n.getParent();
        if (parent.isVar()) {
            if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
                String name = n.getFirstChild().getString();
                Var aliasVar = scope.getVar(name);
                if (aliasVar != null && aliasVar.getInitialValue() != null) {
                    String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
                    aliases.put(name, aliasVar);
                    transformation.addAlias(name, qualifiedName);
                } else {
                    report(t, n, GOOG_SCOPE_ALIAS_ERROR, name);
                }
            } else {
                report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
            }
        }
    }
}