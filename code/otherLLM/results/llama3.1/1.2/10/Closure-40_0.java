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

        // Record prototype inheritance for constructor functions
        if (isConstructorFunction(n)) {
          Node protoNode = n.getPrototype();
          NameInformation protoNs = createNameInformation(t, protoNode, parent);
          if (protoNs != null) {
            recordPrototypeSet(protoNs.name, ns.name, n);
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
        refNodes.add(new ClassDefiningFunctionNode(
            name, n, parent, parent.getParent()));
      }
    }
  }

  // Additional method to check if a function is a constructor
  private boolean isConstructorFunction(Node n) {
    return NodeUtil.isFunctionDeclaration(n) && hasConstructorKeyword(n);
  }

  // Method to check if a node has the 'constructor' keyword
  private boolean hasConstructorKeyword(Node n) {
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getString().equals("constructor")) {
        return true;
      }
    }
    return false;
  }

}