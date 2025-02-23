public void visit(NodeTraversal t, Node n, Node parent) {
    // Ensure we are in the global scope for declarations
    if (t.inGlobalScope()) {
        processGlobalScopeDeclarations(t, n, parent);
    }

    // Handle variable assignments
    if (n.isAssign()) {
        processAssignments(t, n);
    }

    // Handle function calls
    else if (n.isCall()) {
        processFunctionCalls(t, n, parent);
    }
}

private void processGlobalScopeDeclarations(NodeTraversal t, Node n, Node parent) {
    if (NodeUtil.isVarDeclaration(n)) {
        handleVarDeclaration(t, n, parent);
    } else if (NodeUtil.isFunctionDeclaration(n)) {
        handleFunctionDeclaration(t, n);
    } else if (NodeUtil.isObjectLitKey(n, parent)) {
        handleObjectLiteralKey(t, n, parent);
    }
}

private void handleVarDeclaration(NodeTraversal t, Node n, Node parent) {
    NameInformation ns = createNameInformation(t, n, parent);
    if (ns != null) {
        recordSet(ns.name, n);
    }
}

private void handleFunctionDeclaration(NodeTraversal t, Node n) {
    Node nameNode = n.getFirstChild();
    if (nameNode != null && nameNode.isName()) {
        NameInformation ns = createNameInformation(t, nameNode, n);
        if (ns != null) {
            JsName nameInfo = getName(nameNode.getString(), true);
            if (nameInfo != null) {
                recordSet(nameInfo.name, nameNode);
            }
        }
    }
}

private void handleObjectLiteralKey(NodeTraversal t, Node n, Node parent) {
    NameInformation ns = createNameInformation(t, n, parent);
    if (ns != null) {
        recordSet(ns.name, n);
    }
}

private void processAssignments(NodeTraversal t, Node n) {
    Node nameNode = n.getFirstChild();
    if (nameNode != null && nameNode.isName()) {
        NameInformation ns = createNameInformation(t, nameNode, n);
        if (ns != null) {
            if (ns.isPrototype) {
                recordPrototypeSet(ns.prototypeClass, ns.prototypeProperty, n);
            } else {
                recordSet(ns.name, nameNode);
            }
        }
    }
}

private void processFunctionCalls(NodeTraversal t, Node n, Node parent) {
    Node nameNode = n.getFirstChild();
    if (nameNode != null && nameNode.isName()) {
        NameInformation ns = createNameInformation(t, nameNode, n);
        if (ns != null && ns.onlyAffectsClassDef) {
            JsName name = getName(ns.name, false);
            if (name != null) {
                refNodes.add(new ClassDefiningFunctionNode(name, n, parent, parent.getParent()));
            }
        }
    }
}
