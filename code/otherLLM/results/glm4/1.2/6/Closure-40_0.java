public void visit(NodeTraversal t, Node n, Node parent) {
  // Record global variable and function declarations
  if (t.inGlobalScope()) {
    if (NodeUtil.isVarDeclaration(n)) {
      NameInformation ns = createNameInformation(t, n, parent);
      Preconditions.checkNotNull(ns);
      recordSet(ns.name, n);
    } else if (NodeUtil.isFunctionDeclaration(n)) {
      Node nameNode = n.getFirstChild();
      NameInformation ns = createNameInformation(t, nameNode, n);
      if (ns != null) {
        JsName nameInfo = getName(nameNode.getString(), true);
        recordSet(nameInfo.name, nameNode);
      }
    } else if (NodeUtil.isObjectLitKey(n, parent)) {
      NameInformation ns = createNameInformation(t, n, parent);
      if (ns != null) {
        // Check for correct recording of object literal keys
        if (!recordSet(ns.name, n)) {
          throw new IllegalStateException("Failed to record object literal key: " + ns.name);
        }
      }
    }
  }

  // Record assignments and call sites
  if (n.isAssign()) {
    Node nameNode = n.getFirstChild();
    NameInformation ns = createNameInformation(t, nameNode, n);
    if (ns != null) {
      // Check for correct recording of prototypes in assignments
      if (ns.isPrototype && ns.prototypeClass != null) {
        recordPrototypeSet(ns.prototypeClass, ns.prototypeProperty, n);
      } else {
        JsName nameInfo = getName(nameNode.getString(), true);
        if (nameInfo != null) {
          recordSet(nameInfo.name, nameNode);
        }
      }
    }
  } else if (n.isCall()) {
    Node nameNode = n.getFirstChild();
    NameInformation ns = createNameInformation(t, nameNode, n);
    if (ns != null && ns.onlyAffectsClassDef) {
      JsName name = getName(ns.name, false);
      if (name != null) {
        refNodes.add(new ClassDefiningFunctionNode(
            name, n, parent, parent.getParent()));
      }
    }
  }
}