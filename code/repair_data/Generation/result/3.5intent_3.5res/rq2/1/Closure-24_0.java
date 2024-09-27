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
                aliases.put(name, aliasVar);

                String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
                transformation.addAlias(name, qualifiedName);
            } else {
                // Fix for unsupported private variables
                fixTypeNode(n);
            }
        }
    }
}

private void fixTypeNode(Node typeNode) {
    if (typeNode.isString()) {
        String name = typeNode.getString();
        int endIndex = name.indexOf('.');
        if (endIndex == -1) {
            endIndex = name.length();
        }
        String baseName = name.substring(0, endIndex);
        Var aliasVar = aliases.get(baseName);
        if (aliasVar != null) {
            Node aliasedNode = aliasVar.getInitialValue();
            aliasUsages.add(new AliasedTypeNode(typeNode, aliasedNode.getQualifiedName() + name.substring(endIndex)));
        }
    }

    for (Node child = typeNode.getFirstChild(); child != null; child = child.getNext()) {
        fixTypeNode(child);
    }
}