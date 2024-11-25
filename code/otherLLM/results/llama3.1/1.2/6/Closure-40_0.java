public void visit(NodeTraversal t, Node n, Node parent) {

  // Record global variable and function declarations
  if (t.inGlobalScope()) {
    if (NodeUtil.isVarDeclaration(n)) {
      NameInformation ns = createNameInformation(t, n, parent);
      Preconditions.checkNotNull(ns);
      recordSet(ns.name, n);
    } else if (NodeUtil.isFunctionDeclaration(n) || NodeUtil.isObjectLitKey(n, parent)) {
      Node nameNode = n.getFirstChild();
      NameInformation ns = createNameInformation(t, nameNode, n);
      Preconditions.checkNotNull(ns);
      JsName nameInfo = getName(nameNode.getString(), true);
      recordSet(nameInfo.name, nameNode);
    }
  }

  // Record assignments and call sites
  if (n.isAssign()) {
    Node nameNode = n.getFirstChild();

    NameInformation ns = createNameInformation(t, nameNode, n);
    Preconditions.checkNotNull(ns);
    if (ns.isPrototype) {
      recordPrototypeSet(ns.prototypeClass, ns.prototypeProperty, n);
    } else {
      recordSet(ns.name, nameNode);
    }
  } else if (n.isCall()) {
    Node nameNode = n.getFirstChild();
    NameInformation ns = createNameInformation(t, nameNode, n);
    Preconditions.checkNotNull(ns);
    JsName name = getName(ns.name, false);
    if (name != null && ns.onlyAffectsClassDef) {
      refNodes.add(new ClassDefiningFunctionNode(
          name, n, parent, parent.getParent()));
    }
  }
}