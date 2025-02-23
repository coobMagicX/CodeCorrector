public void visit(NodeTraversal t, Node n, Node parent) {

  // Record global variable and function declarations
  if (t.inGlobalScope()) {
    processGlobalScope(t, n, parent);
  }

  // Record assignments and call sites
  processAssignmentsAndCalls(t, n, parent);
}

private void processGlobalScope(NodeTraversal t, Node n, Node parent) {
  NameInformation ns = null;
  if (NodeUtil.isVarDeclaration(n)) {
    ns = createNameInformation(t, n, parent);
  } else if (NodeUtil.isFunctionDeclaration(n)) {
    Node nameNode = n.getFirstChild();
    if (nameNode != null) {
      ns = createNameInformation(t, nameNode, n);
    }
  } else if (NodeUtil.isObjectLitKey(n, parent)) {
    ns = createNameInformation(t, n, parent);
  }
  
  if (ns != null) {  // Generalized null check for any NameInformation
    JsName nameInfo = getName(ns.name, true);
    if (nameInfo != null) {
      recordSet(nameInfo.name, n);
    }
  }
}

private void processAssignmentsAndCalls(NodeTraversal t, Node n, Node parent) {
  if (n.isAssign()) {
    Node nameNode = n.getFirstChild();
    NameInformation ns = createNameInformation(t, nameNode, n);
    if (ns != null) {
      JsName nameInfo = getName(ns.name, ns.isPrototype);
      if (nameInfo != null) {  // Null check before recording
        if (ns.isPrototype) {
          recordPrototypeSet(ns.prototypeClass, ns.prototypeProperty, n);
        } else {
          recordSet(nameInfo.name, nameNode);
        }
      }
    }
  } else if (n.isCall()) {
    Node nameNode = n.getFirstChild();
    NameInformation ns = createNameInformation(t, nameNode, n);
    if (ns != null && ns.onlyAffectsClassDef) {
      JsName name = getName(ns.name, false);
      if (name != null) { // Ensuring that 'name' is not null
        refNodes.add(new ClassDefiningFunctionNode(
            name, n, parent, parent.getParent()));
      }
    }
  }
}
