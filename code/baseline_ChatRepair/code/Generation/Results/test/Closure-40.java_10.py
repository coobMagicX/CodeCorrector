public void visit(NodeTraversal t, Node n, Node parent) {

  // Record global variable and function declarations
  if (t.inGlobalScope()) {
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

  // Record assignments and call sites
  if (n.isAssign()) {
    Node nameNode = n.getFirstChild();
    NameInformation ns = createNameInformation(t, nameNode, n);
    if (ns != null) {
      JsName nameInfo = getName(ns.name, ns.isPrototype); // Getting name based on whether it's a prototype or not
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
    if (ns != null) {
      JsName name = getName(ns.name, false);
      if (name != null && ns.onlyAffectsClassDef) { // Ensuring that 'name' is not null
        refNodes.add(new ClassDefiningFunctionNode(
            name, n, parent, parent.getParent()));
      }
    }
  }
}
