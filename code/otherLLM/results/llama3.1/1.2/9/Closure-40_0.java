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

        // Check for constructor inheritance
        if (nameNode.isConstructor()) {
          String inheritedClass = getInheritedClass(nameNode);
          if (inheritedClass != null) {
            NameInformation inheritedNs = createNameInformation(t, nameNode, parent);
            Preconditions.checkNotNull(inheritedNs);
            recordPrototypeSet(inheritedNs.name, inheritedNs.prototypeProperty, n);
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

        // Check for constructor inheritance
        String inheritedClass = getInheritedClass(nameNode);
        if (inheritedClass != null) {
          NameInformation inheritedNs = createNameInformation(t, nameNode, parent);
          Preconditions.checkNotNull(inheritedNs);
          recordPrototypeSet(inheritedNs.name, inheritedNs.prototypeProperty, n);
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

  // Improve constructor recognition
  if (n.isConstructor()) {
    NameInformation ns = createNameInformation(t, n, parent);
    if (ns != null) {
      recordSet(ns.name, n);

      String inheritedClass = getInheritedClass(n);
      if (inheritedClass != null) {
        NameInformation inheritedNs = createNameInformation(t, n, parent);
        Preconditions.checkNotNull(inheritedNs);
        recordPrototypeSet(inheritedNs.name, inheritedNs.prototypeProperty, n);
      }
    }
  }
}