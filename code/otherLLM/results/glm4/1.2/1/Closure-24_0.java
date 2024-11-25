private void findAliases(NodeTraversal t) {
    Scope scope = t.getScope();
    Map<String, Var> aliases = new HashMap<>();
    for (Var v : scope.getVarIterable()) {
        Node n = v.getNode();
        int type = n.getType();
        Node parent = n.getParent();

        // Check if the node is a local variable declaration
        boolean isLocalVarDeclaration = parent.isExpressionStatement() && parent.getFirstChild().isVariableDeclaration();

        if (isLocalVarDeclaration) {
            String name = n.getString();
            Var aliasVar = scope.getVar(name);
            aliases.put(name, aliasVar);

            // Check if the declared variable is an alias
            String qualifiedName = aliasVar.getInitialValue() != null ? aliasVar.getInitialValue().getQualifiedName() : null;
            if (qualifiedName != null) {
                transformation.addAlias(name, qualifiedName);
            }
        } else if (!parent.isFunctionDeclaration()) { // Exclude function declarations
            report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
        }
    }
}