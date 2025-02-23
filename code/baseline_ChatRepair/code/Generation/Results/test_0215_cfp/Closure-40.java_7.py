public void visit(NodeTraversal t, Node n, Node parent) {
    try {
        // Record global variable and function declarations
        if (t.inGlobalScope()) {
            if (NodeUtil.isVarDeclaration(n)) {
                NameInformation ns = createNameInformation(t, n, parent);
                Preconditions.checkNotNull(ns, "NameInformation should not be null");
                recordSet(ns.name, n);
            } else if (NodeUtil.isFunctionDeclaration(n)) {
                Node nameNode = n.getFirstChild();
                Preconditions.checkNotNull(nameNode, "Name node should not be null for function declarations");
                NameInformation ns = createNameInformation(t, nameNode, n);
                if (ns != null) {
                    JsName nameInfo = getName(nameNode.getString(), true);
                    Preconditions.checkNotNull(nameInfo, "JsName should not be null for function declarations");
                    recordSet(nameInfo.name, nameNode);
                }
            } else if (NodeUtil.isObjectLitKey(n, parent)) {
                NameInformation ns = createNameInformation(t, n, parent);
                if (ns != null) {
                    recordSet(ns.name, n);
                }
            }
        }

        // Record assignments and call sites
        if (n.isAssign()) {
            Node nameNode = n.getFirstChild();
            Preconditions.checkNotNull(nameNode, "Left-hand side of assignment should not be null");
            NameInformation ns = createNameInformation(t, nameNode, n);
            if (ns != null) {
                if (ns.isPrototype) {
                    recordPrototypeSet(ns.prototypeClass, ns.prototypeProperty, n);
                } else {
                    recordSet(ns.name, nameNode);
                }
            }
        } else if (n.isCall()) {
            Node nameNode = n.getFirstChild();
            Preconditions.checkNotNull(nameNode, "Function name node in call should not be null");
            NameInformation ns = createNameInformation(t, nameNode, n);
            if (ns != null && ns.onlyAffectsClassDef) {
                JsName name = getName(ns.name, false);
                if (name != null) {
                    refNodes.add(new ClassDefiningFunctionNode(name, n, parent, parent.getParent()));
                }
            }
        }
    } catch (RuntimeException e) {
        e.printStackTrace();
        throw new RuntimeException("Error during node visit: " + e.getMessage(), e);
    }
}
