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
        // Correctly handle the key of an object literal
        String key = NodeUtil.getNameForObjectlitKey(n);
        recordSet(key, n);
      }
    }
  }

  // Record assignments and call sites
  if (n.isAssign()) {
    Node nameNode = n.getFirstChild();

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

// Additional method for handling object literal keys
private String getNameForObjectlitKey(Node n) {
  String key;
  if (n.isString()) {
    key = n.getString();
  } else if (n.isIdentifier()) {
    key = n.getString();
  } else {
    throw new IllegalArgumentException("Unexpected node type for object lit key: " + n.getType());
  }
  return key;
}