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

        // Handle class inheritance relationships when recording function declarations
        Node parentFunctionDecl = getParentFunctionDeclaration(n);
        if (parentFunctionDecl != null) {
          NameInformation parentNs = createNameInformation(t, parentFunctionDecl, n);
          if (parentNs != null && parentNs.isPrototype) {
            recordPrototypeSet(ns.name, ns.name, nameNode);
          }
        }
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
        // Consider adding logic to handle multiple levels of inheritance
        Node parentFunctionDecl = getParentFunctionDeclaration(n);
        while (parentFunctionDecl != null) {
          NameInformation parentNs = createNameInformation(t, parentFunctionDecl, n);
          if (parentNs != null && parentNs.isPrototype) {
            recordPrototypeSet(name.name, name.name, n);
          }
          parentFunctionDecl = getParentFunctionDeclaration(parentFunctionDecl);
        }

        refNodes.add(new ClassDefiningFunctionNode(
            name, n, parent, parent.getParent()));
      }
    }
  }

  // Method to get the parent function declaration
  Node getParentFunctionDeclaration(Node node) {
    if (node.isFunctionDeclaration()) {
      return node;
    } else {
      return getParentFunctionDeclaration(node.getParent());
    }
  }
}