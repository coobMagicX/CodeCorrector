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
      if (ns != null && !recordedNames.contains(ns.name)) { // Check for duplicate recordings
        recordedNames.add(ns.name); // Add the name to the set of recorded names
        recordSet(ns.name, n);
      }
    }
  }

  // Record assignments and call sites
  if (n.isAssign()) {
    Node nameNode = n.getFirstChild();

    NameInformation ns = createNameInformation(t, nameNode, n);
    if (ns != null) {
      recordedNames.add(ns.name); // Add the name to the set of recorded names
      if (ns.isPrototype) {
        recordPrototypeSet(ns.prototypeClass, ns.prototypeProperty, n);
      } else {
        recordSet(ns.name, nameNode);
      }
    }
  } else if (n.isCall()) {
    Node nameNode = n.getFirstChild();
    NameInformation ns = createNameInformation(t, nameNode, n);
    if (ns != null && !ns.onlyAffectsClassDef) { // Validate inheritance handling
      JsName name = getName(ns.name, false);
      if (name != null) {
        refNodes.add(new ClassDefiningFunctionNode(
            name, n, parent, parent.getParent()));
      }
    } else if (ns != null && ns.onlyAffectsClassDef) {
      JsName name = getName(ns.name, false);
      if (name != null) {
        // Handle object literal keys correctly
        Node keyNode = nameNode.getFirstChild();
        NameInformation keyNs = createNameInformation(t, keyNode, n);
        if (keyNs != null && !recordedNames.contains(keyNs.name)) { 
          recordedNames.add(keyNs.name); // Add the key to the set of recorded names
          refNodes.add(new ClassDefiningFunctionNode(
              name, n, parent, parent.getParent()));
        }
      }
    }
  }
}